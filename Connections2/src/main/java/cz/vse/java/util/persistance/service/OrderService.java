package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.entities.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 11. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.service
 */
public class OrderService extends AEntityService implements IPersistor {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderService() {

        super(EDBUse.ORDERS_MANAGEMENT);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Saves the {@link IEntity} to the database. First, it checks if
     * the instance is not already there. When it is, it's just updated;
     * otherwise the new creation is done.</p>
     *
     * @param entity to be persisted
     * @throws SQLException when something goes wrong
     */
    @Override
    public void persist(IEntity entity) throws SQLException {

        if(entity instanceof Order) {

            Order o = (Order) entity;

            if(!this.exists(o)) {

                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);
                String query = "INSERT INTO ORDERS VALUES " +
                        "(?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setLong(1, o.getId());
                ps.setString(2, o.getSubmitter());
                ps.setString(3, o.getContact());
                ps.setString(4, o.getNote());

                ps.execute();

                synchronized (o.getOrderItems()) {

                    List<OrderItem> orderItems = new ArrayList<>();

                    for (OrderItem oi : o.getOrderItems()) {

                        OrderItemService ois = new OrderItemService();
                        Long id = ois.getUniqueId();

                        oi.setId(id);
                        orderItems.add(oi);

                        ois.update(oi);
                    }

                    for (OrderItem oi : orderItems) {

                        o.addOrderItem(oi);
                    }
                }

                conn.commit();

            } else {

                LOG.log(Level.SEVERE, "Entity does exist already!");
            }

        } else {

            LOG.log(Level.SEVERE, "NOT SUPPORTED ENTITY - ORDER REQUIRED");
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

        if(entity instanceof Order) {

            Order o = (Order) entity;

            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            String query = "SELECT ID FROM ORDERS WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, o.getId());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return true;
            }

            conn.commit();

        } else {

            LOG.log(Level.SEVERE, "Not supported entity! ORDER required.");
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

        List<IEntity> orders = new ArrayList<>();

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT * FROM ORDERS";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Order o = new Order();
            o.setId(rs.getLong(1));
            o.setSubmitter(rs.getString(2));
            o.setContact(rs.getString(3));
            o.setNote(rs.getString(4));

            String subQuery = "SELECT ID FROM ORDER_ITEM WHERE ORDER_ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(subQuery);
            ps2.setLong(1, o.getId());
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                OrderItem oi = (OrderItem) new OrderItemService().get(rs2.getLong(1));
                o.addOrderItem(oi);
            }

            orders.add(o);
        }

        conn.commit();
        return orders;
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

        Order order = null;

        String query = "SELECT * FROM ORDERS WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {

            order = new Order();
            order.setId(rs.getLong(1));
            order.setSubmitter(rs.getString(2));
            order.setContact(rs.getString(3));
            order.setNote(rs.getString(4));

            String subquery = "SELECT ID FROM ORDER_ITEM WHERE ORDER_ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(subquery);
            ps2.setLong(1, order.getId());

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                OrderItem orderItem = (OrderItem) new OrderItemService().get(rs2.getLong(1));
                order.addOrderItem(orderItem);
            }
        }

        conn.commit();
        return order;
    }

    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity entity to be updated in the database.
     */
    @Override
    public void update(IEntity entity) throws SQLException {

        if(entity instanceof Order) {

            Order o = (Order) entity;

            if(this.exists(o)) {

                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);

                String query = "UPDATE ORDERS SET " +
                        "SUBMITTER = ?, " +
                        "CONTACT = ?, " +
                        "NOTE = ? " +
                        "WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, o.getSubmitter());
                ps.setString(2, o.getContact());
                ps.setString(3, o.getNote());
                ps.setLong(4, o.getId());

                ps.execute();

                for (OrderItem oi : o.getOrderItems()) {

                    o.addOrderItem(oi);
                    OrderItemService ois = new OrderItemService();
                    ois.update(oi);
                }

                conn.commit();
            }
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

        if(entity instanceof Order) {

            Order o = (Order) entity;

            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            String query = "DELETE FROM ORDERS WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, o.getId());

            ps.execute();

            conn.commit();
        }
    }



    public Long getUniqueId() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        String query = "SELECT ORDER_SEQ.NEXTVAL";
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        Long id = null;

        if(rs.next()) {

            id = rs.getLong(1);
        }

        return id;
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
     * The main method of the class of OrderService.
     *
     */
    public static void main(String[] args) throws SQLException {
        
        System.err.println(">>> QuickTest: OrderService class");
        System.err.println(">>> Creating OrderService instance...");

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.ORDERS_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );

        DatabaseConnectionContainer.getInstance().add(
                EDBUse.STORAGE_MANAGEMENT,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
        );


        OrderService instance = new OrderService();

        //code


        OrderItem oi = (OrderItem) new OrderItemService().get(6397L);

        System.err.println(">>> Creation successfull...");
    }
    

}
