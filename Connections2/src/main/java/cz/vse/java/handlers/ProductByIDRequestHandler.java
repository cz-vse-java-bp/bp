package cz.vse.java.handlers;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.ProductByIDContainer;
import cz.vse.java.messages.ProductByIDRequest;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.StorageService;
import cz.vse.java.util.persistance.entities.Product;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductByIDRequestHandler} is used to abstractly define
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
public class ProductByIDRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductByIDRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProductByIDRequestHandler(HandlerContainer container) {

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

        if(message instanceof ProductByIDRequest) {

            if(connection instanceof S2CConnection) {

                if(connection.getConnectionManager().getService() instanceof StorageService) {

                    StorageService ss = (StorageService) connection.getConnectionManager().getService();

                    Long id = ((ProductByIDRequest) message).getContent();

                    try {

                        Product product = (Product) ss.getProductService().get(id);

                        if(product != null) {

                            connection.send(new ProductByIDContainer(product));

                        } else {

                            connection.send(new ErrorMessage(EErrorType.NO_DB_RESULT_FOUND));
                        }

                    } catch (SQLException e) {

                        LOG.log(Level.SEVERE, "Error while connecting to DB.");
                    }

                    return true;

                } else {

                    throw new UnsupportedOperationException("Not suitable service type!");
                }
            } else {

                throw new UnsupportedOperationException("Not suitable connection type! " +
                        "Required: " + S2CConnection.class.getName() + ", obtained: " + connection.getClass().getName());
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

        return new ProductByIDRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
