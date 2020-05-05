package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.UniqueOrderIdentRequest;
import cz.vse.java.messages.UsePreOrderIdent;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.utils.random.RandomStringGenerator;

import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UniqueOrderIdentRequestHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class UniqueOrderIdentRequestHandler extends AHandler  {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UniqueOrderIdentRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UniqueOrderIdentRequestHandler(HandlerContainer container) {

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

        if(message instanceof UniqueOrderIdentRequest) {

            IService service = connection.getConnectionManager().getService();

            if(service.getServiceType().equals(EServiceType.ORDER_MANAGEMENT)) {

                OrderManagement om = (OrderManagement) service;

                List<String> idents = om.getPreOrdersIdentificators();

                boolean unique = false;
                String newIdent = "";

                while(!unique) {

                    unique = true;
                    newIdent = new RandomStringGenerator().generateRandomString(30);

                    if (idents.contains(newIdent)) {

                        unique = false;
                    }
                }

                om.createNewPreorder(newIdent, connection);
                connection.send(new UsePreOrderIdent(newIdent));

                return true;
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

        return new UniqueOrderIdentRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
