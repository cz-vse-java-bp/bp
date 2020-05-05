package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.Token;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UserNameContainerMessage} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class UserNameContainerMessage extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String userName;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UserNameContainerMessage class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UserNameContainerMessage(Token token, String userName) {

        super(token);
        this.userName = userName;
    }

    public UserNameContainerMessage(String userName) {

        this.userName = userName;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the UserNameContainerMessage instance.
     */
    @Override
    public String toString() {

        return "UserNameContainerMessage.toString() - NOT DEFINED YET!";
    }

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public String getContent() {

        return this.userName;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
