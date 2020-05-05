package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.OrderContainerMessage;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.util.persistance.entities.orders.Order;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderContainerMessageHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 13. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class OrderContainerMessageHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderContainerMessageHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderContainerMessageHandler(HandlerContainer container) {

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

        if(message instanceof OrderContainerMessage) {

            IService service = connection.getConnectionManager().getService();
            if(service instanceof OrderManagement) {

                Order order = ((OrderContainerMessage) message).getContent();
                OrderManagement om = (OrderManagement) service;
                om.persistNewOrder(order);

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

        return new OrderContainerMessageHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
