package cz.vse.java.util.persistance.entities.tasks;


import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.service.TaskService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 */
public class TaskContainer implements ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<Task> tasks;
    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskContainer() {

        this.tasks = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the TaskContainer instance.
     */
    @Override
    public String toString() {
        return "TaskContainer{" +
                "tasks=" + tasks +
                '}';
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


    /**
     * <p>Removes all given tasks from the field.</p>
     */
    public void clear() {

        synchronized (this.tasks) {

            this.tasks.clear();
        }
    }


    /**
     * <p>Adds all given {@link Task}s from the {@link List}.</p>
     *
     * <p>Before adding, the container is cleared.</p>
     *
     * <p>Also notifies listening {@link IObserver}s.</p>
     *
     * @param tasks     to be added.
     */
    public void addAll(List<Task> tasks) {

        synchronized (this.tasks) {

            this.tasks.clear();
            this.tasks.addAllAbsent(tasks);
        }
        notifyObservers();
    }

    /**
     * <p>Adds given {@link Task}. When already added, it won't
     * be added to the field.</p>
     *
     * <p>Also notifies listening {@link IObserver}s.</p>
     *
     * @param task  to be added
     */
    public void add(Task task) {

        synchronized (this.tasks) {

            Task found = this.getTask(task.getId());

            if (found != null) {

                this.tasks.remove(found);
            }

            this.tasks.addIfAbsent(task);

            notifyObservers();
        }
    }


    /**
     * <p>Removes task from the container.</p>
     *
     * <p>Also notifies listening {@link IObserver}s.</p>
     *
     * @param task  to be removed
     */
    public void remove(Task task) {

        synchronized (this.tasks) {

            this.tasks.remove(task);
            notifyObservers();
        }
    }


    /**
     * <p>Removes task from the container by it's id.</p>
     *
     * @param id    of the Task to be removed
     */
    public void remove(Long id) {

        if(id != null) {

            Task task = this.getTask(id);

            synchronized (this.tasks) {

                if(task != null) {

                    this.remove(task);
                }
            }
        }
    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code tasks}
     * of the instance of {@link TaskContainer}
     *
     * @return the value of {@code tasks}
     * @see CopyOnWriteArrayList<>
     * @see TaskContainer
     */
    public CopyOnWriteArrayList<Task> getTasks() {

        return tasks;
    }



    /**
     * <p>Returns all task of given user.</p>
     *
     * @param user  the returned tasks belongs to
     *
     * @return      {@link List} of {@link Task}
     *              belonging to the user
     */
    public List<Task> getTasks(User user) {

        List<Task> tasks = new ArrayList<>();

        synchronized (this.tasks) {

            for (Task t : this.tasks) {

                if(t.getUser() != null) {

                    if (t.getUser().getId().equals(user.getId())) {

                        tasks.add(t);
                    }
                }
            }
        }

        return tasks;
    }


    /**
     * <p>Returns {@link Task} with such an ID or null</p>
     *
     * @param id    id used for searching
     * @return      Task with such an ID
     */
    public Task getTask(Long id) {

        synchronized (this.tasks) {
            for (Task t : this.tasks) {

                if (t.getId().equals(id)) {

                    return t;
                }
            }
        }

        return null;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    public void setTaskState(Task task, ETaskState state) throws SQLException {

        synchronized (this.tasks) {

            for (Task t : this.tasks) {

                if(t.getId().equals(task.getId())) {

                    t.setState(state);
                    TaskService ts = new TaskService();
                    ts.update(t);
                }
            }
        }
    }
}
