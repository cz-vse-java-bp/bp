package cz.vse.java.handlers;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AddTaskMessage;
import cz.vse.java.messages.TaskStateChange;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.TaskManagement;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.service.TaskService;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskStateChangeHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 25. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class TaskStateChangeHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskStateChangeHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskStateChangeHandler(HandlerContainer container) {

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

        if(message instanceof TaskStateChange) {

            if(connection instanceof S2CConnection) {

                IService s = connection.getConnectionManager().getService();

                if(s.getServiceType().equals(EServiceType.TASK_SERVICE)) {

                    TaskManagement tm = (TaskManagement) s;

                    String username = (String) ((TaskStateChange) message).getContent()[0];
                    Long id = (Long) ((TaskStateChange) message).getContent()[1];
                    ETaskState state = (ETaskState) ((TaskStateChange) message).getContent()[2];

                    List<Task> tasks = tm.getTasks(username);

                    Task changed = null;

                    for (Task t : tasks) {

                        if(t.getId().equals(id)) {

                            try {

                                changed = t;

                                t.setState(state);

                                LOG.log(Level.SEVERE, "Updating the task state with ID like "
                                        + id + " to the " + state.getDesc() + ", task state is now: "
                                        + t.getState().getDesc());

                                new TaskService().update(t);

                            } catch (SQLException e) {

                                LOG.log(Level.SEVERE, "Connection with DB failed! " + e.getMessage());
                            }

                            break;
                        }
                    }

                    if(changed != null) {

                        tm.getTaskSolverContainer().getTaskSolver(username).remove(changed.getId());
                        tm.getTaskSolverContainer().getTaskSolver(username).add(changed);
                    }

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

        return new TaskStateChangeHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of TaskStateChangeHandler.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: TaskStateChangeHandler class");
        System.err.println(">>> Creating TaskStateChangeHandler instance...");
        TaskStateChangeHandler instance = new TaskStateChangeHandler();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
