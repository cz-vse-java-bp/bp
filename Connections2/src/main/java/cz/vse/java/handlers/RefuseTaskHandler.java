package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.RefuseTask;
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
 * <p>The class of {@code RefuseTaskHandler} is used to abstractly define
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
public class RefuseTaskHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RefuseTaskHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RefuseTaskHandler(HandlerContainer container) {

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

        if(message instanceof RefuseTask) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.TASK_SERVICE)) {

                TaskManagement tm = (TaskManagement) s;

                String username = (String) ((RefuseTask) message).getContent()[0];
                Long id = (Long) ((RefuseTask) message).getContent()[1];

                System.out.println("Task about to be refused: " + id);

                List<Task> tasks = tm.getTasks(username);

                Task toBeRemoved = null;

                for (Task t : tasks) {

                    if(t.getId().equals(id)) {

                        System.out.println(">>>>> Right task found and gonna be removed");

                        t.setUser(null);
                        t.setState(ETaskState.NOT_ASSIGNED);

                        TaskService ts = new TaskService();

                        toBeRemoved = t;

                        try {

                            ts.update(t);

                        } catch (SQLException e) {

                            LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                        }
                    }
                }

                if(toBeRemoved != null) {

                    tm.getTaskSolverContainer()
                            .getTaskSolver(username)
                            .getTasks(username)
                            .remove(toBeRemoved);

                    LOG.log(Level.SEVERE, "Task removed and gonna be reassigned! "
                            + toBeRemoved.toString());
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

        return new RefuseTaskHandler(container);
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
     * The main method of the class of RefuseTaskHandler.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: RefuseTaskHandler class");
        System.err.println(">>> Creating RefuseTaskHandler instance...");
        RefuseTaskHandler instance = new RefuseTaskHandler();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
