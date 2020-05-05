package cz.vse.java.handlers;


import cz.vse.java.connections.clientSide.C2RConnection;
import cz.vse.java.connections.clientSide.C2SConnection;
import cz.vse.java.connections.routerSide.R2SConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.problemSolvers.utils.IProblemSolver;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ServiceTypeContainer;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceTypeContainerHandler} is used to abstractly define
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
public class ServiceTypeContainerHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceTypeContainerHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceTypeContainerHandler(HandlerContainer container) {

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

        if(message instanceof ServiceTypeContainer) {

            EServiceType serviceType = ((ServiceTypeContainer) message).getContent();

            if(connection instanceof R2SConnection) {

                ((R2SConnection) connection).setServiceType(serviceType);

            } else if(connection instanceof C2RConnection) {

                LOG.log(Level.INFO, "Got Service type " + serviceType);

            } else if(connection instanceof C2SConnection) {

                EServiceType saved = ((C2SConnection) connection).getOtherSideServiceType();

                if(!saved.equals(serviceType)) {

                    IProblemSolver solver = (IProblemSolver) connection;
                    solver.solve(connection, EErrorType.WRONG_SERVICE_TYPE);
                }
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

        return new ServiceTypeContainerHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
