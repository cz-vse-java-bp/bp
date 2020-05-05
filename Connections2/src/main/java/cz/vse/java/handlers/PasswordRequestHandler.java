package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.PasswordContainerMessage;
import cz.vse.java.messages.PasswordRequest;
import cz.vse.java.utils.cryptography.hashing.EHashAlgorithm;
import cz.vse.java.util.userData.UserProperties;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PasswordRequestHandler} is used to abstractly define
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
public class PasswordRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PasswordRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final EHashAlgorithm alg = EHashAlgorithm.SHA512;

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PasswordRequestHandler(HandlerContainer container) {

        super(container);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the PasswordRequestHandler instance.
     */
    @Override
    public String toString() {

        return "PasswordRequestHandler.toString() - NOT DEFINED YET!";
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

        if(message instanceof PasswordRequest) {

            int numOfHashes = ((PasswordRequest) message).getNumOfHashes();
            String salt = ((PasswordRequest) message).getSalt();
            String password = UserProperties.getInstance().getHashedPassword(numOfHashes, salt);

            connection.send(new PasswordContainerMessage(password));

            return true;
        }

        return false;
    }


    /**
     * <p>Clones the instance.</p>
     *
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container){

        return new PasswordRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
