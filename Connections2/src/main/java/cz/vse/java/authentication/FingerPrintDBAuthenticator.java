package cz.vse.java.authentication;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.routerSide.R2SConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AuthMeMessage;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.FingerPrintContainerMessage;
import cz.vse.java.messages.FingerPrintContainerRequest;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.FingerPrint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code FingerPrintDBAuthenticator} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This instance provides the {@link FingerPrint} check against the data in the database.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.authentication
 * @see FingerPrint
 */
public class FingerPrintDBAuthenticator extends AHandler implements IAuthenticationScenario {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /**
     * {@link Connection} instance created by
     * {@link DBConnection} with database.
     */
    private Connection database;

    /**
     * Service the check is provided for.
     */
    private EServiceType serviceType;

    /**
     * Information about if it passed or not.
     */
    private boolean passed = false;

    /**
     * Information about if the {@link FingerPrint} was
     * or wasn't provided.
     */
    private boolean gotFP = false;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link FingerPrintDBAuthenticator class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Parametric constructor filling the fields and creating ancestor instance.</p>
     *
     * @param container     the container this instance belongs to.
     * @param serviceType   the type this authentication is provided for
     * @param database      connection with DB, where the data about access is stored.
     */
    public FingerPrintDBAuthenticator(HandlerContainer container,
                                      EServiceType serviceType,
                                      Connection database) {

        super(container);
        this.database = database;
        this.serviceType = serviceType;
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

            if (connection instanceof R2SConnection) {

                if (message instanceof AuthMeMessage) {

                    connection.send(new FingerPrintContainerRequest());

                } else if (message instanceof FingerPrintContainerMessage) {

                    FingerPrint fp = ((FingerPrintContainerMessage) message).getContent();
                    String ip = fp.getIp();
                    String mac = fp.getMac();

                    try {
                        database.setAutoCommit(false);

                        if (getIPs().contains(ip) && getMACs().contains(mac)) {

                            ((R2SConnection) connection).updateAuthState(this.getAuthScenarioType(), true);
                            this.passed = true;
                            LOG.log(Level.SEVERE, "'" + this.getAuthScenarioType().getDescription() + "' passed! IP: " + ip + ", MAC: " + mac);
                        } else {

                            LOG.log(Level.SEVERE, "Fingerprint the authenticator obtained does not correspond!");
                            connection.send(new ErrorMessage(EErrorType.WRONG_FINGERPRINT));
                            connection.close();
                        }

                        database.commit();
                    } catch (SQLException e) {
                        LOG.log(Level.SEVERE, "Error while communicating with DB: " + e.getMessage());
                        connection.send(new ErrorMessage(EErrorType.CANNOT_AUTHENTICATE));
                        return false;
                    }
                }
                return true;
            } else {

                LOG.log(Level.SEVERE, "Unsupported connection type! " + connection.getClass().getName());
                return false;
            }
        } else {

            LOG.log(Level.SEVERE, "Already passed!");
            return true;
        }
    }


    /**
     * <p>Private method returning all available IPs for this service.</p>
     *
     * @return  {@link ArrayList} of {@link String}-formed IP addresses from
     *          DB with access to this service.
     *
     * @throws SQLException     warns about failed DB connection.
     */
    private ArrayList<String> getIPs() throws SQLException {

        ArrayList<String> ips = new ArrayList<>();

        String query =
                "SELECT IP_ADDRESS " +
                "FROM IP_ADDRESS_ACCESS " +
                "JOIN SERVICE_TYPE ON " +
                        "IP_ADDRESS_ACCESS.ID_SERVICE = SERVICE_TYPE.ID " +
                "WHERE SERVICE_TYPE.SERVICE_NAME LIKE ?";

        PreparedStatement ipStatement = database.prepareStatement(query);
        ipStatement.setString(1, this.serviceType.name().toUpperCase());

        ResultSet rs = ipStatement.executeQuery();

        String ip;

        while(rs.next()) {

            ip = rs.getString(1);
            ips.add(ip);
        }

        return ips;
    }


    /**
     * <p>Private method returning all available MAC addresses for this service.</p>
     *
     * @return  {@link ArrayList} of {@link String}-formed MAC addresses from
     *          DB with access to this service.
     *
     * @throws SQLException     warns about failed DB connection.
     */
    private ArrayList<String> getMACs() throws SQLException {

        ArrayList<String> macs = new ArrayList<>();

        String query =
                "SELECT MAC " +
                        "FROM MAC_ACCESS " +
                        "JOIN SERVICE_TYPE ON " +
                        "MAC_ACCESS.ID_SERVICE = SERVICE_TYPE.ID " +
                        "WHERE SERVICE_TYPE.SERVICE_NAME LIKE ?";

        PreparedStatement ipStatement = database.prepareStatement(query);
        ipStatement.setString(1, this.serviceType.name().toUpperCase());

        ResultSet rs = ipStatement.executeQuery();

        String mac;

        while(rs.next()) {

            mac = rs.getString(1);
            macs.add(mac);
        }

        return macs;
    }

    /**
     * <p>Clones the instance.</p>
     *
     * @param container         this handler <i>(polymorfism)</i> belongs to.
     *
     * @return cloned handler   new instance with same parameters in fields.
     */
    @Override
    public IHandler copy(HandlerContainer container) {

        return new FingerPrintDBAuthenticator(container, this.serviceType, this.database);
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

        return EAuthenticationScenarioType.FP_VIA_DB;
    }
}
