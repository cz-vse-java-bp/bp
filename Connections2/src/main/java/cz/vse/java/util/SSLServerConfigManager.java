package cz.vse.java.util;


import java.io.File;

/**************************************************************
 * <p>Class SSLServerConfigManager is Singleton by design pattern definition
 * with only one possible instance of this class.</p>
 *
 * <p>The class has <b>Thread-safe</b> getInstance() creation
 * mechanism implemented to be sure there is only one instance.</p>
 *
 * Written for project "Connections".
 * @author Vojtěch Pavlů
 * @version 29. 02. 2020
 *
 *
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class SSLServerConfigManager {

    /* *******************************************************/
    /* Instance variables ************************************/

    private String pathToKeyStore;
    private String pathToTrustStore;
    private String passwordToKeyStore;
    private String passwordToTrustStore;

    private String[] enabledCiphers;
    private String[] enabledProtocols;

    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile SSLServerConfigManager singletonInstance = null;



    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the SSLServerConfigManager class.</p>
     */
    private SSLServerConfigManager() {}

    /* *******************************************************/
    /* Instance methods **************************************/


    public void init(String[] enabledProtocols,
                     String[] enabledCiphers,
                     String pathToKeyStore,
                     String passwordToKeyStore,
                     String pathToTrustStore,
                     String passwordToTrustStore) {

        this.enabledProtocols = enabledProtocols;
        this.enabledCiphers = enabledCiphers;
        this.pathToKeyStore = pathToKeyStore;
        this.passwordToKeyStore = passwordToKeyStore;
        this.pathToTrustStore = pathToTrustStore;
        this.passwordToTrustStore = passwordToTrustStore;

        setSystemProperties();
    }


    public void init(String[] enabledProtocols,
                     String[] enabledCiphers,
                     String pathToKeyStore,
                     String passwordToKeyStore) {

        this.enabledProtocols = enabledProtocols;
        this.enabledCiphers = enabledCiphers;
        this.pathToKeyStore = pathToKeyStore;
        this.passwordToKeyStore = passwordToKeyStore;
        this.pathToTrustStore = pathToKeyStore;
        this.passwordToTrustStore = passwordToKeyStore;

        setSystemProperties();
    }


    public void init(String pathToKeyStore, String passwordToKeyStore) {

        this.enabledProtocols = new String[]{"TLSv1.3"};
        this.enabledCiphers = new String[]{"TLS_AES_128_GCM_SHA256", "TLS_AES_256_GCM_SHA384"};
        this.pathToKeyStore = pathToKeyStore;
        this.passwordToKeyStore = passwordToKeyStore;
        this.pathToTrustStore = pathToKeyStore;
        this.passwordToTrustStore = passwordToKeyStore;

        setSystemProperties();
    }


    public void init() {

        File keyStore = new File(SSLServerConfigManager.class.getClassLoader().getResource(
                "keyStore.jks").getFile());

        this.enabledProtocols = new String[]{"TLSv1.3"};
        this.enabledCiphers = new String[]{"TLS_AES_128_GCM_SHA256"};
        this.pathToKeyStore = keyStore.getAbsolutePath();
        this.passwordToKeyStore = "changeit";
        this.pathToTrustStore = pathToKeyStore;
        this.passwordToTrustStore = passwordToKeyStore;

        setSystemProperties();
    }


    private void setSystemProperties() {

        System.setProperty("javax.net.ssl.keyStore", pathToKeyStore);
        System.setProperty("javax.net.ssl.keyStorePassword", passwordToKeyStore);
        System.setProperty("javax.net.ssl.trustStore", pathToTrustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", passwordToTrustStore);
    }


    /* *******************************************************/
    /* Getters and setters ***********************************/

    /**
     * Getter for array of {@link String}s formed {@code enabledCiphers}
     * of the instance of {@link SSLServerConfigManager}
     *
     * @return the value of {@code enabledCiphers}
     * @see String
     * @see SSLServerConfigManager
     */
    public String[] getEnabledCiphers() {

        return enabledCiphers;
    }

    /**
     * Getter for array of {@link String}s formed {@code enabledProtocols}
     * of the instance of {@link SSLServerConfigManager}
     *
     * @return the value of {@code enabledProtocols}
     * @see String[]
     * @see SSLServerConfigManager
     */
    public String[] getEnabledProtocols() {

        return enabledProtocols;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code pathToKeyStore} variable.</p>
     *
     * @param pathToKeyStore given String value to
     *                       be set to the variable
     * @see String
     * @see SSLServerConfigManager
     */
    public void setPathToKeyStore(String pathToKeyStore) {

        this.pathToKeyStore = pathToKeyStore;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code pathToTrustStore} variable.</p>
     *
     * @param pathToTrustStore given String value to
     *                         be set to the variable
     * @see String
     * @see SSLServerConfigManager
     */
    public void setPathToTrustStore(String pathToTrustStore) {

        this.pathToTrustStore = pathToTrustStore;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code passwordToKeyStore} variable.</p>
     *
     * @param passwordToKeyStore given String value to
     *                           be set to the variable
     * @see String
     * @see SSLServerConfigManager
     */
    public void setPasswordToKeyStore(String passwordToKeyStore) {

        this.passwordToKeyStore = passwordToKeyStore;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code passwordToTrustStore} variable.</p>
     *
     * @param passwordToTrustStore given String value to
     *                             be set to the variable
     * @see String
     * @see SSLServerConfigManager
     */
    public void setPasswordToTrustStore(String passwordToTrustStore) {

        this.passwordToTrustStore = passwordToTrustStore;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code enabledCiphers} variable.</p>
     *
     * @param enabledCiphers given String value to
     *                       be set to the variable
     * @see String[]
     * @see SSLServerConfigManager
     */
    public void setEnabledCiphers(String[] enabledCiphers) {

        this.enabledCiphers = enabledCiphers;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code enabledProtocols} variable.</p>
     *
     * @param enabledProtocols given String value to
     *                         be set to the variable
     * @see String[]
     * @see SSLServerConfigManager
     */
    public void setEnabledProtocols(String[] enabledProtocols) {

        this.enabledProtocols = enabledProtocols;
    }


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of SSLServerConfigManager.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the SSLServerConfigManager class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static SSLServerConfigManager getInstance() {

        if (singletonInstance == null) {

            synchronized (SSLServerConfigManager.class) {

                singletonInstance = new SSLServerConfigManager();
            }
        }

        return singletonInstance;
    }
}
