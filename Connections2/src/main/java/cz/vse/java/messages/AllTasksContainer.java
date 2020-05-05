package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AllTasksContainer} is used to abstractly define
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
public class AllTasksContainer extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private List<Task> tasks;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AllTasksContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AllTasksContainer(List<Task> tasks) {

        if(tasks == null) {

            tasks = new ArrayList<>();
        }

        System.out.println("SENDING TASKS: " + tasks.size());

        this.tasks = tasks;

        System.out.println("SENDING TASKS: " + this.getContent().size());
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
     */
    @Override
    public List<Task> getContent() {

        System.out.println("GET CONTENT - TASKS " + tasks.size());
        return this.tasks;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
