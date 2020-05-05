package cz.vse.java.messages.utils;


import cz.vse.java.connections.utils.IConnection;

/**************************************************************
 * <p>The interface of ICommand is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.messages.utils
 */
public interface ICommand extends IMessage {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Method defining the execution procedure.</p>
     *
     * @param connection    receiver and executor of the message.
     */
    void execute(IConnection connection);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
