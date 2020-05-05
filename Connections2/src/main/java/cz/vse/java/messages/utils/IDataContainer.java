package cz.vse.java.messages.utils;


/**************************************************************
 * <p>The interface of IDataContainer is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.messages
 */
public interface IDataContainer extends IMessage {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return  the content the message contains.
     */
    Object getContent();

    /* *****************************************************************/
    /* Default methods *************************************************/


}
