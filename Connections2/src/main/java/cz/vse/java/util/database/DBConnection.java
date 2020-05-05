package cz.vse.java.util.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*********************************************************************
 * <p>The class of {@code DBConnection} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections".</i>
 * @author Vojtěch Pavlů
 * @version 24. 03. 2020
 *
 *
 * @see cz.vse.java.utils
 */
public class DBConnection {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String url = "";
    private String username = "";
    private String password = "";

    private Connection connection;

    private boolean stable = false;
    private boolean created = false;


    /* *****************************************************************/
    /* Static variables ************************************************/

    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static int ATTEMPTS = 100;

    /* *****************************************************************/
    /* Constructors ****************************************************/


    public DBConnection(String url, String username, String password) {

        this.url = url;
        this.username = username;
        this.password = password;

        this.start();

        stable = true;
        created = true;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    public void close() {

        if(created) {
            try {

                this.connection.close();

            } catch (SQLException e) {

                LOG.log(Level.SEVERE, "Cannot close the connection to DB.");
            }
        }
    }


    public boolean reset() {

        LOG.log(Level.FINE, "Resetting the DB connection.");

        connection = null;

        return start();
    }


    public boolean start() {

        for(int i = 0; i < ATTEMPTS; i++) {

            try {
                this.connection = DriverManager.getConnection(url, username, password);
                LOG.log(Level.FINE, "Connection with DB successfully created and is stable.");
                stable = true;
                created = true;
                break;

            } catch (SQLException e) {
                this.stable = false;
                this.created = false;
                LOG.log(Level.SEVERE, "Connection with DB cannot be created! Attempt n. "
                        + (i+1) + ": " + e.getMessage());
            }
        }

        return stable;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Connection} formed {@code connection}
     * of the instance of {@link DBConnection}
     *
     * @return the value of {@code connection}
     * @see Connection
     * @see DBConnection
     */
    public Connection getConnection() {

        return connection;
    }

    /**
     * Getter for {@link boolean} formed {@code stable}
     * of the instance of {@link DBConnection}
     *
     * @return the value of {@code stable}
     * @see boolean
     * @see DBConnection
     */
    public boolean isStable() {

        return stable;
    }

    /**
     * Getter for {@link boolean} formed {@code created}
     * of the instance of {@link DBConnection}
     *
     * @return the value of {@code created}
     * @see boolean
     * @see DBConnection
     */
    public boolean isCreated() {

        return created;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/


    /**
     * <p>Setter for the integer-formed {@code ATTEMPTS} variable.</p>
     *
     * <p>This variable is responsible for restricting
     * the number of cycles of the DB connection creation.
     * When the number of tries gets to this value, it ends
     * with the attempts and stops.</p>
     *
     * @param ATTEMPTS given $field.typeName value to
     *                 be set to the variable
     * @see DBConnection
     * @see DBConnection#reset()
     */
    public static void setATTEMPTS(int ATTEMPTS) {

        DBConnection.ATTEMPTS = ATTEMPTS;
    }


    /**
     * <p>Setter for the {@code String} formed
     * {@code url} variable.</p>
     *
     * @param url given String value to
     *            be set to the variable
     * @see String
     * @see DBConnection
     */
    public void setUrl(String url) {

        this.url = url;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code username} variable.</p>
     *
     * @param username given String value to
     *                 be set to the variable
     * @see String
     * @see DBConnection
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code password} variable.</p>
     *
     * @param password given String value to
     *                 be set to the variable
     * @see String
     * @see DBConnection
     */
    public void setPassword(String password) {

        this.password = password;
    }
}
