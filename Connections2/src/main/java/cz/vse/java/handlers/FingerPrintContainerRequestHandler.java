package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.FingerPrintContainerMessage;
import cz.vse.java.messages.FingerPrintContainerRequest;
import cz.vse.java.messages.utils.IMessage;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code FingerPrintContainerRequestHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class FingerPrintContainerRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link FingerPrintContainerRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public FingerPrintContainerRequestHandler(HandlerContainer container) {

        super(container);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection receiver of the message
     * @param message    received message
     * @return boolean interpretation if it was or wasn't handled.
     */
    @Override
    public boolean handle(IConnection connection, IMessage message) {

        if(message instanceof FingerPrintContainerRequest) {

            connection.send(new FingerPrintContainerMessage());
            return true;
        }
        return false;
    }


    /**
     * <p>Clones the instance.</p>
     *
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container){

        return new FingerPrintContainerRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
