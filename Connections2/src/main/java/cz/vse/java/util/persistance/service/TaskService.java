package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.entities.*;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.service
 */
public class TaskService extends AEntityService implements IPersistor {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TaskService() {

        super(EDBUse.TASK_MANAGEMENT);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Saves the {@link IEntity} to the database. First, it checks if
     * the instance is not already there. When it is, it's just updated;
     * otherwise the new creation is done.</p>
     *
     * @param entity to be persisted
     * @return result of the creation
     * @throws SQLException when something goes wrong
     */
    @Override
    public void persist(IEntity entity) throws SQLException {

        if(entity instanceof Task) {

            if(!exists(entity)) {

                Task t = (Task) entity;

                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);

                if(new OrderItemService().exists(t.getOrderItem())) {

                   String query = "INSERT INTO TASK VALUES " +
                           "(?, ?, ?, ?, ?, ?)";
                   PreparedStatement ps = conn.prepareStatement(query);
                   ps.setLong(1, t.getId());

                   if(t.getUser() != null) {

                       ps.setLong(2, t.getUser().getId());

                   } else {

                       ps.setNull(2, Types.BIGINT);
                   }

                   ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                   ps.setLong(4, t.getState().getId());
                   ps.setLong(5, t.getOrderItem().getId());
                   ps.setString(6, t.getDescription());

                   ps.execute();

                } else {

                    LOG.log(Level.SEVERE, "Order Item does not exist!");
                    throw new UnsupportedOperationException("Such an OrderItem does not exist!");
                }

                conn.commit();

            } else {

                LOG.log(Level.SEVERE, "Entity exists already!");

                throw new IllegalArgumentException("Given entity already exists!");
            }

        } else {

            LOG.log(Level.SEVERE, "Unsupported entity type!");
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }
    }

    /**
     * <p>Checks the existence of the {@link IEntity} in the database.</p>
     *
     * @param entity to be checked.
     * @return result of the test
     * @throws SQLException when something goes wrong.
     */
    @Override
    public boolean exists(IEntity entity) throws SQLException {

        if(entity instanceof Task) {

            Task t = (Task) entity;

            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            String query = "SELECT ID FROM TASK WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, t.getId());

            ResultSet rs = ps.executeQuery();

            conn.commit();

            if(rs.next()) {

                return true;
            }

        } else {

            LOG.log(Level.SEVERE, "Unsupported entity!");
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }

        return false;
    }

    /**
     * <p>Returns all {@link IEntity} instances from the
     * given database.</p>
     *
     * @return all entities from the database
     */
    @Override
    public List<IEntity> getAll() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        List<IEntity> tasks = new ArrayList<>();

        String query = "SELECT * FROM TASK";
        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Task t = new Task();

            t.setId(rs.getLong(1));
            t.setUser((User) new UserService().get(rs.getLong(2)));
            t.setCreated(rs.getTimestamp(3).toLocalDateTime());
            t.setState(ETaskState.getById(rs.getLong(4)));

            t.setOrderItem((OrderItem) new OrderItemService().get(rs.getLong(5)));
            t.setDescription(rs.getString(6));

            tasks.add(t);
        }

        conn.commit();

        return tasks;
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
        String query = "SELECT * FROM TASK WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();


        Task t = null;

        if(rs.next()) {

            t = new Task();

            t.setId(rs.getLong(1));
            t.setUser((User) new UserService().get(rs.getLong(2)));
            t.setCreated(rs.getTimestamp(3).toLocalDateTime());
            t.setState(ETaskState.getById(rs.getLong(4)));

            OrderItem oi = (OrderItem) new OrderItemService().get(rs.getLong(5));
            oi.addTask(t);

            t.setDescription(rs.getString(6));
        }

        conn.commit();

        return t;
    }

    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity entity to be updated in the database.
     */
    @Override
    public void update(IEntity entity) throws SQLException {

        if(entity instanceof Task) {

            if(this.exists(entity)) {
                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);

                Task t = (Task) entity;

                String query = "UPDATE TASK SET " +
                        "EMP_ID = ?, " +
                        "CREATED = ?, " +
                        "TASK_STATE_ID = ?, " +
                        "ORDER_ITEM_ID = ?, " +
                        "DESCRIPTION = ? " +
                        "WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(query);

                if (t.getUser() != null) {

                    ps.setLong(1, t.getUser().getId());

                } else {

                    ps.setNull(1, Types.BIGINT);
                }

                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(3, t.getState().getId());
                ps.setLong(4, t.getOrderItem().getId());
                ps.setString(5, t.getDescription());
                ps.setLong(6, t.getId());

                ps.execute();

                conn.commit();

            } else {

                LOG.log(Level.SEVERE, "Entity does not exist!");
            }

        } else {

            LOG.log(Level.SEVERE, "NOT SUPPORTED ENTITY!");
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }

    }

    /**
     * <p>Removes the {@link IEntity} from the database.</p>
     *
     * @param entity {@link IEntity} to be removed
     * @throws SQLException when there is any error.
     */
    @Override
    public void delete(IEntity entity) throws SQLException {

        if(entity instanceof Task) {

            Task t = (Task) entity;
            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            String query = "DELETE FROM TASK WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, t.getId());

            ps.execute();

            conn.commit();

        } else {

            LOG.log(Level.SEVERE, "Unsupported entity type! "
                    + entity.getClass().getName() +
                    " is not supported in TaskManagement");
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }
    }



    public List<Task> getByOrderItemsId(Long id) throws SQLException {

        List<Task> tasks = new ArrayList<>();

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT * FROM TASK WHERE ORDER_ITEM_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Task t = new Task();

            t.setId(rs.getLong(1));
            t.setUser((User) new UserService().get(rs.getLong(2)));
            t.setCreated(rs.getTimestamp(3).toLocalDateTime());
            t.setState(ETaskState.getById(rs.getLong(4)));
            t.setOrderItem((OrderItem) new OrderItemService().get(id));
            t.setDescription(rs.getString(6));

            tasks.add(t);
        }

        conn.commit();
        return tasks;
    }


    public Long getUniqueId() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        String query = "SELECT TASK_SEQ.NEXTVAL";
        PreparedStatement ps = conn.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            return rs.getLong(1);
        }

        return null;
    }

    public List<Task> get(ETaskState state) throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM TASK WHERE TASK_STATE_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, state.getId());

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Task t = new Task();
            t.setId(rs.getLong(1));
            t.setUser((User) new UserService().get(rs.getLong(2)));
            t.setCreated(rs.getTimestamp(3).toLocalDateTime());
            t.setState(ETaskState.getById(rs.getLong(4)));
            t.setOrderItem((OrderItem) new OrderItemService().get(rs.getLong(5)));
            t.setDescription(rs.getString(6));

            tasks.add(t);
        }

        conn.commit();
        return tasks;
    }


    public List<Task> getByState(ETaskState state) throws SQLException {

        Connection conn = this.getConnection().getConnection();

        conn.setAutoCommit(false);

        String query = "SELECT TASK.ID FROM TASK " +
                "WHERE TASK.TASK_STATE_ID = ?";

        PreparedStatement ps = conn.prepareStatement(query);

        ps.setLong(1, state.getId());

        ResultSet rs = ps.executeQuery();

        List<Task> tasks = new ArrayList<>();

        while (rs.next()) {

            tasks.add((Task) this.get(rs.getLong(1)));
        }

        conn.commit();

        return tasks;
    }


    public List<Task> getByUserNameAndState(String username, ETaskState state) throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        List<Task> tasks = new ArrayList<>();

        String query =
                "SELECT * FROM TASK " +
                "JOIN EMPLOYEES ON TASK.EMP_ID = EMPLOYEES.ID " +
                "WHERE EMPLOYEES.USERNAME LIKE ? AND TASK.TASK_STATE_ID = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ps.setLong(2, state.getId());

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Task t = new Task();
            t.setId(rs.getLong(1));
            t.setUser((User) new UserService().get(rs.getLong(2)));
            t.setCreated(rs.getTimestamp(3).toLocalDateTime());
            t.setState(ETaskState.getById(rs.getLong(4)));
            t.setOrderItem((OrderItem) new OrderItemService().get(rs.getLong(5)));
            t.setDescription(rs.getString(6));

            tasks.add(t);
        }

        conn.commit();

        return tasks;
    }


    public List<Task> getNullUser() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);
        String query = "SELECT ID FROM TASK WHERE EMP_ID IS ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setNull(1, Types.BIGINT);

        List<Task> tasks = new ArrayList<>();

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            IEntity e = this.get(rs.getLong(1));

            if(e != null) {

                tasks.add((Task) e);
            }
        }

        conn.commit();

        return tasks;
    }


    public List<Task> getNullUser(ETaskState state) throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT ID FROM TASK WHERE EMP_ID IS ? AND TASK_STATE_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setNull(1, Types.BIGINT);
        ps.setLong(2, state.getId());

        ResultSet rs = ps.executeQuery();

        List<Task> tasks = new ArrayList<>();

        while (rs.next()) {

            Task t = (Task) this.get(rs.getLong(1));

            if(t != null) {

                tasks.add(t);
            }
        }

        conn.commit();

        return tasks;
    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of TaskManagement.
     *
     */
    public static void main(String[] args) throws SQLException {
        
        System.err.println(">>> QuickTest: TaskManagement class");
        System.err.println(">>> Creating TaskManagement instance...");

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.EMPLOYEE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.TASK_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.ORDERS_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        TaskService instance = new TaskService();

        System.out.println("tasks without users");
        instance.getNullUser().forEach(n-> System.out.println(n.getId()));

        System.err.println(">>> Creation successfull...");
    }
    

}
