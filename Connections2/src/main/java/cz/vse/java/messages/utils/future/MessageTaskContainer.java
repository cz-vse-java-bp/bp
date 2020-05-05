package cz.vse.java.messages.utils.future;


import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code MessageTaskContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This kind of container is used for storing
 * {@link cz.vse.java.messages.utils.IMessage} implementing instances
 * sealed in {@link MessageTask} to be sent in the future.</p>
 *
 * <p>Mostly they are executed when the connection is authenticated,
 * ie when the connection successfully passes the authentication
 * procedure and is informed about it. When it won't, the message task
 * gets expired.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.messages.utils
 * @see cz.vse.java.messages.utils.IMessage
 * @see MessageTask
 */
public class MessageTaskContainer implements IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<MessageTask> tasks;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link MessageTaskContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Non-parametric constructor initializing thread-safe field for
     * {@link MessageTask} instances.</p>
     */
    public MessageTaskContainer() {

        this.tasks = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Adds a message task to the container for future execution.</p>
     *
     * @param task  {@link MessageTask} to be executed.
     *
     * @see MessageTaskContainer#update()
     */
    public void add(MessageTask task) {

        synchronized (this.tasks) {

            this.tasks.addIfAbsent(task);
        }
    }

    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     *
     * <p>It tries to run all the given {@link MessageTask}.
     * When there is an expired message task, it's removed.
     * When the task is executed, it will remove it, otherwise it
     * remains in the container till it's execution or expiration.</p>
     */
    @Override
    public void update() {

        synchronized (this.tasks) {

            ArrayList<MessageTask> removals = new ArrayList<>();

            for (MessageTask task : this.tasks) {

                if(task.isExpired()) {

                    removals.add(task);

                } else {

                    if(task.runTask()){

                        removals.add(task);
                    }
                }
            }

            LOG.log(Level.INFO, "Message tasks solved or expired: " + removals.size() +
                    ", remains: " + (this.tasks.size() - removals.size()));

            for (MessageTask task : removals) {

                this.tasks.remove(task);
            }
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
