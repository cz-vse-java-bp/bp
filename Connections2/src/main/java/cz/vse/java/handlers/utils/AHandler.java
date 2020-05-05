package cz.vse.java.handlers.utils;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.IMessage;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public abstract class AHandler implements IHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private HandlerContainer container;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AHandler(HandlerContainer container) {

        this.container = container;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link HandlerContainer} formed {@code container}
     * of the instance of {@link AHandler}
     *
     * @return the value of {@code container}
     * @see HandlerContainer
     * @see AHandler
     */
    public HandlerContainer getContainer() {

        return container;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code HandlerContainer} formed
     * {@code container} variable.</p>
     *
     * @param container given HandlerContainer value to
     *                  be set to the variable
     * @see HandlerContainer
     * @see AHandler
     */
    public void setContainer(HandlerContainer container) {

        this.container = container;
    }

    /* *****************************************************************/
    /* Main method *****************************************************/


}
