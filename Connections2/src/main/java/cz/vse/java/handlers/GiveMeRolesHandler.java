package cz.vse.java.handlers;


import cz.vse.java.connections.routerSide.R2CConnection;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.handlers.utils.AHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.ErrorMessage;
import cz.vse.java.messages.GiveMeRoles;
import cz.vse.java.messages.RolesContainerMessage;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.userData.ERole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code GiveMeRolesHandler} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 *
 * @see cz.vse.java.handlers
 */
public class GiveMeRolesHandler extends AHandler {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private DBConnection connection;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link GiveMeRolesHandler class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public GiveMeRolesHandler(HandlerContainer container) {

        super(container);
        this.connection = DatabaseConnectionContainer.getInstance()
                .get(EDBUse.USER_AUTHENTICATION);
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

        if(message instanceof GiveMeRoles) {

            if(connection instanceof R2CConnection) {

                if(connection.getConnectionManager()
                        .getService().getServiceType()
                        .equals(EServiceType.ROUTER)) {

                    String query = "SELECT ROLE.ID FROM EMPLOYEES " +
                            "JOIN EMPLOYEES_ROLE ON EMPLOYEES.ID = EMP_ID " +
                            "JOIN ROLE ON ROLE.ID = ROLE_ID " +
                            "WHERE EMPLOYEES.USERNAME LIKE ?";

                    Connection conn = this.connection.getConnection();

                    ArrayList<ERole> roles = new ArrayList<>();

                    try {
                        PreparedStatement ps = conn.prepareStatement(query);

                        ps.setString(1, ((GiveMeRoles) message).getContent());

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {

                            roles.add(ERole.getById(rs.getLong(1)));
                        }
                    } catch (SQLException e) {

                        connection.send(new ErrorMessage(EErrorType.NO_DB_RESULT_FOUND));
                    }

                    connection.send(new RolesContainerMessage(roles));

                    return true;
                }
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

        return new GiveMeRolesHandler(container);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
