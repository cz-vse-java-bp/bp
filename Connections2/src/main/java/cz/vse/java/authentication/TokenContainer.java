package cz.vse.java.authentication;


import cz.vse.java.connections.serviceSide.S2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.util.Token;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TokenContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Instances of this class are used for validating the {@link Token}
 * instances.</p>
 * <p>First of all, the {@link Token} is added to the field, then anyone
 * can ask if any other token is valid (if it is contained in this container).
 * If true, this token is removed from the field and set to the given connection.
 * If false, it just gives information about that back.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.authentication
 * @see Token
 * @see TokenValidator
 */
public class TokenContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<Token> tokens;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TokenContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Nonparametric constructor for initializing empty instance.</p>
     */
    public TokenContainer() {

        this.tokens = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Validates the given token against already saved tokens.</p>
     *
     * @param connection    connection the token is validated for
     * @param token         token to be validated.
     *
     * @return              result information of the test
     */
    public boolean validate(IConnection connection, Token token) {

        boolean indicator = false;

        synchronized (this.tokens) {

            for (Token t : this.tokens) {

                if(t.validate(token)) {

                    if(connection instanceof S2CConnection) {

                        connection.setToken(t);
                        indicator = true;
                        break;

                    } else {

                        throw new UnsupportedOperationException("Not supported for other connection yet!");
                    }
                }
            }
        }

        if(indicator) {

            this.tokens.remove(token);
        }

        return indicator;
    }


    /**
     * <p>Adds token if it is not contained already.</p>
     *
     * @param token to be added
     */
    public void add(Token token) {

        synchronized (this.tokens) {

            this.tokens.addIfAbsent(token);
        }
    }
}
