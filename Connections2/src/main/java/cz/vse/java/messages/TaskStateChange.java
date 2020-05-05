package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskStateChange} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 25. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class TaskStateChange extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long taskID;
    private ETaskState state;
    private String userName;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskStateChange class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskStateChange(Long taskID, ETaskState state, String userName) {

        this.taskID = taskID;
        this.state = state;
        this.userName = userName;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     * In the first index, there is userName, in the second, there is
     * id of the task to be changed and in the last one is the state to
     * be the task changed to.
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.userName, this.taskID, this.state};
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of TaskStateChange.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: TaskStateChange class");
        System.err.println(">>> Creating TaskStateChange instance...");
        TaskStateChange instance = new TaskStateChange();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
