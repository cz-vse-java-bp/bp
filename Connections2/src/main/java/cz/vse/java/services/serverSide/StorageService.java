package cz.vse.java.services.serverSide;


import cz.vse.java.handlers.ChangeProductQuantityHandler;
import cz.vse.java.handlers.ProductAllRequestHandler;
import cz.vse.java.handlers.ProductByIDRequestHandler;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.persistance.service.ProductService;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code StorageService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.services.serverSide
 */
public class StorageService extends AGeneralService {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ProductService productService;

    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link StorageService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public StorageService(String routerIP,
                          int routerPort,
                          int clientsPort,
                          int maxClients,
                          String pathToTrustStore,
                          String passwordToTrustStore,
                          String pathToKeyStore,
                          String passwordToKeyStore) {

        super(EServiceType.STORAGE_MANAGEMENT,
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

        this.productService = new ProductService();

        super.clients.addMessageHandler(new ProductByIDRequestHandler(null));
        super.clients.addMessageHandler(new ProductAllRequestHandler(null));
        super.clients.addMessageHandler(new ChangeProductQuantityHandler(null));
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

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link ProductService} formed {@code productService}
     * of the instance of {@link StorageService}
     *
     * @return the value of {@code productService}
     * @see ProductService
     * @see StorageService
     */
    public ProductService getProductService() {

        return productService;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of StorageService.
     *
     */
    public static void main(String[] args){
        
        System.err.println(">>> QuickTest: StorageService class");
        System.err.println(">>> Creating StorageService instance...");

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );


        ClassLoader classLoader = StorageService.class.getClassLoader();
        File file = new File(classLoader.getResource("stores/keyStore.jks").getFile());

        ClassLoader classLoader2 = StorageService.class.getClassLoader();
        File file2 = new File(classLoader2.getResource("stores/trustStore.jts").getFile());


        StorageService instance = new StorageService(
                "localhost",
                888,
                1234,
                50,
                file2.getAbsolutePath(), "changeit",
                file.getAbsolutePath(), "changeit"

        );
        
        //code
        
        new Thread(instance).start();
        System.err.println(">>> Creation successfull...");
    }
    

}
