package cz.vse.java.handlers;


import cz.vse.java.connections.clientSide.C2SConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AddTaskMessage;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.util.persistance.entities.tasks.Task;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AddTaskMessageHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 25. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class AddTaskMessageHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AddTaskMessageHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AddTaskMessageHandler(HandlerContainer container) {

        super(container);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection receiver of the message
     * @param message    received message
     * @return boolean interpretation if it was or wasn't handled.
     */
    @Override
    public boolean handle(IConnection connection, IMessage message) {

        if(message instanceof AddTaskMessage) {

            if(connection instanceof C2SConnection) {

                IService s = connection.getConnectionManager().getService();

                if(s.getServiceType().equals(EServiceType.CLIENT)) {

                    Task t = ((AddTaskMessage) message).getContent();
                    Client.getInstance().addTask(t);

                    LOG.log(Level.INFO, "Adding task " + t);

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Clones the instance.</p>
     *
     * @param container to be set as default
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new AddTaskMessageHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of AddTaskMessageHandler.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: AddTaskMessageHandler class");
        System.err.println(">>> Creating AddTaskMessageHandler instance...");
        AddTaskMessageHandler instance = new AddTaskMessageHandler();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
