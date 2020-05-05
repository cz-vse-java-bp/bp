package cz.vse.java.services.clientSide;


import cz.vse.java.connections.clientSide.C2RConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.management.ClientRouterManagement;
import cz.vse.java.connections.utils.management.ClientServiceManagement;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.messages.utils.future.MessageTaskContainer;
import cz.vse.java.messages.utils.past.ReceivedMessageContainer;
import cz.vse.java.services.serverSide.AService;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.references.SRCContainer;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.services.references.ServiceReferenceContainer;
import cz.vse.java.util.FingerPrint;
import cz.vse.java.util.SSLClientConfigManager;
import cz.vse.java.util.Token;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.util.persistance.entities.orders.PreOrder;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.entities.tasks.TaskContainer;
import cz.vse.java.util.userData.UserProperties;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**************************************************************
 * <p>Class Client is Singleton by design pattern definition
 * with only one possible instance of this class.</p>
 *
 * <p>The class has <b>Thread-safe</b> getInstance() creation
 * mechanism implemented to be sure there is only one instance.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.services
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class Client extends AService implements Runnable, IObserver {

    /* *******************************************************/
    /* Instance variables ************************************/

    private ClientRouterManagement router;
    private SRCContainer srcContainer;
    private MessageTaskContainer messageTaskContainer;
    private ClientServiceManagement services;

    private PreOrder preOrder;


    private final CopyOnWriteArrayList<IObserver> observers;
    private TaskContainer tasks;

    private int maxTasks = 5;

    private ProductsContainer products;

    private final ArrayList<ReceivedMessageContainer> receivedMessages;

    private boolean listeningToTasks = false;


    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile Client singletonInstance = null;


    /**
     * Private {@link Logger} instance - Logger of the {@link Client} class
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    private static String pathToTrustStore = null;
    private static String passwordToTrustStore = null;

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the Client class.</p>
     */
    private Client() {

        super(EServiceType.CLIENT);

        this.srcContainer = new SRCContainer();
        this.messageTaskContainer = new MessageTaskContainer();
        this.observers = new CopyOnWriteArrayList<>();
        this.receivedMessages = new ArrayList<>();
        this.tasks = new TaskContainer();
        this.products = new ProductsContainer();
        this.createNewPreOrder();
        ISearchingEngine se = new ProductNameSearch(
                this.products,
                false);

        LOG.log(Level.INFO, "Adding search engine: " + se.getClass().getName());
    }


    /* *******************************************************/
    /* Instance methods **************************************/



    /**
     * <p>Prepares the connection with the router.</p>
     *
     * @param ip    IP address, where should the client
     *              connect to
     *
     * @param port  port, what the router is listening
     *              at for new clients.
     */
    public void prepareRouterConnection(String ip,
                                        int port) {

        router = new ClientRouterManagement(
                this,
                ip,
                port,
                Client.pathToTrustStore,
                Client.passwordToTrustStore);
    }


    public void prepareConnections(String routerIP, int routerPort) {

        prepareRouterConnection(routerIP, routerPort);

        this.services = new ClientServiceManagement(this,
                pathToTrustStore,
                passwordToTrustStore
        );

        ((C2RConnection) router.getRouter()).addObserver(this.messageTaskContainer);
        this.srcContainer.addObserver(this.services);
        this.tasks.addObserver(this);
    }


    /**
     * <p>Sets new {@link PreOrder} instance.</p>
     */
    public void createNewPreOrder() {

        this.preOrder = new PreOrder();
    }


    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     *
     * <p>First of all, checks the tasks.</p>
     */
    @Override
    public void update() {

        notifyObservers();
    }


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

        new Thread(router).start();

        /* Initialization of secondary thread removing all received messages
        * older than 30 seconds in past. It does it every 10 seconds. Saves
        * memory resources and supports stability and performance. */

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true) {

                    clearReceivedMessages(30L);

                    try {

                        Thread.sleep(10000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.setDaemon(true);
        t.start();
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
     * <p>Adds {@link ServiceReference} to the container.</p>
     *
     * @param serviceReference  service reference to be added.
     */
    public void addServiceReference(ServiceReference serviceReference) {

        this.srcContainer.add(new ServiceReferenceContainer(serviceReference));
        this.messageTaskContainer.update();
        this.notifyObservers();
    }


    /**
     * <p>Adds the given {@link Token} instance to prepared
     * {@link ServiceReference} in {@link SRCContainer}.
     * This is provided using {@link EServiceType} interpretation
     * of service.</p>
     *
     * @param token     to be added
     * @param type      the service reference shall be found by
     */
    public void addToken(Token token, EServiceType type) {

        this.srcContainer.addToken(token, type);
        this.messageTaskContainer.update();
        this.notifyObservers();
    }


    /**
     * <p>Sends a message to the given service.</p>
     *
     * @param message   to be sent
     * @param type      type of the service - the receiver.
     *
     * @return          If the message was sent. The possibility
     *                  of sending the message depends on what
     *                  service is the receiver - when it's
     *                  {@code CLIENT}, the sending will be refused.
     *                  If it's router, it returns true. When it's
     *                  any other service <b>and</b> is accessable,
     *                  only then the message sending can be provided.
     */
    public boolean send(IMessage message, EServiceType type) {

        if(type.equals(EServiceType.ROUTER)) {

            this.router.send(message);
            return true;

        } else if(type.equals(EServiceType.CLIENT)) {

            return false;

        } else {

            IConnection connection = this.services.getServiceConnection(type);

            if(connection != null) {

                connection.send(message);
                return true;

            } else {

                return false;
            }
        }
    }


    /**
     * <p>Adds a new {@link MessageTask} to the container of
     * message tasks. It will be sent in the future or the message
     * task expires - when the client will not be authenticated
     * at the proper service.</p>
     *
     * @param task  to be done in the future.
     */
    public void addMessageTask(MessageTask task) {

        this.messageTaskContainer.add(task);
        this.messageTaskContainer.update();
    }


    public boolean clearTasks() {

        this.tasks.clear();

        return true;
    }


    /**
     * <p>Adds {@link Task} instance to the {@link TaskContainer} saved
     * in the {@code tasks} field.</p>
     *
     * @param task  to be added.
     */
    public void addTask(Task task) {

        if(listeningToTasks) {

            System.out.println("Task state: " + task.getState());

            this.tasks.remove(task.getId());
            this.tasks.add(task);

            if (task.getState().equals(ETaskState.ASSIGNED)) {

                this.addMessageTask(new MessageTask(
                        new TaskStateChange(task.getId(),
                                ETaskState.CONFIRMED,
                                UserProperties.getInstance().getUserName()),
                        EServiceType.TASK_SERVICE
                ));
            }

        } else {

            if (task.getState().equals(ETaskState.ASSIGNED)) {

                this.addMessageTask(new MessageTask(
                                new RefuseTask(
                                        UserProperties.getInstance().getUserName(),
                                        task.getId()
                                ),
                                EServiceType.TASK_SERVICE
                        )
                );
            }
        }
    }


    public void addReceivedMessage(IMessage message) {

        synchronized (this.receivedMessages) {

            this.receivedMessages.add(new ReceivedMessageContainer(message));
            notifyObservers();
        }
    }

    public void clearReceivedMessages(Long secondsPast) {

        synchronized (this.receivedMessages) {

            ArrayList<ReceivedMessageContainer> deletables = new ArrayList<>();
            LocalDateTime since = LocalDateTime.now().minusSeconds(secondsPast);

            for (ReceivedMessageContainer rmc : this.receivedMessages) {

                if(rmc.getTimeOfReceiving().isBefore(since)) {

                    deletables.add(rmc);
                }
            }

            LOG.log(Level.SEVERE, "Cleaning messages older than "
                    + since.format(DateTimeFormatter.ISO_TIME) +
                    ". Number of them: " + deletables.size());

            this.receivedMessages.removeAll(deletables);
        }
    }


    /**
     * <p>Finds all messages by it's class name.</p>
     *
     * @param className     by it's looked for
     *
     * @return              the {@link List} of messages
     *                      with the class' name corresponding.
     */
    public List<IMessage> getReceivedByClass(String className) {

        List<IMessage> messages = new ArrayList<>();

        synchronized (this.receivedMessages) {

            for (ReceivedMessageContainer rmc : this.receivedMessages) {

                if(rmc.getMessage().getClass().getName().equals(className)) {

                    messages.add(rmc.getMessage());
                }
            }
        }

        return messages;
    }


    public void requestAllMyTasks() {

        this.addMessageTask(new MessageTask(

                new GiveMeMyTasks(UserProperties.getInstance().getUserName()),
                EServiceType.TASK_SERVICE)
        );
    }



    /* *******************************************************/
    /* Getters and setters ***********************************/

    /**
     * Getter for {@link MessageTaskContainer} formed {@code messageTaskContainer}
     * of the instance of {@link Client}
     *
     * @return the value of {@code messageTaskContainer}
     * @see MessageTaskContainer
     * @see Client
     */
    public MessageTaskContainer getMessageTaskContainer() {

        return messageTaskContainer;
    }

    /**
     * Getter for {@link PreOrder} formed {@code preOrder}
     * of the instance of {@link Client}
     *
     * @return the value of {@code preOrder}
     * @see PreOrder
     * @see Client
     */
    public PreOrder getPreOrder() {

        return preOrder;
    }

    /**
     * <p>Finds the connection by it's type.</p>
     *
     * @param type  of the demanded connection with service.
     *
     * @return      the connection. When the given type equals
     *              {@code CLIENT}, it's refused and throws
     *              {@link UnsupportedOperationException}.
     */
    public IConnection getConnectionWithService(EServiceType type) {

        if(type.name().equals(EServiceType.ROUTER.name())) {

            return router.getRouter();

        } else if(type.equals(EServiceType.CLIENT)) {

            throw new UnsupportedOperationException("Unsupported to communicate with yourself!");

        } else {

            return this.services.getServiceConnection(type);
        }
    }


    /**
     * <p>Returns the {@link ClientRouterManagement} instance.</p>
     *
     * @return  the Connection manager of the router connection.
     */
    public ClientRouterManagement getRouter() {

        return router;
    }


    /**
     * Getter for {@link SRCContainer} formed {@code srcContainer}
     * of the instance of {@link Client}
     *
     * @return the value of {@code srcContainer}
     * @see SRCContainer
     * @see Client
     */
    public SRCContainer getSrcContainer() {

        return srcContainer;
    }

    /**
     * Getter for {@link ProductsContainer} formed {@code products}
     * of the instance of {@link Client}
     *
     * @return the value of {@code products}
     * @see ProductsContainer
     * @see Client
     */
    public ProductsContainer getProducts() {

        return products;
    }



    /**
     * <p>Returns the port the clients can connect to the service at.</p>
     *
     * @return integer representation of port
     */
    @Override
    public int getClientsPort() {

        throw new UnsupportedOperationException("Not supported - Client's port does not make any sense!");
    }

    /**
     * <p>Returns the IP address this service is working at.</p>
     *
     * @return {@link String} representation of the address
     * the clients can connect with this service at.
     */
    @Override
    public String getClientsIP() {

        return new FingerPrint().getIp();
    }

    /**
     * Getter for {@link TaskContainer} formed {@code tasks}
     * of the instance of {@link Client}
     *
     * @return the value of {@code tasks}
     * @see TaskContainer
     * @see Client
     */
    public TaskContainer getTasks() {

        return tasks;
    }


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of Client.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the Client class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static Client getInstance() {

        if (singletonInstance == null) {

            synchronized (Client.class) {

                singletonInstance = new Client();
            }
        }

        return singletonInstance;
    }


    /**
     * <p>Resets the static field of {@code singletonInstance}
     * to default state - it is set to {@code null} again.</p>
     *
     * <p><b>ALL SET DATA ARE GONNA BE REMOVED</b></p>
     */
    public static void reset() {

        singletonInstance = null;
    }

    /**
     * <p>Setter for the {@code String}-formed
     * {@code pathToTrustStore} and {@code passwordToTrustStore}
     * variables.</p>
     *
     * @param pathToTrustStore given String value to
     *                         be set to the variable
     * @see String
     * @see Client
     */
    public static void setTrustStore(String pathToTrustStore, String passwordToTrustStore) {

        if(Client.pathToTrustStore == null && Client.passwordToTrustStore == null) {

            Client.pathToTrustStore = pathToTrustStore;
            Client.passwordToTrustStore = passwordToTrustStore;
            SSLClientConfigManager.getInstance().init(Client.pathToTrustStore, Client.passwordToTrustStore);
        }
    }

    /**
     * <p>Setter for the {@code PreOrder} formed
     * {@code preOrder} variable.</p>
     *
     * @param preOrder given PreOrder value to
     *                 be set to the variable
     * @see PreOrder
     * @see Client
     */
    public void setPreOrder(PreOrder preOrder) {

        this.preOrder = preOrder;
    }

    /**
     * Getter for {@link boolean} formed {@code listeningToTasks}
     * of the instance of {@link Client}
     *
     * @return the value of {@code listeningToTasks}
     * @see boolean
     * @see Client
     */
    public boolean isListeningToTasks() {

        return listeningToTasks;
    }

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code listeningToTasks} variable.</p>
     *
     * @param listeningToTasks given $field.typeName value to
     *                         be set to the variable
     * @see boolean
     * @see Client
     */
    public void setListeningToTasks(boolean listeningToTasks) {

        this.listeningToTasks = listeningToTasks;
    }

    /* *******************************************************/
    /* Main method *******************************************/

    /**
     * The main method of the class of Client.
     *
     */
    public static void main(String[] args) throws InterruptedException {
       
        System.err.println(">>> QuickTest: Client class"); 
        System.err.println(">>> Creating Client instance...");

        ClassLoader classLoader = Client.class.getClassLoader();
        File file = new File(classLoader.getResource("stores/trustStore.jts").getFile());


        Client.setTrustStore(file.getAbsolutePath(), "changeit");

        UserProperties.getInstance().setUserName("cimj00");
        UserProperties.getInstance().setPassword("password");

        Client instance = Client.getInstance();


        instance.prepareConnections(
                "192.168.1.105",
                9889
        );

        instance.run();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    instance.messageTaskContainer.update();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();

        //instance.addMessageTask(new MessageTask(new ServiceReferenceRequest(EServiceType.TEXT_PRINTER), EServiceType.ROUTER));
        //instance.addMessageTask(new MessageTask(new ServiceReferenceRequest(EServiceType.STORAGE_MANAGEMENT), EServiceType.ROUTER));
        //instance.addMessageTask(new MessageTask(new ProductByIDRequest(4L), EServiceType.STORAGE_MANAGEMENT, 30));
        //instance.addMessageTask(new MessageTask(new ProductAllRequest(), EServiceType.STORAGE_MANAGEMENT, 25));
        //instance.addMessageTask(new MessageTask(new ChangeProductQuantity(0, 4L), EServiceType.STORAGE_MANAGEMENT));
        instance.addMessageTask(new MessageTask(new ServiceReferenceRequest(EServiceType.TASK_SERVICE), EServiceType.ROUTER));
        //instance.addMessageTask(new MessageTask(new ListeningForTasksContainer(UserProperties.getInstance().getUserName(), true), EServiceType.TASK_SERVICE));
        //instance.addMessageTask(new MessageTask(new ServiceReferenceRequest(EServiceType.ORDER_MANAGEMENT), EServiceType.ROUTER));

        //instance.addMessageTask(new MessageTask(new UniqueOrderIdentRequest(), EServiceType.ORDER_MANAGEMENT, 120L));
        //instance.addMessageTask(new MessageTask(new GiveMeRoles("jira00"), EServiceType.ROUTER));


        //String ident = instance.getPreOrder().getIdentificator();

        //Thread.sleep(2000);
/*
        instance.addMessageTask(new MessageTask(new GiveMeMyOrder(ident), EServiceType.ORDER_MANAGEMENT));

        instance.addMessageTask(new MessageTask(new SetContactToOrderMessage("yourmama@email.com", ident), EServiceType.ORDER_MANAGEMENT));
        instance.addMessageTask(new MessageTask(new SetNoteToOrderMessage("Note kemo", ident), EServiceType.ORDER_MANAGEMENT));
*/

        instance.addMessageTask(new MessageTask(new ListeningForTasksContainer(UserProperties.getInstance().getUserName(), true), EServiceType.TASK_SERVICE));

        //Thread.sleep(6000);


        //Task task = instance.getTasks().getTasks().get(0);


        //instance.addMessageTask(new MessageTask(new TryToGenerateOrder(ident), EServiceType.ORDER_MANAGEMENT));

    }
}
