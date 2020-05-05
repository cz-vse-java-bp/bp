package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code GiveMeMyTasks} is used to abstractly define
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
public class GiveMeMyTasks extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String userName;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link GiveMeMyTasks class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public GiveMeMyTasks(String userName) {

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
     * @return the content the message contains, which stands
     * for the username of the user wanting the messages update.
     */
    @Override
    public String getContent() {

        return this.userName;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of GiveMeMyTasks.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: GiveMeMyTasks class");
        System.err.println(">>> Creating GiveMeMyTasks instance...");
        GiveMeMyTasks instance = new GiveMeMyTasks();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
