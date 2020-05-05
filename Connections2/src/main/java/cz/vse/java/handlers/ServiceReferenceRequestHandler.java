package cz.vse.java.handlers;


import cz.vse.java.connections.routerSide.R2CConnection;
import cz.vse.java.connections.routerSide.R2SConnection;
import cz.vse.java.connections.serviceSide.S2RConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.ServiceReferenceContainer;
import cz.vse.java.messages.ServiceReferenceRequest;
import cz.vse.java.messages.UseToken;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.Router;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.util.Token;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceReferenceRequestHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 18. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class ServiceReferenceRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceReferenceRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceReferenceRequestHandler(HandlerContainer container) {

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

        if(message instanceof ServiceReferenceRequest) {

            if (connection instanceof R2CConnection) {

                EServiceType type = ((ServiceReferenceRequest) message).getContent();

                if(type != null) {

                    Router router = (Router) connection.getConnectionManager().getService();
                    ServiceReference sr = router.getServices().getServiceReference(type);

                    if(sr != null) {

                        R2SConnection r2s = router.getServices().getServiceConnection(type);

                        if(r2s != null) {

                            connection.send(new ServiceReferenceContainer(sr));

                            Token token = new Token();

                            r2s.send(new UseToken(token, type));
                            connection.send(new UseToken(token, type));
                        }

                    } else {

                        connection.send(new ErrorMessage(EErrorType.SERVICE_REFERENCE_NOT_FOUND));
                    }
                    return true;

                } else {

                    LOG.log(Level.SEVERE, "Given EServiceType in the message is null!");
                    connection.send(new ErrorMessage(EErrorType.NULL_MESSAGE_CONTENT));
                }

            } else if (connection instanceof S2RConnection) {

                ServiceReference sr = ((S2RConnection) connection).getServiceReference();

                LOG.log(Level.SEVERE, "Sending new service reference: " + sr.toString());

                connection.send(new ServiceReferenceContainer(sr));

                return true;

            } else {

                LOG.log(Level.SEVERE, "Non supported type of connection: '"
                        + connection.getClass().getName() + "'." +
                        " Required: '" + R2CConnection.class.getName() + "' or '"
                        + S2RConnection.class.getName() + "'");
            }
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

        return new ServiceReferenceRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
