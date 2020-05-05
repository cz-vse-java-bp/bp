package cz.vse.java.services.serverSide;


import cz.vse.java.services.serverSide.config.ServerConfiguration;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.utils.xml.XMLSchemaValidator;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**************************************************************
 * <p>Class Server is Singleton by design pattern definition
 * with only one possible instance of this class.</p>
 *
 * <p>The class has <b>Thread-safe</b> getInstance() creation
 * mechanism implemented to be sure there is only one instance.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.services
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class Server implements Runnable, ISubject {

    /* *******************************************************/
    /* Instance variables ************************************/

    private final CopyOnWriteArrayList<AService> services;
    private Router router;
    private final CopyOnWriteArrayList<IObserver> observers;

    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile Server singletonInstance = null;


    /**
     * Private {@link Logger} instance - Logger of the {@link Server} class
     */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the Server class.</p>
     */
    private Server() {

        this.services = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
    }


    /* *******************************************************/
    /* Instance methods **************************************/

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

        for (AService service : this.services) {

            Thread t = new Thread(service);
            t.start();
            t.setPriority(Thread.MAX_PRIORITY);
        }
    }


    /**
     * <p>Starts the router only.</p>
     */
    public void startRouter() {

        if(router != null) {

            Thread router = new Thread(this.router);
            router.start();
            router.setPriority(5);
        }
    }

    /**
     * <p>Adds service to the container of runnable services.</p>
     *
     * @param service   To be added
     */
    public void addService(AService service) {

        synchronized (this.services) {

            this.services.addIfAbsent(service);
        }
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
     * <p>Loads the data from configuration XML file and before that
     * it's validated using the given XSD schema.</p>
     *
     * @param configFilePath        XML file path of configuration
     * @param schemaFilePath        XSD file path of schema
     */
    public void load(String configFilePath, String schemaFilePath) {

        try {

            if (new XMLSchemaValidator(configFilePath, schemaFilePath).validate()) {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                dbFactory.setIgnoringComments(true);
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(configFilePath);
                doc.getDocumentElement().normalize();

                new ServerConfiguration(doc);
            }

        } catch (Exception e) {

            LOG.log(Level.SEVERE, "XML configuration file is not valid, " +
                    "does not exist or validation schema does not exist!\n" + e.getMessage());
        }
    }


    /* *******************************************************/
    /* Getters and setters ***********************************/


    /**
     * Getter for {@link CopyOnWriteArrayList<>}-formed {@code services} of
     * the instance of {@link Server} containing {@link AService} instances.
     *
     * @return the value of {@code services}
     * @see CopyOnWriteArrayList<>
     * @see Server
     */
    public CopyOnWriteArrayList<AService> getServices() {

        return services;
    }

    /**
     * <p>Setter for the {@code Router} formed
     * {@code router} variable.</p>
     *
     * @param router given Router value to
     *               be set to the variable
     * @see Router
     * @see Server
     */
    public void setRouter(Router router) {

        this.router = router;
    }


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of Server.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the Server class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static Server getInstance() {

        if (singletonInstance == null) {

            synchronized (Server.class) {

                singletonInstance = new Server();
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


    public static void main(String[] args) throws Exception {


        /*
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

        Server s = Server.getInstance();


        ClassLoader classLoader = Server.getInstance().getClass().getClassLoader();
        File file = new File(classLoader.getResource("stores/keyStore.jks").getFile());
        ClassLoader classLoader2 = Server.getInstance().getClass().getClassLoader();
        File file2 = new File(classLoader2.getResource("stores/trustStore.jts").getFile());


        Router router = new Router(
                50,
                50,
                9889,
                888,
                file.getAbsolutePath(),
                "changeit"
        );

        s.setRouter(router);

        s.startRouter();

        s.addService(new OrderManagement(
                "localhost",
                888,
                1010,
                50,
                file2.getAbsolutePath(), "changeit",
                file.getAbsolutePath(), "changeit"
        ));

        System.out.println("Starting...");

        Thread t = new Thread(s);
        t.start();*/

    }
}
