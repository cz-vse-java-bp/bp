package cz.vse.java.services.serverSide;


import cz.vse.java.connections.utils.management.RouterClientsManagement;
import cz.vse.java.connections.utils.management.ServiceClientsManagement;
import cz.vse.java.connections.utils.management.ServiceRouterManagement;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AGeneralService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.services
 */
public abstract class AGeneralService extends AService implements IService, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    protected ServiceRouterManagement router;
    protected ServiceClientsManagement clients;

    private boolean autoClosingClients;

    private String routerIP;
    private int routerPort;
    private String pathToTrustStore;
    private String passwordToTrustStore;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AGeneralService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static int MAX_RECONNECTION_ATTEMPTS = 30;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AGeneralService(
                    EServiceType type,
                    String routerIP,
                    int routerPort,
                    int clientsPort,
                    int maxClients,
                    boolean autoClosingClients,
                    String pathToTrustStore,
                    String passwordToTrustStore,
                    String pathToKeyStore,
                    String passwordToKeyStore) {

        super(type);

        this.autoClosingClients = autoClosingClients;
        this.routerIP = routerIP;
        this.routerPort = routerPort;
        this.pathToTrustStore = pathToTrustStore;
        this.passwordToTrustStore = passwordToTrustStore;

        this.clients = new ServiceClientsManagement(
                maxClients,
                clientsPort,
                this,
                pathToKeyStore,
                passwordToKeyStore,
                autoClosingClients
        );

        if(!this.routerInit()) {

            this.update();
        }
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    public boolean routerInit() {

        try {
            this.router = new ServiceRouterManagement(
                    this,
                    this.routerIP,
                    this.routerPort,
                    this.pathToTrustStore,
                    this.passwordToTrustStore);

            return true;

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Problem while generating router: " + e.getMessage());
            return false;
        }
    }

    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     */
    @Override
    public void update() {

        boolean indicator = false;

        for (int i = 0; i < MAX_RECONNECTION_ATTEMPTS; i++) {

            if(this.routerInit()) {

                indicator = true;
                break;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    LOG.log(Level.SEVERE, "Problem while waiting: " + e.getMessage());
                }
            }
        }

        if(!indicator) {

            this.close();
            LOG.log(Level.SEVERE, "Closing service");
        }
    }


    public void close() {

        if(this.clients != null) {

            this.clients.close();
        }
        if(this.router != null) {

            this.router.close();
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link ServiceRouterManagement} formed {@code router}
     * of the instance of {@link AGeneralService}
     *
     * @return the value of {@code router}
     * @see ServiceRouterManagement
     * @see AGeneralService
     */
    public ServiceRouterManagement getRouter() {

        return router;
    }


    /**
     * <p>Returns the port the clients can connect to the service at.</p>
     *
     * @return integer representation of port
     */
    @Override
    public int getClientsPort() {

        return this.clients.getPort();
    }

    /**
     * <p>Returns the IP address this service is working at.</p>
     *
     * @return {@link String} representation of the address
     * the clients can connect with this service at.
     */
    @Override
    public String getClientsIP() {

        return this.clients.getIp();
    }

    /**
     * Getter for {@link RouterClientsManagement} formed {@code clients}
     * of the instance of {@link AGeneralService}
     *
     * @return the value of {@code clients}
     * @see RouterClientsManagement
     * @see AGeneralService
     */
    public ServiceClientsManagement getClients() {

        return clients;
    }



    /* *****************************************************************/
    /* Setters *********************************************************/



}
