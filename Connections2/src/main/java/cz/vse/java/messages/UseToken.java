package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.Token;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UseToken} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class UseToken extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Token token;
    private EServiceType type;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UseToken class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UseToken(Token token, EServiceType type) {

        this.token = token;
        this.type = type;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * <p>The content is formed in the array of {@link Object}
     * instances. In the index of 0 is the {@link EServiceType},
     * in the index of 1 is saved the {@link Token} instance to use.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.type, this.token};
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
