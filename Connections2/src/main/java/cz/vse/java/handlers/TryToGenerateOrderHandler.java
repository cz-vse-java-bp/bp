package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.OrderTransformationResult;
import cz.vse.java.messages.PreOrderContainerMessage;
import cz.vse.java.messages.TryToGenerateOrder;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.entities.orders.PreOrder;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TryToGenerateOrderHandler} is used to abstractly define
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
public class TryToGenerateOrderHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TryToGenerateOrderHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TryToGenerateOrderHandler(HandlerContainer container) {

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

        if(message instanceof TryToGenerateOrder) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.ORDER_MANAGEMENT)) {

                OrderManagement om = (OrderManagement) s;

                String ident = ((TryToGenerateOrder) message).getContent();

                PreOrder po = om.getPreOrder(ident);


                if(po != null) {

                    Order o = null;

                    try {

                        o = po.prepareOrder();

                        if(o != null) {

                            if(om.persistNewOrder(o)) {

                                connection.send(new OrderTransformationResult(true));

                                om.removePreOrder(ident);

                            } else {

                                LOG.log(Level.SEVERE, "Cannot be persisted.");
                                connection.send(new OrderTransformationResult(false));
                                connection.send(new PreOrderContainerMessage(po));
                            }
                        } else {

                            LOG.log(Level.SEVERE, "Cannot be persisted.");
                            connection.send(new OrderTransformationResult(false));
                            connection.send(new PreOrderContainerMessage(po));
                        }

                    } catch (SQLException e) {

                        LOG.log(Level.SEVERE, "Cannot get unique ID from the DB! " + e.getMessage());

                    } catch (Exception e) {

                        connection.send(new PreOrderContainerMessage(po));
                        connection.send(new OrderTransformationResult(false));
                    }
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

        return new TryToGenerateOrderHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
