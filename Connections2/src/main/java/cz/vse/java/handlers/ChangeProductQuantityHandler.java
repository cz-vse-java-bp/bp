package cz.vse.java.handlers;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ChangeProductQuantity;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.StorageService;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.service.ProductService;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ChangeProductQuantityHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class ChangeProductQuantityHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ChangeProductQuantityHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ChangeProductQuantityHandler(HandlerContainer container) {

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

        if(message instanceof ChangeProductQuantity) {

            if(connection instanceof S2CConnection) {

                IService service = connection.getConnectionManager().getService();

                if(service instanceof StorageService) {

                    ProductService ps = ((StorageService) service).getProductService();

                    try {

                        Product product = (Product) ps.get(((ChangeProductQuantity) message).getContent()[0]);

                        if(product != null) {

                            int currentValue = product.getQuantity();

                            long quantityChangeL = ((ChangeProductQuantity) message).getContent()[1];

                            if(currentValue >= quantityChangeL) {

                                int quantityChange = (int) quantityChangeL;
                                product.setQuantity(product.getQuantity() + quantityChange);

                                ps.update(product);

                            } else {

                                LOG.log(Level.SEVERE, "Unsupported value of " + quantityChangeL +
                                        " - not possible to make such a massive change at once!");

                                throw new IllegalArgumentException("Not possible to make such a massive change in DB!");
                            }

                        } else {

                            connection.send(new ErrorMessage(EErrorType.NO_DB_RESULT_FOUND));
                        }

                        return true;

                    } catch (SQLException e) {

                        LOG.log(Level.SEVERE, "Error while executing these changes in DB: " + e.getMessage());
                    }

                } else {

                    LOG.log(Level.SEVERE, "Not supported service!");
                }

            } else {

                LOG.log(Level.SEVERE, "Not supported connection!");
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

        return new ChangeProductQuantityHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
