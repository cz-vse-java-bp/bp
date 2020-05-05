package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.GiveMeMyOrder;
import cz.vse.java.messages.PreOrderContainerMessage;
import cz.vse.java.messages.UsePreOrderIdent;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.util.persistance.entities.orders.PreOrder;
import cz.vse.java.utils.random.RandomStringGenerator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code GiveMeMyOrderHandler} is used to abstractly define
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
public class GiveMeMyOrderHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link GiveMeMyOrderHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public GiveMeMyOrderHandler(HandlerContainer container) {

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

        if(message instanceof GiveMeMyOrder) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.ORDER_MANAGEMENT)) {

                OrderManagement om = (OrderManagement) s;
                PreOrder po = om.getPreOrder(((GiveMeMyOrder) message).getContent());

                if(po != null) {

                    connection.send(new PreOrderContainerMessage(po));

                } else {

                    List<String> used = om.getPreOrdersIdentificators();

                    LOG.log(Level.SEVERE, "PreOrder with such an ident not found: "
                            + ((GiveMeMyOrder) message).getContent());

                    boolean foundUnique = false;
                    String newIdent = "";

                    while(!foundUnique) {

                        foundUnique = true;
                        newIdent = new RandomStringGenerator().generateRandomString(30);

                        if (used.contains(newIdent)) {

                            foundUnique = false;
                        }
                    }

                    om.createNewPreorder(newIdent, connection);
                    connection.send(new UsePreOrderIdent(newIdent));
                    connection.send(new PreOrderContainerMessage(new PreOrder()));
                }

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

        return new GiveMeMyOrderHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
