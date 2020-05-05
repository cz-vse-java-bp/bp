package cz.vse.java.messages;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.ICommand;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AskForTaskCommand} is used to abstractly define
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
public class AskForTaskCommand extends AMessage implements ICommand {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AskForTaskCommand class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AskForTaskCommand() {
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method defining the execution procedure.</p>
     *
     * @param connection receiver and executor of the message.
     */
    @Override
    public void execute(IConnection connection) {
/*
        Client.getInstance().addMessageTask(new MessageTask(
                new GiveMeTasksMessage(UserProperties.getInstance().getUserName()),
                EServiceType.TASK_SERVICE,
                360L
        ));*/

        System.out.println(">>>>>>>>>>>>>>>>>>>> NOT IMPLEMENTED YET");

    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
