package cz.vse.java.connections.utils;


import cz.vse.java.connections.utils.management.AConnectionManager;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.util.Token;

/**************************************************************
 * <p>The interface of IConnection is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.connections
 */
public interface IConnection extends Runnable {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Closes the connection channel.</p>
     */
    void close();


    /**
     * <p>Provides the listening operation.</p>
     */
    void listen();

    /**
     * <p>Sends the message to the channel.</p>
     * @param message to be sent
     */
    void send(IMessage message);


    /**
     * <p>Returns the token used for the communication.</p>
     *
     * @return  {@link Token} used for the communication.
     */
    Token getToken();


    /**
     * <p>Sets token to the {@link IConnection} instance.</p>
     *
     * @param token to be set as default.
     */
    void setToken(Token token);


    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler   handler to be added.
     */
    void addMessageHandler(IHandler handler);


    /**
     * <p>Gets reference to the message handler container.</p>
     *
     * @return  the given message handler container
     */
    HandlerContainer getMessageHandlerContainer();


    /**
     * <p>Sets the authentication result</p>
     *
     * @param result    of the authentication process
     *                  on the other side of the connection
     */
    void setAmIAuthenticated(boolean result);


    /**
     * <p>gets the authentication result</p>
     *
     * @return result of the authentication process
     *         on the other side of the connection
     */
    boolean getAmIAuthenticated();


    /**
     * <p>Returns the {@link AConnectionManager} instance this
     * connection belongs to.</p>
     *
     * @return  the connection manager
     */
    AConnectionManager getConnectionManager();
}
