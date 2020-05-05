package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ListeningForTasksContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class ListeningForTasksContainer extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String username;
    private Boolean sendMeTasks;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ListeningForTasksContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ListeningForTasksContainer(String username, boolean sendMeTasks) {

        this.username = username;
        this.sendMeTasks = sendMeTasks;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * <p>First index of the Object[] contains username,
     * the second one contains {@link Boolean} value.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.username, this.sendMeTasks};
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
