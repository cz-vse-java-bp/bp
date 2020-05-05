package cz.vse.java.handlers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.GiveMeMyOrder;
import cz.vse.java.messages.PreOrderChangeResult;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.serverSide.IService;
import cz.vse.java.util.persistance.entities.Product;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PreOrderChangeResultHandler} is used to abstractly define
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
public class PreOrderChangeResultHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PreOrderChangeResultHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PreOrderChangeResultHandler(HandlerContainer container) {

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

        if(message instanceof PreOrderChangeResult) {

            IService s = connection.getConnectionManager().getService();

            if(s.getServiceType().equals(EServiceType.CLIENT)) {

                Product p = (Product) ((PreOrderChangeResult) message).getContent()[0];
                int q = (int) ((PreOrderChangeResult) message).getContent()[1];
                String ident = (String) ((PreOrderChangeResult) message).getContent()[2];

                if(ident.equals(Client.getInstance().getPreOrder().getIdentificator())) {

                    Client.getInstance().getPreOrder().addToPOI(p, q);
                    Client.getInstance().addMessageTask(
                            new MessageTask(new GiveMeMyOrder(ident), EServiceType.ORDER_MANAGEMENT));
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

        return new PreOrderChangeResultHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
