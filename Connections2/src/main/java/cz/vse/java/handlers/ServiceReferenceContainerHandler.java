package cz.vse.java.handlers;


import cz.vse.java.connections.clientSide.C2RConnection;
import cz.vse.java.connections.routerSide.R2SConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ServiceReferenceContainer;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.references.ServiceReference;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceReferenceContainerHandler} is used to abstractly define
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
public class ServiceReferenceContainerHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceReferenceContainerHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceReferenceContainerHandler(HandlerContainer container) {

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

        if(message instanceof ServiceReferenceContainer) {

            ServiceReference sr = ((ServiceReferenceContainer) message).getContent();

            if(connection instanceof R2SConnection) {

                ((R2SConnection) connection).setSrForClients(sr);

            } else if(connection instanceof C2RConnection) {

                Client.getInstance().addServiceReference(sr);
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

        return new ServiceReferenceContainerHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
