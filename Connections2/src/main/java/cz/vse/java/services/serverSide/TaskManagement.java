package cz.vse.java.services.serverSide;


import cz.vse.java.handlers.GiveMeMyTasksHandler;
import cz.vse.java.handlers.ListeningForTasksContainerHandler;
import cz.vse.java.handlers.RefuseTaskHandler;
import cz.vse.java.handlers.TaskStateChangeHandler;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.service.TaskService;
import cz.vse.java.util.userTaskAssignment.IAssignScenario;
import cz.vse.java.util.userTaskAssignment.RandomAssign;
import cz.vse.java.util.userTaskAssignment.TaskSolverContainer;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 *
 *
 * @see cz.vse.java.services
 */
public class TaskManagement extends AGeneralService implements IService, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    private final CopyOnWriteArrayList<IObserver> observers;

    private IAssignScenario assignScenario;
    private TaskSolverContainer taskSolverContainer;

    private Runnable autoUpdate;
    private boolean update = false;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskManagement(String routerIP,
                          int routerPort,
                          int clientsPort,
                          int maxClients,
                          String pathToTrustStore,
                          String passwordToTrustStore,
                          String pathToKeyStore,
                          String passwordToKeyStore) {

        super(
                EServiceType.TASK_SERVICE,
                routerIP,
                routerPort,
                clientsPort,
                maxClients,
                false,
                pathToTrustStore,
                passwordToTrustStore,
                pathToKeyStore,
                passwordToKeyStore
        );

        this.observers = new CopyOnWriteArrayList<>();
        this.taskSolverContainer = new TaskSolverContainer(new RandomAssign());

        this.update = true;

        super.clients.addMessageHandler(new ListeningForTasksContainerHandler(null));
        super.clients.addMessageHandler(new TaskStateChangeHandler(null));
        super.clients.addMessageHandler(new GiveMeMyTasksHandler(null));
        super.clients.addMessageHandler(new RefuseTaskHandler(null));
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        new Thread(super.router).start();
        new Thread(super.clients).start();

        prepareThread();
    }


    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.addIfAbsent(observer);
        }
    }

    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void removeObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.remove(observer);
        }
    }

    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    @Override
    public void notifyObservers() {

        synchronized (this.observers) {

            for (IObserver observer : observers) {

                observer.update();
            }
        }
    }


    public void prepareThread() {

        this.autoUpdate = new Runnable(){

            @Override
            public void run() {

                while (update) {

                    try {

                        updateTasks();

                        checkTasks(new TaskService().getByState(ETaskState.CONFIRMED));
                        checkTasks(new TaskService().getByState(ETaskState.DONE));
                        checkTasks(new TaskService().getByState(ETaskState.NOT_DONE));
                        checkTasks(new TaskService().getByState(ETaskState.ASSIGNED));

                        Thread.sleep(5000);

                    } catch (SQLException e) {

                        e.printStackTrace();

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }
                }
            }
        };

        new Thread(autoUpdate).start();
    }


    public void updateTasks() throws SQLException {

        TaskService ts = new TaskService();
        List<Task> tasks = ts.getNullUser(ETaskState.NOT_ASSIGNED);

        LOG.log(Level.INFO, "Gonna reassign not assigned tasks. There are " + tasks.size() + " of them.");

        for (Task e : tasks) {

            this.taskSolverContainer.assign(e);
        }
    }


    public List<Task> getTasks(String userName) {

        return this.taskSolverContainer.getByName(userName);
    }


    public void checkTasks(List<Task> tasks) {

        for (Task task : tasks) {

            User u = task.getUser();

            if(u != null) {

                String username = u.getUserName();

                List<Task> userTasks = this.getTasks(username);

                if(userTasks != null) {

                    for (Task task2 : userTasks) {

                        task2.setState(task.getState());
                    }
                }
            }
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link TaskSolverContainer} formed {@code taskSolverContainer}
     * of the instance of {@link TaskManagement}
     *
     * @return the value of {@code taskSolverContainer}
     * @see TaskSolverContainer
     * @see TaskManagement
     */
    public TaskSolverContainer getTaskSolverContainer() {

        return taskSolverContainer;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of TaskManagement.
     *
     */
    public static void main(String[] args) {

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.USER_AUTHENTICATION,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.FINGERPRINT_AUTHENTICATION,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.EMPLOYEE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.TASK_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.ORDERS_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );


        ClassLoader classLoader = TaskManagement.class.getClassLoader();
        File keystore = new File(classLoader.getResource("stores/keyStore.jks").getFile());

        ClassLoader classLoader2 = TaskManagement.class.getClassLoader();
        File truststore = new File(classLoader2.getResource("stores/trustStore.jts").getFile());

        TaskManagement instance = new TaskManagement(
                "localhost",
                888,
                555,
                50,
                keystore.getAbsolutePath(), "changeit",
                truststore.getAbsolutePath(), "changeit"
        );

        new Thread(instance).start();

        instance.prepareThread();
    }
}
