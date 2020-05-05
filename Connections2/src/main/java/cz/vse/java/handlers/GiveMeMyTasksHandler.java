package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AllTasksContainer;
import cz.vse.java.messages.ClearYourTasks;
import cz.vse.java.messages.GiveMeMyTasks;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.TaskManagement;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.service.TaskService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code GiveMeMyTasksHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 30. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class GiveMeMyTasksHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link GiveMeMyTasksHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public GiveMeMyTasksHandler(HandlerContainer container) {

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

        if(message instanceof GiveMeMyTasks) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.TASK_SERVICE)) {

                TaskManagement tm = (TaskManagement) s;
                String username = ((GiveMeMyTasks) message).getContent();

                TaskService ts = new TaskService();

                List<Task> tasks = new ArrayList<>();

                try {

                    //TODO
                    tasks.addAll(ts.getByUserNameAndState(username, ETaskState.ASSIGNED));

                    tasks.addAll(ts.getByUserNameAndState(username, ETaskState.CONFIRMED));

                } catch (SQLException e) {

                    LOG.log(Level.SEVERE, "Unable to get data from this DB! " + e.getMessage());
                }

                if(tm.getTaskSolverContainer().getTaskSolver(username) != null) {

                    tm.getTaskSolverContainer().getTaskSolver(username).clearTasks();

                    for (Task t : tasks) {

                        tm.getTaskSolverContainer().getTaskSolver(username).add(t);
                    }

                    connection.send(new AllTasksContainer(tasks));
                }

                return true;
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

        return new GiveMeMyTasksHandler(container);
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
     * The main method of the class of GiveMeMyTasksHandler.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: GiveMeMyTasksHandler class");
        System.err.println(">>> Creating GiveMeMyTasksHandler instance...");
        GiveMeMyTasksHandler instance = new GiveMeMyTasksHandler();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
