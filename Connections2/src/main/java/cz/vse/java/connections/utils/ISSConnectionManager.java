package cz.vse.java.connections.utils;


import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.util.Token;

/**************************************************************
 * <p>The interface of ISSConnectionManager is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.connections
 */
public interface ISSConnectionManager {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Builds the {@link ServiceReference} instance.</p>
     *
     * @return  the service reference for creating the connection usage
     */
    ServiceReference getServiceReference();



    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler handler to be added.
     */
    void addMessageHandler(IHandler handler);


    /**
     * <p>Validates the token against the given and saved ones.</p>
     *
     * @param connection    to be set to use the given token only if
     *                      it corresponds and is valid.
     * @param token         to be set to the connection, if it is valid.
     *
     * @return              the result of validity test
     */
    boolean validateToken(IConnection connection, Token token);


    /**
     * <p>Adds the token to the {@link cz.vse.java.authentication.TokenContainer}
     * for future token validation.</p>
     *
     * @param token     token
     */
    void addToken(Token token);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
