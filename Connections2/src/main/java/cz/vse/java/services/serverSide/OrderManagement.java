package cz.vse.java.services.serverSide;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.*;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.PreOrder;
import cz.vse.java.util.persistance.entities.tasks.*;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.service.OrderService;
import cz.vse.java.util.persistance.service.TaskService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 12. 04. 2020
 *
 *
 * @see cz.vse.java.services.serverSide
 */
public class OrderManagement extends AGeneralService {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<IObserver> observers;
    private final ArrayList<ISideTaskAssigner> sideTasksGen;

    private final CopyOnWriteArrayList<PreOrder> preOrders;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderManagement(String routerIP,
                           int routerPort,
                           int clientsPort,
                           int maxClients,
                           String pathToTrustStore,
                           String passwordToTrustStore,
                           String pathToKeyStore,
                           String passwordToKeyStore) {

        super(EServiceType.ORDER_MANAGEMENT,
                routerIP,
                routerPort,
                clientsPort,
                maxClients,
                false,
                pathToTrustStore,
                passwordToTrustStore,
                pathToKeyStore,
                passwordToKeyStore);

        this.observers = new CopyOnWriteArrayList<>();
        this.sideTasksGen = new ArrayList<>();
        this.sideTasksGen.add(new RandomCheckSideTask((short) 50));
        this.preOrders = new CopyOnWriteArrayList<>();

        this.clients.addMessageHandler(new OrderContainerMessageHandler(null));
        this.clients.addMessageHandler(new UniqueOrderIdentRequestHandler(null));
        this.clients.addMessageHandler(new AddPreOrderItemHandler(null));
        this.clients.addMessageHandler(new GiveMeMyOrderHandler(null));
        this.clients.addMessageHandler(new SetNoteToOrderMessageHandler(null));
        this.clients.addMessageHandler(new SetContactToOrderMessageHandler(null));
        this.clients.addMessageHandler(new SetSubmitterToOrderHandler(null));
        this.clients.addMessageHandler(new TryToGenerateOrderHandler(null));
        this.clients.addMessageHandler(new RemovePreOrderItemHandler(null));

        this.clients.addObserver(this);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        new Thread(super.router).start();
        new Thread(super.clients).start();
    }


    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.addIfAbsent(observer);
        }
    }

    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void removeObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.remove(observer);
        }
    }

    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    @Override
    public void notifyObservers() {

        synchronized (this.observers) {

            for (IObserver observer : observers) {

                observer.update();
            }
        }
    }


    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     */
    @Override
    public void update() {
        super.update();


        synchronized (this.preOrders) {

            PreOrder toBeReseted = null;

            for (PreOrder po : this.preOrders) {

                boolean found = false;

                for (IConnection conn : this.clients.getClients()) {

                    if(!conn.getToken().equals(po.getToken())) {

                        found = true;
                        break;
                    }
                }

                if(!found) {

                    toBeReseted = po;
                    break;
                }
            }

            if(toBeReseted != null) {

                try {

                    toBeReseted.reset();
                    this.preOrders.remove(toBeReseted);

                } catch (SQLException e) {

                    LOG.log(Level.SEVERE, "Failed to reset the unfinished PreOrder!");
                    LOG.log(Level.SEVERE, "PreOrder was like: " + toBeReseted.toString());
                }
            }
        }
    }

    /**
     * <p>Persists the given {@link Order} to DB.</p>
     *
     * @param order to be persisted
     */
    public boolean persistNewOrder(Order order) {

        OrderService os = new OrderService();

        try {

            order.setId(os.getUniqueId());

            for (OrderItem oi : order.getOrderItems()) {

                oi.getTasks().clear();
            }

            os.persist(order);

            this.generateTasks(order);

            return true;

        } catch (SQLException e) {

            LOG.log(Level.SEVERE, "Error while order persistence! Error n.: "
                    + e.getErrorCode()
                    + " Error message: " +  e.getMessage());

        } catch (NullPointerException e) {

            LOG.log(Level.SEVERE, "Connection with DB was not established probably! " + e.getMessage());
        }
        return false;
    }


    /**
     * <p>Removes {@link PreOrder} from {@code preOrdes}
     * container field.</p>
     *
     * <p>The one to be removes is found by the generated
     * random {@code ident}, which every {@link PreOrder}
     * should have. When no pre-order with such an ident
     * is found, nothing is deleted.</p>
     *
     * @param ident     the {@link PreOrder} meant to
     *                  be deleted has
     */
    public void removePreOrder(String ident) {

        synchronized (this.preOrders) {

            PreOrder po = this.getPreOrder(ident);

            if(po != null) {

                preOrders.remove(po);
            }
        }
    }


    /**
     * <p>Generates {@link Task}s for each {@link OrderItem} from
     * given {@link Order}. First of all, it generates main tasks
     * like for preparing {@link Product}s in given quantity.</p>
     *
     * <p>After this procedure, it prepares side tasks using
     * {@link ISideTaskAssigner} interface implementing instances.
     * This could be for example generating tasks like random check
     * of the storage place, if the products are in the quantity,
     * how it should by the system be.</p>
     *
     * @param order     {@link Order} the tasks should be generated for
     */
    public void generateTasks(Order order) {

        for (OrderItem orderItem : order.getOrderItems()) {

            Task t = new Task();
            TaskService ts = new TaskService();

            try {

                t.setId(ts.getUniqueId());
                t.setState(ETaskState.NOT_ASSIGNED);
                t.setUser(null);
                orderItem.addTask(t);
                t.setCreated(LocalDateTime.now());

                t.setDescription(
                        TaskDescriptionGenerator.getProductPreparation(
                                orderItem.getProduct(),
                                orderItem
                        )
                );

                ts.persist(t);

                for (ISideTaskAssigner sideTaskGen : this.sideTasksGen) {

                    Task sideTask = sideTaskGen.assign(orderItem);

                    if(sideTask != null) {

                        ts.persist(sideTask);
                    }
                }

            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    public void createNewPreorder(String identificator, IConnection connection) {

        synchronized (this.preOrders) {

            PreOrder po = new PreOrder();
            po.setIdentificator(identificator);
            po.setToken(connection.getToken());
            this.preOrders.add(po);
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code preOrders}
     * of the instance of {@link OrderManagement}
     *
     * @return the value of {@code preOrders}
     * @see CopyOnWriteArrayList<>
     * @see OrderManagement
     */
    public CopyOnWriteArrayList<PreOrder> getPreOrders() {

        return preOrders;
    }


    public PreOrder getPreOrder(String identificator) {

        synchronized (this.preOrders) {

            for (PreOrder po : this.preOrders) {

                if(po.getIdentificator().equals(identificator)) {

                    return po;
                }
            }
        }
        return null;
    }


    public List<String> getPreOrdersIdentificators() {

        List<String> idents = new ArrayList<>();

        synchronized (this.preOrders) {

            for (PreOrder preOrder : this.preOrders){

                idents.add(preOrder.getIdentificator());
            }
        }

        return idents;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of OrderManagement.
     *
     */
    public static void main(String[] args){
        
        System.err.println(">>> QuickTest: OrderManagement class");
        System.err.println(">>> Creating OrderManagement instance...");


        DatabaseConnectionContainer.getInstance().add(
                EDBUse.USER_AUTHENTICATION,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.FINGERPRINT_AUTHENTICATION,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.EMPLOYEE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.TASK_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.ORDERS_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );



        ClassLoader classLoader = OrderManagement.class.getClassLoader();
        File file = new File(classLoader.getResource("stores/keyStore.jks").getFile());

        ClassLoader classLoader2 = OrderManagement.class.getClassLoader();
        File file2 = new File(classLoader2.getResource("stores/trustStore.jts").getFile());



        OrderManagement instance = new OrderManagement(
                "localhost",
                888,
                1010,
                50,
                file.getAbsolutePath(), "changeit",
                file2.getAbsolutePath(), "changeit"
        );

        new Thread(instance).start();

    }
}
