package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AddPreOrderItem;
import cz.vse.java.messages.PreOrderChangeResult;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.OrderManagement;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.PreOrder;
import cz.vse.java.util.persistance.service.ProductService;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AddPreOrderItemHandler} is used to abstractly define
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
public class AddPreOrderItemHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AddPreOrderItemHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AddPreOrderItemHandler(HandlerContainer container) {

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

        if(message instanceof AddPreOrderItem) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.ORDER_MANAGEMENT)) {

                OrderManagement om = (OrderManagement) s;

                Product p = (Product) ((AddPreOrderItem) message).getContent()[0];
                int q = (int) ((AddPreOrderItem) message).getContent()[1];
                String ident = (String) ((AddPreOrderItem) message).getContent()[2];

                PreOrder po = om.getPreOrder(ident);

                if(po != null) {
                    Product product = null;

                    try {

                        product = (Product) new ProductService().get(p.getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if(product != null) {

                        p.setQuantity(product.getQuantity());
                        int quantity = p.getQuantity();

                        if (quantity >= q) {

                            po.addToPOI(p, q);

                            ProductService ps = new ProductService();
                            p.setQuantity(quantity - q);
                            try {
                                ps.update(p);
                                connection.send(new PreOrderChangeResult(p, q, po.getIdentificator()));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {

                            LOG.log(Level.SEVERE, "Not possible to do so! Product does not have enough quantity!");
                        }
                    } else {

                        LOG.log(Level.SEVERE, "Product not found in DB!");
                    }
                } else {

                    LOG.log(Level.SEVERE, "PreOrder not found!");
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

        return new AddPreOrderItemHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
