package cz.vse.java.handlers;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.ProductAllContainer;
import cz.vse.java.messages.ProductAllRequest;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.services.serverSide.StorageService;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductAllRequestHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * .vse.java.handlers
 */
public class ProductAllRequestHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductAllRequestHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProductAllRequestHandler(HandlerContainer container) {

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

        if(message instanceof ProductAllRequest) {

            if(connection instanceof S2CConnection) {

                IService service = connection.getConnectionManager().getService();

                if(service instanceof StorageService) {

                    ArrayList<Product> products = new ArrayList<>();

                    try {

                        for (IEntity entity : ((StorageService) service).getProductService().getAll()) {

                            products.add((Product) entity);
                        }

                    } catch (SQLException e) {

                        LOG.log(Level.SEVERE, "Error while reading from DB!");

                    } finally {

                        if(products.size() > 0) {

                            connection.send(new ProductAllContainer(products));

                        } else {

                            connection.send(new ErrorMessage(EErrorType.NO_DB_RESULT_FOUND));
                        }
                    }

                    return true;

                } else {

                    LOG.log(Level.SEVERE, "Not supported service!");
                }

            } else {

                LOG.log(Level.SEVERE, "Not supported connection type: " + connection.getClass().getName());
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

        return new ProductAllRequestHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
