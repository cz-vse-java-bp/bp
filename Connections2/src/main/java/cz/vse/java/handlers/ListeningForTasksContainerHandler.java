package cz.vse.java.handlers;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ListeningForTasksContainer;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.TaskManagement;
import cz.vse.java.util.userTaskAssignment.TaskSolver;
import cz.vse.java.util.userTaskAssignment.TaskSolverContainer;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ListeningForTasksContainerHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class ListeningForTasksContainerHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ListeningForTasksContainerHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ListeningForTasksContainerHandler(HandlerContainer container) {

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

        if(message instanceof ListeningForTasksContainer) {

            if(connection instanceof S2CConnection) {

                IService service = connection.getConnectionManager().getService();

                if(service instanceof TaskManagement) {

                    String userName = (String) ((ListeningForTasksContainer) message).getContent()[0];
                    boolean listening = (boolean) ((ListeningForTasksContainer) message).getContent()[1];

                    TaskManagement tm = (TaskManagement) service;

                    TaskSolverContainer tsc = tm.getTaskSolverContainer();
                    TaskSolver ts = tsc.getTaskSolver(userName);

                    if(ts == null) {

                        LOG.log(Level.SEVERE, "Creating new TaskSolver instance");

                        tsc.add(connection, userName);
                        ts = tsc.getTaskSolver(userName);
                    }

                    LOG.log(Level.INFO, "Setting listening to new tasks to: " + listening);

                    ts.setListening(listening);

                    return true;
                }
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

        return new ListeningForTasksContainerHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
