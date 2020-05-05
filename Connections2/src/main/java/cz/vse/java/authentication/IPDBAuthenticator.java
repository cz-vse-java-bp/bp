package cz.vse.java.authentication;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.IPAddressContainer;
import cz.vse.java.messages.utils.IMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code IPDBAuthenticator} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This authenticator works with database. The given IP address is checked
 * against the set of the databases from the DB.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.authentication
 */
public class IPDBAuthenticator extends AHandler implements IAuthenticationScenario {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** Connection with database */
    private Connection database;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link IPDBAuthenticator class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Constructor creating an instance of ancestor and filling the
     * fields.</p>
     *
     * @param container this instance belongs to
     * @param database  connection with DB
     */
    public IPDBAuthenticator(HandlerContainer container, Connection database) {

        super(container);

        this.database = database;


    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method for resolving the authentication scenario type.</p>
     *
     * @return the type of authentication scenario
     */
    @Override
    public EAuthenticationScenarioType getAuthScenarioType() {

        return EAuthenticationScenarioType.IP_VIA_DB;
    }

    /**
     * <p>Handles the message the given connection received.</p>
     *
     * @param connection receiver of the message
     * @param message    received message
     * @return boolean interpretation if it was or wasn't handled.
     */
    @Override
    public boolean handle(IConnection connection, IMessage message) {

        if(message instanceof IPAddressContainer) {

            String query = "SELECT IP_ADDRESS FROM IP_ADDRESS_ACCESS";

            try {
                database.setAutoCommit(false);

                PreparedStatement statement = database.prepareStatement(query);

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {

                    LOG.log(Level.FINE, rs.getString(1));
                }

                database.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

    /**
     * <p>Clones the instance.</p>
     *
     * @param container new instance should belong to
     * @return cloned handler
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new IPDBAuthenticator(container, this.database);
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
