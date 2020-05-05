package cz.vse.java.util.userTaskAssignment;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.AddTaskMessage;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.entities.tasks.TaskContainer;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.service.TaskService;
import cz.vse.java.util.persistance.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskSolver} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>The {@link TaskSolver} is an entity <i>solving tasks</i>. It means
 * it listens to the tasks and is able to solve them. Instances of this class
 * are defined by {@link String} username, {@link IConnection} where it listens,
 * and boolean interpretation if the solver is currently listening to new tasks.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 18. 04. 2020
 */
public class TaskSolver implements IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    private String userName;
    private TaskContainer container;
    private IConnection connection;

    private boolean listening = false;

    private TaskSolverContainer tsc;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskSolver class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Constructor for defining the fields.</p>
     *
     * @param userName      username
     * @param connection    where the user is listening to
     * @param tsc           container of these TaskSolvers
     */
    public TaskSolver(String userName, IConnection connection, TaskSolverContainer tsc) {

        this.userName = userName;
        this.connection = connection;
        this.container = new TaskContainer();


        ((S2CConnection) connection).addObserver(this);

        this.tsc = tsc;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns tasks this user has assigned.</p>
     *
     * @param userName  for check if it really is meant for this one.
     *
     * @return          {@link List} of {@link Task}s assigned to this user
     */
    public List<Task> getTasks(String userName) {

        if(userName.equals(this.userName)) {

            return this.container.getTasks();
        }

        return null;
    }


    /**
     * <p>Number of the tasks set to the user.</p>
     *
     * @return  integer representation of number of
     * tasks assigned to this user
     */
    public int size() {

        return this.getTasks(this.userName).size();
    }


    /**
     * <p>Adds the given {@link Task} to the user assignments.</p>
     *
     * @param task      to be assigned
     */
    public void add(Task task) {

        if(listening) {

            Long id = null;

            this.remove(id);

            this.container.add(task);

            User u = null;

            try {

                u = (User) new UserService().findByUserName(this.userName);

            } catch (SQLException e) {

                e.printStackTrace();
            }

            if (task.getState().equals(ETaskState.NOT_ASSIGNED)) {

                System.out.println("Task was not assigned, now should be");
                task.setState(ETaskState.ASSIGNED);
            }

            if (u != null) {

                task.setUser(u);

                try {

                    new TaskService().update(task);

                } catch (SQLException e) {

                    LOG.log(Level.SEVERE, "Failed to work with DB! " + e.getMessage());
                }
            }
        }
    }


    /**
     * <p>Clears the tasks.</p>
     */
    public void clearTasks() {

        this.container.clear();
    }

    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     */
    @Override
    public void update() {

        if(connection instanceof S2CConnection) {

            S2CConnection conn = (S2CConnection) connection;

            LOG.log(Level.SEVERE, "Updating connection");

            if(!conn.isRunning() && conn.isAuthenticated()) {

                listening = false;
                this.tsc.resetTasks(this);
            }

        } else {

            LOG.log(Level.SEVERE, "UNSUPPORTED CONNECTION TYPE!");
        }
    }


    public void remove(Long id) {

        this.container.remove(id);
    }


    /**
     * <p>Updates the {@link ETaskState} of the {@link Task}</p>
     *
     * @param state     to be set to
     * @param taskID    ID of the task
     */
    public void updateTaskState(ETaskState state, Long taskID) {

        for (Task t : this.getTasks(this.getUserName())) {

            if(t.getId().equals(taskID)) {

                t.setState(state);
                TaskService ts = new TaskService();
                try {

                    ts.update(t);

                } catch (SQLException e) {

                    LOG.log(Level.SEVERE, "Cannot update the task! " + e.getMessage());
                }
                break;
            }
        }
    }

    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code userName}
     * of the instance of {@link TaskSolver}
     *
     * @return the value of {@code userName}
     * @see String
     * @see TaskSolver
     */
    public String getUserName() {

        return userName;
    }


    /**
     * Getter for {@link IConnection} formed {@code connection}
     * of the instance of {@link TaskSolver}
     *
     * @return the value of {@code connection}
     * @see IConnection
     * @see TaskSolver
     */
    public IConnection getConnection() {

        return connection;
    }

    /**
     * Getter for {@link boolean} formed {@code listening}
     * of the instance of {@link TaskSolver}
     *
     * @return the value of {@code listening}
     * @see boolean
     * @see TaskSolver
     */
    public boolean isListening() {

        return listening;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code listening} variable.</p>
     *
     * @param listening given $field.typeName value to
     *                  be set to the variable
     * @see boolean
     * @see TaskSolver
     */
    public void setListening(boolean listening) {

        this.listening = listening;
    }
}
