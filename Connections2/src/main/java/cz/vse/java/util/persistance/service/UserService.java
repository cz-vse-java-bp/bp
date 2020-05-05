package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.userData.ERole;
import cz.vse.java.util.persistance.entities.IEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code UserService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.service
 */
public class UserService extends AEntityService implements IPersistor {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link UserService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public UserService() {

        super(EDBUse.EMPLOYEE_MANAGEMENT);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void persist(IEntity entity) throws SQLException {

        if(entity instanceof User) {

            User user = (User) entity;

            if(!this.exists(user)) {

                Connection connection = super.getConnection().getConnection();
                connection.setAutoCommit(false);

                String query =
                        "INSERT INTO EMPLOYEES VALUES(?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, user.getId());
                ps.setString(2, user.getFirstName());
                ps.setString(3, user.getLastName());
                ps.setString(4, user.getUserName());
                ps.setString(5, user.getPassword());
                ps.setString(6, user.getPasswordSalt());
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

                ps.execute();

                for (ERole role : user.getRoles()) {

                    query = "INSERT INTO EMPLOYEES_ROLE VALUES (EMPLOYEE_ROLE_SEQ.NEXTVAL, ?, ?)";
                    ps = connection.prepareStatement(query);
                    ps.setLong(1, user.getId());
                    ps.setLong(2, role.getId());

                    ps.execute();
                }

                connection.commit();

            } else {

                update(user);
            }
        }
    }

    /**
     * <p>Returns all {@link IEntity} instances from the
     * given database.</p>
     *
     * @return all entities from the database
     */
    @Override
    public List<IEntity> getAll() throws SQLException {

        Connection connection = this.getConnection().getConnection();
        connection.setAutoCommit(false);

        ArrayList<IEntity> users = new ArrayList<>();
        String query =
                "SELECT * FROM EMPLOYEES";
        PreparedStatement ps = connection.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            User user = new User();
            user.setId(rs.getLong(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setUserName(rs.getString(4));
            user.setPassword(rs.getString(5));
            user.setPasswordSalt(rs.getString(6));
            user.setDateOfCreation(rs.getDate(7).toLocalDate());

            String query2 = "SELECT ROLE_ID FROM EMPLOYEES_ROLE WHERE EMP_ID = ?";

            PreparedStatement ps2 = connection.prepareStatement(query2);
            ps2.setLong(1, user.getId());
            ResultSet roles = ps2.executeQuery();

            while(roles.next()) {

                user.addRole(ERole.getById(roles.getLong(1)));
            }

            users.add(user);
        }

        connection.commit();
        return users;
    }

    /**
     * <p>Returns the {@link IEntity} with this id.</p>
     *
     * @param id {@link Long} id the entity has.
     * @return {@link IEntity} with given ID.
     */
    @Override
    public IEntity get(Long id) throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT * FROM EMPLOYEES " +
                "WHERE EMPLOYEES.ID = ?";

        User user = null;

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            user = new User();

            user.setId(rs.getLong(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setUserName(rs.getString(4));
            user.setPassword(rs.getString(5));
            user.setPasswordSalt(rs.getString(6));
            user.setDateOfCreation(rs.getDate(7).toLocalDate());

            query = "SELECT ROLE_ID FROM EMPLOYEES_ROLE " +
                    "WHERE EMP_ID = ?";


            ps = conn.prepareStatement(query);
            ps.setLong(1, user.getId());

            rs = ps.executeQuery();

            while(rs.next()) {

                user.addRole(ERole.getById(rs.getLong(1)));
            }
        }
        conn.commit();

        return user;
    }

    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity entity to be updated in the database.
     */
    @Override
    public void update(IEntity entity) throws SQLException {

        if(entity instanceof User) {

            if(this.exists(entity)) {

                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);

                User user = (User) entity;

                String query =
                        "DELETE FROM EMPLOYEES_ROLE WHERE EMP_ID = ?";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setLong(1, user.getId());

                ps.execute();


                query = "UPDATE EMPLOYEES SET " +
                        "FIRSTNAME = ?, " +
                        "LASTNAME = ?, " +
                        "USERNAME = ?, " +
                        "PASS_HASH = ?, " +
                        "HASH_SALT = ?, " +
                        "DATE_OF_CREATION = ? " +

                        "WHERE ID = ?";

                ps = conn.prepareStatement(query);

                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getUserName());
                ps.setString(4, user.getPassword());
                ps.setString(5, user.getPasswordSalt());

                if(user.getDateOfCreation() == null) {

                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

                } else {

                    ps.setTimestamp(6, Timestamp.valueOf(user.getDateOfCreation().atStartOfDay()));

                }ps.setLong(7, user.getId());

                ps.execute();

                for (ERole role : user.getRoles()) {

                    query = "INSERT INTO EMPLOYEES_ROLE " +
                            "VALUES (EMPLOYEE_ROLE_SEQ.NEXTVAL, ?, ?)";

                    ps = conn.prepareStatement(query);
                    ps.setLong(1, user.getId());
                    ps.setLong(2, role.getId());

                    ps.execute();
                }

                conn.commit();
            } else {

                this.persist(entity);
            }
        }
    }

    /**
     * <p>Checks the existance of the {@link IEntity} in the database.</p>
     *
     * @param entity to be checked.
     * @return result of the test
     * @throws SQLException when something goes wrong.
     */
    @Override
    public boolean exists(IEntity entity) throws SQLException {

        if(entity instanceof User) {

            Connection connection = super.getConnection().getConnection();

            String sqlQuery =
                    "SELECT * FROM EMPLOYEES " +
                    "WHERE ID = ?";

            PreparedStatement ps = connection.prepareStatement(sqlQuery);

            ps.setLong(1, ((User) entity).getId());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return true;

            } else {

                return false;
            }
        }

        return false;
    }

    /**
     * <p>Removes the {@link IEntity} from the database.</p>
     *
     * @param entity {@link IEntity} to be removed
     * @throws SQLException when there is any error.
     */
    @Override
    public void delete(IEntity entity) throws SQLException {

        if(entity instanceof User) {

            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);
            User u = (User) entity;

            if(this.exists(entity)) {

                String query = "DELETE FROM EMPLOYEES_ROLE WHERE EMP_ID = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setLong(1, u.getId());
                ps.execute();

                query = "DELETE FROM EMPLOYEES WHERE ID = ?";
                ps = conn.prepareStatement(query);
                ps.setLong(1, u.getId());
                ps.execute();
            }
        }
    }


    /**
     * <p>Tries to find an {@link User} instance by username
     * in the database.</p>
     *
     * @param username  Username to be looked for.
     *
     * @return          User with the given name or null, when
     *                  no such user is in the DB.
     */
    public IEntity findByUserName(String username) throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT ID FROM EMPLOYEES " +
                "WHERE USERNAME LIKE ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            return this.get(rs.getLong(1));
        }
        return null;
    }


    public Long getUniqueID() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        String query = "SELECT EMPLOYEE_SEQUENCE.NEXTVAL";
        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        Long i = null;

        if(rs.next()) {

            i = rs.getLong(1);
        }

        return i;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
