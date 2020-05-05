package cz.vse.java.connections.utils;


import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.Token;

/**************************************************************
 * <p>The interface of ICSConnection is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.connections
 */
public interface ICSConnection extends IConnection {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Returns {@link EServiceType} of the service this connection
     * is communicating with.</p>
     *
     * @return  {@link EServiceType}
     */
    EServiceType getOtherSideServiceType();


    /**
     * <p>Returns the token used for the communication.</p>
     *
     * @return  token used for communication.
     */
    Token getToken();


    /**
     * <p>Sets the authentication result.</p>
     *
     * @param result    result of the authentication
     *                  process.
     */
    void setAmIAuthenticated(boolean result);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
