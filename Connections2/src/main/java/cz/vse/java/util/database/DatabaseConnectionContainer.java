package cz.vse.java.util.database;


import java.util.HashMap;
import java.util.logging.Logger;


/**************************************************************
 * <p>Class DatabaseConnectionContainer is Singleton by design pattern definition
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
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class DatabaseConnectionContainer {

    /* *******************************************************/
    /* Instance variables ************************************/

    private HashMap<EDBUse, DBConnection> connections;

    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile DatabaseConnectionContainer singletonInstance = null;


    /**
     * Private {@link Logger} instance - Logger of the {@link DatabaseConnectionContainer} class
     */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the DatabaseConnectionContainer class.</p>
     */
    private DatabaseConnectionContainer() {

        this.connections = new HashMap<>();
    }



    /* *******************************************************/
    /* Instance methods **************************************/


    public void add(EDBUse use, DBConnection connection) {

        //LOG.log(Level.INFO, "Adding " + use.name() + " use of connection.");
        this.connections.putIfAbsent(use, connection);
    }


    public DBConnection get(EDBUse use) {

        return this.connections.get(use);
    }


    /* *******************************************************/
    /* Getters and setters ***********************************/





    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of DatabaseConnectionContainer.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the DatabaseConnectionContainer class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static DatabaseConnectionContainer getInstance() {

        if (singletonInstance == null) {

            synchronized (DatabaseConnectionContainer.class) {

                singletonInstance = new DatabaseConnectionContainer();
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

    /* *******************************************************/
    /* Main method *******************************************/

    /**
     * The main method of the class of DatabaseConnectionContainer.
     *
     */
    public static void main(String[] args){
       
        System.err.println(">>> QuickTest: DatabaseConnectionContainer class");
        System.err.println(">>> Creating DatabaseConnectionContainer instance...");

        DatabaseConnectionContainer.getInstance().add(EDBUse.USER_AUTHENTICATION, new DBConnection(
                "jdbc:h2:tcp://localhost/~/test", "sa", ""
        ));
        
        //code
        
        System.err.println(">>> Creation successfull...");
    }
}
