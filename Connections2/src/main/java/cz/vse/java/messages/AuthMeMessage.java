package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.util.Token;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AuthMeMessage} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class AuthMeMessage extends AMessage {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AuthMeMessage class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final Long serilVersionUID = 34513445L;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AuthMeMessage(Token token) {

        super(token);
    }

    public AuthMeMessage() {

        super();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the AuthMeMessage instance.
     */
    @Override
    public String toString() {

        return "AuthMeMessage.toString() - NOT DEFINED YET!";
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
