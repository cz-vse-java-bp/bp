package cz.vse.java.util.userTaskAssignment;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.service.TaskService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskSolverContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Container for {@link TaskSolver}s. Managing the assignment and basic
 * tasks related to it.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 18. 04. 2020
 *
 *
 * @see TaskSolver
 */
public class TaskSolverContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<TaskSolver> container;

    private IAssignScenario assigner;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskSolverContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Constructor used for defining the {@link IAssignScenario}.</p>
     * @param assigner  the assigner of the tasks.
     */
    public TaskSolverContainer(IAssignScenario assigner) {

        this.container = new CopyOnWriteArrayList<>();
        this.assigner = assigner;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Adds new {@link TaskSolver} by username and {@link IConnection}.</p>
     *
     * @param connection    where the user communicates at
     * @param userName      used for communication
     */
    public void add(IConnection connection, String userName) {

        this.container.add(new TaskSolver(userName, connection, this));
    }


    /**
     * <p>Manages the assignment of the task using given assigner.</p>
     *
     * @param task  to be assigned
     */
    public void assign(Task task) {

        this.assigner.assign(this, task);
    }


    /**
     * <p>Resets tasks of the specified {@link TaskSolver},
     * when the connection get's interrupted, for example.</p>
     *
     * @param ts    owner of the tasks.
     */
    public void resetTasks(TaskSolver ts) {

        List<Task> tasks = new ArrayList<>();

        TaskService taskService = new TaskService();

        try {

            tasks.addAll(taskService.getByUserNameAndState(ts.getUserName(), ETaskState.ASSIGNED));
            tasks.addAll(taskService.getByUserNameAndState(ts.getUserName(), ETaskState.CONFIRMED));

        } catch (SQLException e) {
            e.printStackTrace();
        }


        LOG.log(Level.SEVERE, "Reseting tasks of " + ts.getUserName());

        for (Task t : tasks) {

            if(!t.getState().equals(ETaskState.DONE) && !t.getState().equals(ETaskState.NOT_DONE)) {

                t.setState(ETaskState.NOT_ASSIGNED);
                t.setUser(null);

                try {

                    taskService.update(t);

                } catch (SQLException e) {

                    e.printStackTrace();
                }
            }
        }

        LOG.log(Level.SEVERE, tasks.size() + " tasks reseted.");

        ts.clearTasks();
        this.getContainer().remove(ts);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Returns all the usernames.</p>
     *
     * @return  {@link List} of {@link String}s with used usernames.
     */
    public List<String> getNames() {

        List<String> names = new ArrayList<>();

        for (TaskSolver ts : this.container) {

            names.add(ts.getUserName());
        }

        return names;
    }


    /**
     * <p>Gets all tasks assigned to specified username</p>
     * @param userName  to be searched with
     * @return          {@link List} of {@link Task}s assigned
     * to this username
     */
    public List<Task> getByName(String userName) {

        TaskSolver ts = this.getTaskSolver(userName);

        if(ts != null) {

            return ts.getTasks(userName);
        }

        return null;
    }

    /**
     * <p>Returns the {@link TaskSolver} by given username.</p>
     * <p>Can return null when nothing found.</p>
     * @param userName      to be searched with
     * @return              {@link TaskSolver} with such an name or null
     */
    public TaskSolver getTaskSolver(String userName) {

        for(TaskSolver ts : this.getContainer()) {

            if(ts.getUserName().equals(userName)) {

                return ts;
            }
        }

        return null;
    }


    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code container}
     * of the instance of {@link TaskSolverContainer}
     *
     * @return the value of {@code container}
     * @see CopyOnWriteArrayList<>
     * @see TaskSolverContainer
     */
    public CopyOnWriteArrayList<TaskSolver> getContainer() {

        return container;
    }
}
