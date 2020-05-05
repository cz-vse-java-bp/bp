package cz.vse.java.connections.utils.management;


import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.services.serverSide.AService;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AConnectionManager} is used to abstractly define
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
public abstract class AConnectionManager {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    protected AService service;
    protected HandlerContainer handlers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AConnectionManager class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AConnectionManager(AService service) {

        this.service = service;
        this.handlers = new HandlerContainer();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link AService} formed {@code service}
     * of the instance of {@link AConnectionManager}
     *
     * @return the value of {@code service}
     * @see AService
     * @see AConnectionManager
     */
    public AService getService() {

        return service;
    }

    /**
     * Getter for {@link HandlerContainer} formed {@code handlers}
     * of the instance of {@link AConnectionManager}
     *
     * @return the value of {@code handlers}
     * @see HandlerContainer
     * @see AConnectionManager
     */
    public HandlerContainer getHandlers() {

        return handlers;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/



}
