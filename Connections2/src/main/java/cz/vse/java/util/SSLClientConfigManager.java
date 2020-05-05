package cz.vse.java.util;


import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

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
public class SSLClientConfigManager {

    /* *******************************************************/
    /* Instance variables ************************************/

    private String pathToKeyStore = "";
    private String pathToTrustStore = "";
    private String passwordToKeyStore = "";
    private String passwordToTrustStore = "";

    private String[] enabledProtocols = new String[]{"TLSv1.3"};
    private String[] enabledCiphers = new String[]{"TLS_AES_128_GCM_SHA256", "TLS_AES_256_GCM_SHA384"};

    private boolean wasSet = false;

    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile SSLClientConfigManager singletonInstance = null;



    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the {@link SSLClientConfigManager} class.</p>
     */
    private SSLClientConfigManager() {}

    /* *******************************************************/
    /* Instance methods **************************************/


    public void init(String pathToTrustStore, String passwordToTrustStore) {

        this.pathToKeyStore = pathToTrustStore;
        this.passwordToKeyStore = passwordToTrustStore;
        this.pathToTrustStore = pathToTrustStore;
        this.passwordToTrustStore = passwordToTrustStore;

        setSystemProperties();
    }


    public void init() {

        File trustStore = new File(SSLClientConfigManager.class.getClassLoader().getResource(
                "store/trustStore.jts").getFile());

        this.pathToTrustStore = trustStore.getAbsolutePath();
        this.passwordToTrustStore = "changeit";
        this.pathToKeyStore = pathToTrustStore;
        this.passwordToKeyStore = passwordToTrustStore;

        setSystemProperties();
    }


    private void setSystemProperties() {

        if(!wasSet) {

            System.setProperty("javax.net.ssl.keyStore", pathToKeyStore);
            System.setProperty("javax.net.ssl.keyStorePassword", passwordToKeyStore);
            System.setProperty("javax.net.ssl.trustStore", pathToTrustStore);
            System.setProperty("javax.net.ssl.trustStorePassword", passwordToTrustStore);

            wasSet = true;
        }
    }


    /* *******************************************************/
    /* Getters and setters ***********************************/

    /**
     * Getter for {@link String} formed {@code enabledCipher}
     * of the instance of {@link SSLClientConfigManager}
     *
     * @return the value of {@code enabledCipher}
     * @see String
     * @see SSLClientConfigManager
     */
    public String[] getEnabledCiphers() {

        return this.enabledCiphers;
    }

    /**
     * Getter for {@link String} formed {@code enabledProtocol}
     * of the instance of {@link SSLClientConfigManager}
     *
     * @return the value of {@code enabledProtocol}
     * @see String
     * @see SSLClientConfigManager
     */
    public String[] getEnabledProtocols() {

        return this.enabledProtocols;
    }


    public SSLSocketFactory getSSLSocketFactory() {

        SSLSocketFactory socketFactory = null;

        try {

            SSLContext context;
            KeyManagerFactory keyManagerFactory;
            KeyStore keyStore;
            char[] passwordToKeyStore = this.passwordToKeyStore.toCharArray();

            context = SSLContext.getInstance(enabledProtocols[0]);
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyStore = KeyStore.getInstance("JKS");

            keyStore.load(new FileInputStream(pathToKeyStore), passwordToKeyStore);

            keyManagerFactory.init(keyStore, passwordToKeyStore);
            context.init(keyManagerFactory.getKeyManagers(), null, null);

            socketFactory = context.getSocketFactory();

        } catch (
                IOException |
                UnrecoverableKeyException |
                KeyStoreException |
                CertificateException |
                KeyManagementException |
                NoSuchAlgorithmException e
        ) {
            e.printStackTrace();
        }

        return socketFactory;
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
    public static SSLClientConfigManager getInstance() {

        if (singletonInstance == null) {

            synchronized (SSLClientConfigManager.class) {

                singletonInstance = new SSLClientConfigManager();
            }
        }

        return singletonInstance;
    }
}
