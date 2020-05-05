package cz.vse.java.messages;


import cz.vse.java.connections.utils.ICSConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.ICommand;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AuthenticationResultContainer} is used to abstractly define
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
public class AuthenticationResultContainer extends AMessage implements ICommand {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private boolean result;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AuthenticationResultContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AuthenticationResultContainer(boolean result) {

        this.result = result;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method defining the execution procedure.</p>
     *
     * @param connection receiver and executor of the message.
     */
    @Override
    public void execute(IConnection connection) {

        connection.setAmIAuthenticated(this.result);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
