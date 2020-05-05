package cz.vse.java.admin.utils;


import cz.vse.java.util.persistance.entities.tasks.Task;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskSealer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 01. 05. 2020
 *
 *
 * @see cz.vse.java.admin.utils
 */
public class TaskSealer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Task task;

    private Long id;
    private String username;
    private String state;
    private String desc;
    private Long orderItemID;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskSealer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskSealer(Task task) {

        this.task = task;

        if(task != null && task.getOrderItem() != null) {

            this.id = task.getId();
            this.desc = task.getDescription();
            this.orderItemID = task.getOrderItem().getId();
            this.state = task.getState().getDesc();

            if (task.getUser() != null) {

                this.username = task.getUser().getUserName();

            } else {

                this.username = "";
            }
        }
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the TaskSealer instance.
     */
    @Override
    public String toString() {

        return "TaskSealer.toString() - NOT DEFINED YET!";
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Task} formed {@code task}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code task}
     * @see Task
     * @see TaskSealer
     */
    public Task getTask() {
        return task;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code id}
     * @see Long
     * @see TaskSealer
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for {@link String} formed {@code username}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code username}
     * @see String
     * @see TaskSealer
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for {@link String} formed {@code state}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code state}
     * @see String
     * @see TaskSealer
     */
    public String getState() {
        return state;
    }

    /**
     * Getter for {@link String} formed {@code desc}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code desc}
     * @see String
     * @see TaskSealer
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Getter for {@link Long} formed {@code orderItemID}
     * of the instance of {@link TaskSealer}
     *
     * @return the value of {@code orderItemID}
     * @see Long
     * @see TaskSealer
     */
    public Long getOrderItemID() {
        return orderItemID;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Task} formed
     * {@code task} variable.</p>
     *
     * @param task given Task value to
     *             be set to the variable
     * @see Task
     * @see TaskSealer
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * <p>Setter for the {@code Long} formed
     * {@code id} variable.</p>
     *
     * @param id given Long value to
     *           be set to the variable
     * @see Long
     * @see TaskSealer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code username} variable.</p>
     *
     * @param username given String value to
     *                 be set to the variable
     * @see String
     * @see TaskSealer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code state} variable.</p>
     *
     * @param state given String value to
     *              be set to the variable
     * @see String
     * @see TaskSealer
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code desc} variable.</p>
     *
     * @param desc given String value to
     *             be set to the variable
     * @see String
     * @see TaskSealer
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * <p>Setter for the {@code Long} formed
     * {@code orderItemID} variable.</p>
     *
     * @param orderItemID given Long value to
     *                    be set to the variable
     * @see Long
     * @see TaskSealer
     */
    public void setOrderItemID(Long orderItemID) {
        this.orderItemID = orderItemID;
    }
}
