package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RefuseTask} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 30. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class RefuseTask extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String username;
    private Long id;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RefuseTask class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RefuseTask(String username, Long id) {

        this.username = username;
        this.id = id;
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
     * <p>In the first parameter, there is username. in the second
     * index, there is an ID of the task to be reassigned.</p>
     *
     * @return the content the message contains.
     *
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.username, this.id};
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of RefuseTask.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: RefuseTask class");
        System.err.println(">>> Creating RefuseTask instance...");
        RefuseTask instance = new RefuseTask();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
