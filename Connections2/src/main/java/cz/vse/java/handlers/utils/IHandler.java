package cz.vse.java.handlers.utils;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.IMessage;

/**************************************************************
 * <p>The interface of IHandler is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.handlers
 */
public interface IHandler extends Cloneable {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection    receiver of the message
     * @param message       received message
     *
     * @return              boolean interpretation if it was or wasn't handled.
     */
    boolean handle(IConnection connection, IMessage message);


    /**
     * <p>Sets the container to the handler.</p>
     *
     * @param container {@link HandlerContainer} instance
     *                  to be set as default for the handler.
     */
    void setContainer(HandlerContainer container);


    /**
     * <p>Clones the instance.</p>
     *
     * @param container to be set as default
     *
     * @return  cloned handler
     */
    IHandler copy(HandlerContainer container);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
