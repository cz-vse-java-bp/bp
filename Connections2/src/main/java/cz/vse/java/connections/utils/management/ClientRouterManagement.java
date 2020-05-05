package cz.vse.java.connections.utils.management;


import cz.vse.java.connections.clientSide.C2RConnection;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.AService;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ClientRouterManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.management
 */
public class ClientRouterManagement extends AConnectionManager implements Runnable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private AConnectionWithRouter router;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ClientRouterManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ClientRouterManagement(
                    AService service,
                    String routerIP,
                    int routerPort,
                    String pathToTrustStore,
                    String passwordToTrustStore) {

        super(service);

        this.router = new C2RConnection(routerIP, routerPort, pathToTrustStore, passwordToTrustStore, this);
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

        new Thread(router).start();
    }


    /**
     * <p>Sends the given message to the router.</p>
     *
     * @param message   to be sent.
     */
    public void send(IMessage message) {

        this.router.send(message);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link AConnectionWithRouter} formed {@code router}
     * of the instance of {@link ClientRouterManagement}
     *
     * @return the value of {@code router}
     * @see AConnectionWithRouter
     * @see ClientRouterManagement
     */
    public AConnectionWithRouter getRouter() {

        return router;
    }


    /**
     * <p>Returns the authentication state</p>
     *
     * @return  boolean interpretation of if this
     *          connection is authenticated.
     */
    public boolean getAuthenticationState() {

        return this.router.getAmIAuthenticated();
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
