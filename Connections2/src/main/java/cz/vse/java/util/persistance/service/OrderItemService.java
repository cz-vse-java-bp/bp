package cz.vse.java.util.persistance.service;


import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.entities.*;
import cz.vse.java.util.persistance.entities.orders.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderItemService} is used to abstractly define
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
public class OrderItemService extends AEntityService implements IPersistor {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderItemService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderItemService() {

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
     * @return result of the creation
     * @throws SQLException when something goes wrong
     */
    @Override
    public void persist(IEntity entity) throws SQLException {

        if(entity instanceof OrderItem) {

            OrderItem oi = (OrderItem) entity;

            if(!this.exists(oi)) {

                if(new OrderService().exists(oi.getOrder())) {
                    Connection conn = this.getConnection().getConnection();
                    conn.setAutoCommit(false);

                    String query = "INSERT INTO ORDER_ITEM VALUES " +
                            "(?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setLong(1, oi.getId());
                    ps.setLong(2, oi.getProduct().getId());
                    ps.setInt(3, oi.getQuantity());
                    ps.setLong(4, oi.getOrder().getId());

                    ps.execute();
                    conn.commit();

                    TaskService ts = new TaskService();

                    for (Task t : oi.getTasks()) {

                        oi.addTask(t);
                        ts.update(t);
                    }
                } else {

                    LOG.log(Level.SEVERE, "Order does not exist!");
                }
            }

            LOG.log(Level.INFO, "Entity exists already!");

            this.update(oi);

        } else {

            LOG.log(Level.SEVERE, "UNSUPORTED ENTITY!");
            throw new UnsupportedOperationException("Unsupported entity: " + entity.getClass().getName());
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

        if(entity instanceof OrderItem) {

            OrderItem oi = (OrderItem) entity;
            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            String query = "SELECT ID FROM ORDER_ITEM WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, oi.getId());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                return true;
            }

            conn.commit();

        } else {

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

        List<IEntity> ois = new ArrayList<>();

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT * FROM ORDER_ITEM";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            OrderItem oi = new OrderItem(
                    rs.getLong(1),
                    (Product) new ProductService().get(rs.getLong(2)),
                    rs.getInt(3)
            );

            String subQuery = "SELECT * FROM ORDERS WHERE ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(subQuery);
            ps2.setLong(1, rs.getLong(4));

            ResultSet rs2 = ps2.executeQuery();

            Order order;

            if(rs2.next()) {

                order = new Order(
                        rs2.getLong(1),
                        rs2.getString(2),
                        rs2.getString(3),
                        rs2.getString(4)
                );
                order.addOrderItem(oi);
            }

            for (Task t : new TaskService().getByOrderItemsId(oi.getId())) {

                oi.addTask(t);
            }

            ois.add(oi);
        }

        conn.commit();

        LOG.log(Level.INFO, "Found " + ois.size() + " order items.");

        return ois;
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

        String query = "SELECT * FROM ORDER_ITEM WHERE ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        OrderItem oi = null;

        if(rs.next()) {

            oi = new OrderItem();

            oi.setId(rs.getLong(1));
            oi.setProduct((Product) new ProductService().get(rs.getLong(2)));
            oi.setQuantity(rs.getInt(3));

            String subQuery = "SELECT * FROM ORDERS WHERE ID = ?";
            PreparedStatement subPs = conn.prepareStatement(subQuery);
            subPs.setLong(1, rs.getLong(4));
            ResultSet subRs = subPs.executeQuery();

            if(subRs.next()) {

                Order order = new Order(
                        subRs.getLong(1),
                        subRs.getString(2),
                        subRs.getString(3),
                        subRs.getString(4)
                );
                order.addOrderItem(oi);
            }

            String query2 = "SELECT * FROM TASK WHERE ORDER_ITEM_ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(query2);
            ps2.setLong(1, oi.getId());

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                Task t = new Task();

                t.setId(rs2.getLong(1));
                t.setUser((User) new UserService().get(rs2.getLong(2)));
                t.setCreated(rs2.getTimestamp(3).toLocalDateTime());
                t.setState(ETaskState.getById(rs2.getLong(4)));
                oi.addTask(t);
                t.setDescription(rs2.getString(6));
            }
        }

        conn.commit();
        return oi;
    }

    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity entity to be updated in the database.
     */
    @Override
    public void update(IEntity entity) throws SQLException {

        if(entity instanceof OrderItem) {

            OrderItem oi = (OrderItem) entity;

            if(this.exists(oi)) {

                Connection conn = this.getConnection().getConnection();
                conn.setAutoCommit(false);

                String query = "UPDATE ORDER_ITEM SET " +
                        "PRODUCT_ID = ?, " +
                        "QUANTITY = ?, " +
                        "ORDER_ID = ? " +
                        "WHERE ID = ?";

                PreparedStatement ps = conn.prepareStatement(query);
                ps.setLong(1, oi.getProduct().getId());
                ps.setInt(2, oi.getQuantity());
                ps.setLong(3, oi.getOrder().getId());
                ps.setLong(4, oi.getId());

                ps.execute();
                conn.commit();

            } else {

                this.persist(oi);
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

        if(entity instanceof OrderItem) {

            Connection conn = this.getConnection().getConnection();
            conn.setAutoCommit(false);

            OrderItem oi = (OrderItem) entity;

            String query = "DELETE FROM TASK WHERE ORDER_ITEM_ID = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setLong(1, oi.getId());
            ps.execute();

            String query2 = "DELETE FROM ORDER_ITEM WHERE ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(query2);
            ps2.setLong(1, oi.getId());
            ps2.execute();

            conn.commit();

        } else {

            LOG.log(Level.SEVERE, "NOT SUPPORTED ENTITY!");
            throw new UnsupportedOperationException("Unsupported entity type: " + entity.getClass().getName());
        }
    }


    public List<OrderItem> getByOrdersId(Long id) throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);

        String query = "SELECT * FROM ORDER_ITEM WHERE ORDER_ID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            OrderItem oi = new OrderItem();
            oi.setId(rs.getLong(1));
            oi.setProduct((Product) new ProductService().get(rs.getLong(2)));
            oi.setQuantity(rs.getInt(3));

            Order o = (Order) new OrderService().get(rs.getLong(4));
            o.addOrderItem(oi);

            String query2 = "SELECT * FROM TASK WHERE ORDER_ITEM_ID = ?";
            PreparedStatement ps2 = conn.prepareStatement(query2);
            ps2.setLong(1, oi.getId());

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {

                Task t = new Task();

                t.setId(rs2.getLong(1));
                t.setUser((User) new UserService().get(rs2.getLong(2)));
                t.setCreated(rs2.getTimestamp(3).toLocalDateTime());
                t.setState(ETaskState.getById(rs2.getLong(4)));
                oi.addTask(t);
                t.setDescription(rs2.getString(5));
            }

            items.add(oi);
        }

        conn.commit();

        return items;
    }


    public Long getUniqueId() throws SQLException {

        Connection conn = this.getConnection().getConnection();
        conn.setAutoCommit(false);
        String query = "SELECT ORDER_ITEM_SEQ.NEXTVAL";
        ResultSet rs = conn.prepareStatement(query).executeQuery();

        if(rs.next()) {

            conn.commit();
            return rs.getLong(1);
        }

        return null;
    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
