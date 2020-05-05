package cz.vse.java.messages.utils;


import cz.vse.java.util.FingerPrint;
import cz.vse.java.util.Token;

import java.io.Serializable;

/**************************************************************
 * <p>The interface of IMessage is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.messages
 */
public interface IMessage extends Serializable {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Setter for the {@code FingerPrint} formed
     * {@code fingerPrint} variable.</p>
     *
     * @param fingerPrint given FingerPrint value to
     *                    be set to the variable
     * @see FingerPrint
     * @see AMessage
     */
    void setFingerPrint(FingerPrint fingerPrint);

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see AMessage
     */
    void setToken(Token token);


    /* *****************************************************************/
    /* Default methods *************************************************/


}
