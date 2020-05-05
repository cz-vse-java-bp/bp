package cz.vse.java.connections.utils.management;


import cz.vse.java.connections.serviceSide.S2RConnection;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.services.serverSide.AService;

import java.io.IOException;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceRouterManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.management
 */
public class ServiceRouterManagement extends AConnectionManager implements Runnable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private AConnectionWithRouter router;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceRouterManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceRouterManagement(AService service,
                                   String routerIP,
                                   int routerPort,
                                   String pathToTrustStore,
                                   String passwordToTrustStore) throws IOException {

        super(service);

        this.router = new S2RConnection(
                routerIP,
                routerPort,
                this,
                pathToTrustStore,
                passwordToTrustStore
        );


        for (IHandler handler : super.getHandlers().getHandlers()) {

            router.addMessageHandler(handler);
        }
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
     * Getter for {@link HandlerContainer} formed {@code handlers}
     * of the instance of {@link AConnectionManager}
     *
     * @return the value of {@code handlers}
     * @see HandlerContainer
     * @see AConnectionManager
     */
    @Override
    public HandlerContainer getHandlers() {

        return this.router.getMessageHandlerContainer();
    }


    /**
     * <p>Adds message handler to the router connection.</p>
     *
     * @param handler   handler to be added to {@link HandlerContainer}
     *                  instance by copying (cloning) the given one.
     */
    public void addMessageHandler(IHandler handler) {

        this.getHandlers().add(handler);
    }

    public void close() {

        this.router.close();
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/




}
