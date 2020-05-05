package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ErrorMessage} is used to abstractly define
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
public class ErrorMessage extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EErrorType type;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ErrorMessage class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ErrorMessage(EErrorType type) {

        this.type = type;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public EErrorType getContent() {

        return type;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
