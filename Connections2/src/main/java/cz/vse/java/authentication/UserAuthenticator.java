package cz.vse.java.authentication;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.routerSide.R2CConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.utils.cryptography.hashing.EHashAlgorithm;
import cz.vse.java.utils.cryptography.hashing.Hasher;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.utils.random.RandomNumberGenerator;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UserAuthenticator} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Authenticator of the user using <b>username</b>, <b>password</b>
 * and values from DB.</p>
 *
 * <p>The security of the passwords is provided using random number of
 * needed hashes of the password. When both sides of the connection gets
 * the same result of n-hashes with salt, it returns true about the test.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class UserAuthenticator extends AHandler implements IAuthenticationScenario {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** {@link DBConnection} with database*/
    private DBConnection database;

    /** info if it passed or not */
    private boolean passed = false;

    /** storing information about username and password validity */
    private boolean validUsername = false;
    private boolean validPassword = false;

    /** stored data - at the end it is removed */
    private String username;
    private String passFromDB;
    private String hashedPass;
    private String salt;
    private int numOfHashes;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UserAuthenticator class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final EHashAlgorithm alg = EHashAlgorithm.SHA512;

    private static final EAuthenticationScenarioType type =
            EAuthenticationScenarioType.USER_VIA_PASSWORD;

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Constructor responsible for the initialization of the instance.</p>
     *
     * @param container this test belongs to
     * @param database  connection with DB
     */
    public UserAuthenticator(HandlerContainer container, DBConnection database) {

        super(container);
        this.database = database;
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


        if(connection instanceof R2CConnection) {

            if (passed) {

                return true;

            } else {

                if (message instanceof AuthMeMessage) {

                    connection.send(new UserNameRequest());
                    return true;
                }

                if (message instanceof UserNameContainerMessage) {

                    Connection conn = database.getConnection();
                    try {
                        conn.setAutoCommit(false);

                        PreparedStatement statement = conn.prepareStatement(
                                "SELECT USERNAME, PASS_HASH, HASH_SALT FROM EMPLOYEES WHERE USERNAME LIKE ?"
                        );

                        statement.setString(1, ((UserNameContainerMessage) message).getContent());

                        ResultSet rs = statement.executeQuery();

                        if (rs.next()) {

                            this.username = rs.getString(1);
                            this.validUsername = true;
                            this.passFromDB = rs.getString(2);
                            this.salt = rs.getString(3);
                            this.numOfHashes = RandomNumberGenerator.getRandomNumberInRange(10, 1000);

                            connection.send(new PasswordRequest(salt, numOfHashes));

                            this.hashedPass = this.hash();

                        } else {

                            validUsername = false;
                            LOG.log(Level.SEVERE, "Wrong username!");
                            connection.send(new AuthenticationResultContainer(false));
                        }

                        conn.commit();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    return true;

                }
                if (message instanceof PasswordContainerMessage) {

                    boolean indicator = ((PasswordContainerMessage) message).getContent().equals(hashedPass);
                    ((R2CConnection) connection).updateAuthState(type, indicator);

                    if(indicator) {

                        LOG.log(Level.INFO, "User " + username + " authenticated successfully!");
                        tidyUp();

                    } else {

                        connection.send(new AuthenticationResultContainer(false));
                        connection.send(new ErrorMessage(EErrorType.WRONG_PASSWORD));
                        LOG.log(Level.SEVERE, "Wrong password!");
                    }

                    return true;
                }
            }
        } else {

            LOG.log(Level.SEVERE, "Unsupported connection!");
            return true;
        }

        return false;
    }


    /**
     * <p>Provides random number of hashes of the password.</p>
     *
     * @return  password hashed (n-1) times - assuming the password
     *          in the DB is already hashed once.
     */
    private String hash() {

        Hasher hasher = new Hasher(alg);

        String result = this.passFromDB;

        for (int i = 0; i < numOfHashes - 1; i++) {

            try {
                result = hasher.generateHashWithSalt(result, salt);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * <p>Cleans all the sensitive fields.</p>
     */
    private void tidyUp() {

        passed = true;
        this.hashedPass = null;
        this.salt = null;
        this.passFromDB = null;
        this.numOfHashes = 0;
        this.validPassword = false;
        this.validUsername = false;
    }

    /**
     * <p>Clones the instance.</p>
     *
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new UserAuthenticator(container, this.database);
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

        return type;
    }
}
