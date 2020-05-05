package cz.vse.java.authentication;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.util.Token;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TokenValidator} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Validator of the tokens - when any connection wants to connect
 * to connection with {@link TokenValidator} checker, it has to run
 * through this test.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.authentication
 */
public class TokenValidator extends AHandler implements IAuthenticationScenario {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** Container this validator is working with */
    private TokenContainer container;

    /** Already passed information storage */
    private boolean passed = false;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TokenValidator class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Constructor of the instance initializing the ancestor and setting
     * field of {@code container}</p>
     *
     * @param container         container this test belongs to
     * @param tokenContainer    to check the tokens with
     */
    public TokenValidator(HandlerContainer container, TokenContainer tokenContainer) {

        super(container);
        this.container = tokenContainer;
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

        if(!passed) {

            if (message instanceof AuthMeMessage) {

                connection.send(new TokenRequest());
                return true;

            } else if (message instanceof TokenContainerMessage) {

                Token token = ((TokenContainerMessage) message).getContent();

                if (token != null) {

                    boolean indicator = this.container.validate(
                            connection,
                            token
                    );

                    this.passed = indicator;

                    ((S2CConnection) connection).updateAuthState(this.getAuthScenarioType(), indicator);

                    if (!indicator) {

                        LOG.log(Level.SEVERE, "Wrong token obtained! " + token.getToken());
                        connection.send(new ErrorMessage(EErrorType.WRONG_TOKEN));
                    }

                } else {

                    LOG.log(Level.SEVERE, "Null token obtained!");
                    connection.send(new ErrorMessage(EErrorType.NULL_MESSAGE_CONTENT));
                }
            }
            return true;
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

        return new TokenValidator(container, this.container);
    }

    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Method for resolving the authentication scenario type.</p>
     *
     * @return the type of authentication scenario
     */
    @Override
    public EAuthenticationScenarioType getAuthScenarioType() {

        return EAuthenticationScenarioType.TOKEN_CHECK;
    }
}
