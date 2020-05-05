package cz.vse.java.handlers.utils;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.IMessage;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code HandlerContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class HandlerContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<IHandler> handlers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link HandlerContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public HandlerContainer() {

        this.handlers = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    public boolean handle(IConnection connection, IMessage message) {

        synchronized (this.handlers) {

            if(connection != null) {

                for (IHandler handler : this.handlers) {

                    if(handler.handle(connection, message)) {

                        return true;
                    }
                }

            } else {

                LOG.log(Level.SEVERE, "Connection is null!");
            }
        }
        LOG.log(Level.SEVERE, "Message was not handled. No suitable handler for: " + message.getClass().getName());
        return false;
    }

    public void add(IHandler handler) {

        synchronized (this.handlers) {

            IHandler handler1 = handler.copy(this);
            handler1.setContainer(this);
            this.handlers.addIfAbsent(handler1);
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code handlers}
     * of the instance of {@link HandlerContainer}
     *
     * @return the value of {@code handlers}
     * @see CopyOnWriteArrayList<>
     * @see HandlerContainer
     */
    public CopyOnWriteArrayList<IHandler> getHandlers() {

        return handlers;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
