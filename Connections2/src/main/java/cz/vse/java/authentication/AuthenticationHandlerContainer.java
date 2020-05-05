package cz.vse.java.authentication;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.utils.IMessage;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AuthenticationHandlerContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Instances of {@link AuthenticationHandlerContainer} are special kinds of
 * the ancestor of {@link HandlerContainer} instances. The only difference is in
 * contained {@link cz.vse.java.handlers.utils.AHandler}s. These need to be implementing
 * the {@link IAuthenticationScenario} and be {@link IHandler} implementing instances
 * at the same time.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.authentication
 * @see HandlerContainer
 * @see cz.vse.java.handlers.utils.AHandler
 * @see IHandler
 * @see TokenValidator
 * @see FingerPrintDBAuthenticator
 * @see UserAuthenticator
 */
public class AuthenticationHandlerContainer extends HandlerContainer {


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AuthenticationHandlerContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Constructor creating instance of it's ancestor only.</p>
     *
     * @see HandlerContainer
     */
    public AuthenticationHandlerContainer() {

        super();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method of {@code add(IHandler handler)} is overriden from the
     * ancestor - {@link IAuthenticationScenario} implementing check added.</p>
     *
     * @param handler   implementing {@link IHandler} and {@link IAuthenticationScenario}
     *                  at the same time. When this condition is not provided true,
     *                  it won't be added.
     */
    @Override
    public void add(IHandler handler) {

        if(handler instanceof IAuthenticationScenario) {

            super.add(handler);

        } else {

            LOG.log(Level.SEVERE, "Cannot add this kind of handler! " + handler.getClass().getName());
        }
    }

    @Override
    public boolean handle(IConnection connection, IMessage message) {

        boolean indicator = false;

        synchronized (super.getHandlers()) {

            if(connection != null) {

                for (IHandler handler : super.getHandlers()) {

                    if(handler.handle(connection, message)) {

                        indicator = true;
                    }
                }

            } else {

                LOG.log(Level.SEVERE, "Connection is null!");
            }
        }

        if(!indicator) {

            LOG.log(Level.SEVERE,
                    "Message was not handled. " +
                    "No suitable handler for: " +
                    message.getClass().getName()
            );
        }

        return indicator;
    }
}
