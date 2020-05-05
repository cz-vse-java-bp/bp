package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.PreOrderContainerMessage;
import cz.vse.java.messages.RemovePreOrderItem;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.util.persistance.entities.orders.PreOrder;
import cz.vse.java.util.persistance.entities.orders.PreOrderItem;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RemovePreOrderItemHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 05. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class RemovePreOrderItemHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RemovePreOrderItemHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RemovePreOrderItemHandler(HandlerContainer container) {

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

        if(message instanceof RemovePreOrderItem) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.ORDER_MANAGEMENT)) {

                OrderManagement om = (OrderManagement) s;

                String identificator = (String) ((RemovePreOrderItem) message).getContent()[1];
                Long id = (Long) ((RemovePreOrderItem) message).getContent()[0];

                PreOrder po = om.getPreOrder(identificator);

                if(po != null) {

                    PreOrderItem poi = om.getPreOrder(identificator).getPOI(id);

                    if (poi != null) {

                        LOG.log(Level.INFO, "Removing PreOrderItem from PreOrder of " + identificator);

                        try {

                            po.reset(poi);

                        } catch (SQLException e) {

                            LOG.log(Level.SEVERE, "Connection with DB failed! " + e.getMessage());

                        } finally {

                            connection.send(new PreOrderContainerMessage(po));
                        }
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

        return new RemovePreOrderItemHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
