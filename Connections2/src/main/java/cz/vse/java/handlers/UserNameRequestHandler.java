package cz.vse.java.handlers;


import cz.vse.java.connections.utils.ICSConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.UserNameContainerMessage;
import cz.vse.java.messages.UserNameRequest;
import cz.vse.java.util.userData.UserProperties;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UserNameRequestHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class UserNameRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UserNameRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UserNameRequestHandler(HandlerContainer container) {

        super(container);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the UserNameRequestHandler instance.
     */
    @Override
    public String toString() {

        return this.getClass().getName();
    }

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection receiver of the message
     * @param message    received message
     * @return boolean interpretation if it was or wasn't handled.
     */
    @Override
    public boolean handle(IConnection connection, IMessage message) {

        if(message instanceof UserNameRequest) {

            String username = UserProperties.getInstance().getUserName();

            if(connection instanceof ICSConnection) {

                connection.send(new UserNameContainerMessage(username));
                return true;
            } else {

                LOG.log(Level.SEVERE, "Not supported connection.");
            }
        }
        return false;
    }


    /**
     * <p>Clones the instance.</p>
     *
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new UserNameRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
