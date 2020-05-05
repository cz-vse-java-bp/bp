package cz.vse.java.handlers;


import cz.vse.java.connections.clientSide.C2RConnection;
import cz.vse.java.connections.serviceSide.S2RConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.UseToken;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.AGeneralService;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.Token;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UseTokenHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class UseTokenHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UseTokenHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UseTokenHandler(HandlerContainer container) {

        super(container);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection receiver of the message
     * @param message    received message
     * @return boolean interpretation if it was or wasn't handled.
     */
    @Override
    public boolean handle(IConnection connection, IMessage message) {

        if(message instanceof UseToken) {

            Token token         = (Token)        ((UseToken) message).getContent()[1];
            EServiceType type   = (EServiceType) ((UseToken) message).getContent()[0];

            if(connection instanceof C2RConnection) {

                LOG.log(Level.INFO, "Adding token for service type " + type.name());

                Client.getInstance().addToken(token, type);

            } else if(connection instanceof S2RConnection) {

                LOG.log(Level.INFO, "Adding token of " + token.getToken() + " to the clients connection manager.");
                AGeneralService gs = (AGeneralService) connection.getConnectionManager().getService();
                gs.getClients().addToken(token);

            } else {

                throw new UnsupportedOperationException("Not supported connection type!");
            }
            return true;
        }

        return false;
    }

    /**
     * <p>Clones the instance.</p>
     *
     * @param container to be set as default
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new UseTokenHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
