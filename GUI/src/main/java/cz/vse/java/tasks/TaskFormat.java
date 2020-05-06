package cz.vse.java.tasks;


import cz.vse.java.util.persistance.entities.tasks.Task;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskFormat} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 17. 04. 2020
 *
 *
 * @see cz.vse.java.tasks
 */
public class TaskFormat {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;

    private String taskDesc;
    private String taskState;
    private String taskNote;

    private Task task;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskFormat class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskFormat(Task task) {

        this.task = task;

        this.id = task.getId();
        this.taskDesc = task.getDescription();
        this.taskState = task.getState().getDesc();
        this.taskNote = this.task.getOrderItem().getOrder().getNote();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the TaskFormat instance.
     */
    @Override
    public String toString() {

        return "TaskFormat.toString() - NOT DEFINED YET!";
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link TaskFormat}
     *
     * @return the value of {@code id}
     * @see Long
     * @see TaskFormat
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code taskDesc}
     * of the instance of {@link TaskFormat}
     *
     * @return the value of {@code taskDesc}
     * @see String
     * @see TaskFormat
     */
    public String getTaskDesc() {

        return taskDesc;
    }

    /**
     * Getter for {@link String} formed {@code taskState}
     * of the instance of {@link TaskFormat}
     *
     * @return the value of {@code taskState}
     * @see String
     * @see TaskFormat
     */
    public String getTaskState() {

        return taskState;
    }

    /**
     * Getter for {@link String} formed {@code taskNote}
     * of the instance of {@link TaskFormat}
     *
     * @return the value of {@code taskNote}
     * @see String
     * @see TaskFormat
     */
    public String getTaskNote() {

        return taskNote;
    }

    /**
     * Getter for {@link Task} formed {@code task}
     * of the instance of {@link TaskFormat}
     *
     * @return the value of {@code task}
     * @see Task
     * @see TaskFormat
     */
    public Task getTask() {

        return task;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Long} formed
     * {@code id} variable.</p>
     *
     * @param id given Long value to
     *           be set to the variable
     * @see Long
     * @see TaskFormat
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code taskDesc} variable.</p>
     *
     * @param taskDesc given String value to
     *                 be set to the variable
     * @see String
     * @see TaskFormat
     */
    public void setTaskDesc(String taskDesc) {

        this.taskDesc = taskDesc;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code taskState} variable.</p>
     *
     * @param taskState given String value to
     *                  be set to the variable
     * @see String
     * @see TaskFormat
     */
    public void setTaskState(String taskState) {

        this.taskState = taskState;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code taskNote} variable.</p>
     *
     * @param taskNote given String value to
     *                 be set to the variable
     * @see String
     * @see TaskFormat
     */
    public void setTaskNote(String taskNote) {

        this.taskNote = taskNote;
    }

}
