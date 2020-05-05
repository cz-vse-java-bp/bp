package cz.vse.java.util.userData;


import cz.vse.java.utils.cryptography.hashing.EHashAlgorithm;
import cz.vse.java.utils.cryptography.hashing.Hasher;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**************************************************************
 * <p>Class UserProperties is Singleton by design pattern definition
 * with only one possible instance of this class.</p>
 *
 * <p>The class has <b>Thread-safe</b> getInstance() creation
 * mechanism implemented to be sure there is only one instance.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.utils.userData
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class UserProperties {

    /* *******************************************************/
    /* Instance variables ************************************/

    private String userName;
    private String password;
    private EHashAlgorithm algorithm = EHashAlgorithm.SHA512;

    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile UserProperties singletonInstance = null;


    /**
     * Private {@link Logger} instance - Logger of the {@link UserProperties} class
     */
    private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the UserProperties class.</p>
     */
    private UserProperties() {


    }



    /* *******************************************************/
    /* Instance methods **************************************/


    public String getHashedPassword(int numOfHashes, String salt) {

        Hasher hasher = new Hasher(this.algorithm);
        String result = this.password;

        LOG.log(Level.INFO, "Hashing " + numOfHashes + " times the password using '" + salt + "' as salt");

        for (int i = 0; i < numOfHashes; i++) {

            try {

                result = hasher.generateHashWithSalt(result, salt);

            } catch (NoSuchAlgorithmException e) {

                LOG.log(Level.SEVERE, "Algorithm not found!");
            }
        }
        return result;
    }

    /* *******************************************************/
    /* Getters and setters ***********************************/

    /**
     * Getter for {@link String} formed {@code userName}
     * of the instance of {@link UserProperties}
     *
     * @return the value of {@code userName}
     * @see String
     * @see UserProperties
     */
    public String getUserName() {
        return userName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code userName} variable.</p>
     *
     * @param userName given String value to
     *                 be set to the variable
     * @see String
     * @see UserProperties
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for {@link String} formed {@code password}
     * of the instance of {@link UserProperties}
     *
     * @return the value of {@code password}
     * @see String
     * @see UserProperties
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for {@link EHashAlgorithm} formed {@code algorithm}
     * of the instance of {@link UserProperties}
     *
     * @return the value of {@code algorithm}
     * @see EHashAlgorithm
     * @see UserProperties
     */
    public EHashAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * <p>Setter for the {@code EHashAlgorithm} formed
     * {@code algorithm} variable.</p>
     *
     * @param algorithm given EHashAlgorithm value to
     *                  be set to the variable
     * @see EHashAlgorithm
     * @see UserProperties
     */
    public void setAlgorithm(EHashAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code password} variable.</p>
     *
     * @param password given String value to
     *                 be set to the variable
     * @see String
     * @see UserProperties
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of UserProperties.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the UserProperties class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static UserProperties getInstance() {

        if (singletonInstance == null) {

            synchronized (UserProperties.class) {

                singletonInstance = new UserProperties();
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
}
