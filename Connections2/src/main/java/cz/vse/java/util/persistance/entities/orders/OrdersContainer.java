package cz.vse.java.util.persistance.entities.orders;


import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.service.OrderService;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrdersContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 12. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities.orders
 */
public class OrdersContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<Order> orders;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrdersContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Simple constructor defining the final {@link CopyOnWriteArrayList}
     * for the {@link Order}s.</p>
     */
    public OrdersContainer() {

        this.orders = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Adds all the given {@link Order}s stored in {@link List} instance.</p>
     *
     * <p>The addition is provided by {@link OrdersContainer#add(Order)} method.</p>
     *
     * <p>Also provides ability to clear the field before adding any orders - by
     * setting the parameter of {@code clearBefore} to true. When it's false,
     * it adds it by rules of the {@link OrdersContainer#add(Order)} method.</p>
     *
     * @param orders        {@link List} of {@link Order}s to be added
     * @param clearBefore   boolean value of if the field should be cleared
     *                      before adding. When true, the addition is gonna be
     *                      a bit faster (because of the definition of simple
     *                      adding method in this class implemented), but won't
     *                      let stored any of the orders that was there before
     *                      and are not in the given list.
     *
     * @see OrdersContainer#add(Order)
     * @see Order
     * @see OrderService
     * @see List
     * @see CopyOnWriteArrayList
     */
    public void addAll(List<Order> orders, boolean clearBefore) {

        synchronized (this.orders) {

            if(clearBefore) {

                this.orders.clear();
            }

            for (Order o : orders) {

                this.add(o);
            }
        }
    }


    /**
     * <p>Adds an {@link Order} instance to the field - to the container.</p>
     *
     * <p>Before adding, it's checked if it exist. When it doesn't, it
     * won't be added. After that, it's checked against all the others
     * already contained - when there is any of the contained with the same
     * id as it's provided, it's uploaded - the old one is removed and replaced
     * with the new one.</p>
     *
     * <p><b>May cause problems while no DB connection is provided.</b></p>
     *
     * @param order     to be added
     *
     * @see Order
     * @see OrderService
     * @see OrderService#exists(IEntity)
     * @see OrdersContainer#addAll(List, boolean)
     */
    public void add(Order order) {

        try {

            if(new OrderService().exists(order)) {

                Order toBeRemoved = null;

                synchronized (this.orders) {

                    for (Order o : this.orders) {

                        if (o.getId().equals(order.getId())) {

                            toBeRemoved = o;
                        }
                    }

                    if (toBeRemoved != null) {

                        LOG.log(Level.INFO, "Updating order with id={0}", order.getId());
                        this.orders.remove(order);

                    } else {

                        LOG.log(Level.FINE, "Adding new order with id={0}",  order.getId());
                    }

                    this.orders.addIfAbsent(order);
                }
            } else {

                LOG.log(Level.SEVERE, "Order does not exist");
            }
        } catch (SQLException e) {

            LOG.log(Level.SEVERE, "Connection with DB failed!");
        }
    }


    /**
     * <p>Simply removes the order from the container.</p>
     *
     * @param order to be removed
     */
    public void remove(Order order) {

        synchronized (this.orders) {

            this.orders.remove(order);
        }
    }

    /**
     * <p>Auto-update of the container using DB. Also provides "clear before"
     * procedure. It's recommended to do so everytime for preventing storing
     * data (orders) that are not in the DB anymore. But can be useful not to
     * use it sometimes.</p>
     *
     * <p>May cause errors when it's not able for connecting the DB.</p>
     *
     * @param clear             if should be the field cleared before
     *                          storing new orders in it from DB
     *
     * @throws SQLException     when there is problem with connection
     *                          to DB.
     */
    public void autoUpdate(boolean clear) throws SQLException {

        synchronized (this.orders) {

            OrderService os = new OrderService();

            if(clear) {

                this.orders.clear();
            }

            List<IEntity> orders = os.getAll();

            for (IEntity entity : orders) {

                Order order = (Order) entity;
                this.add(order);
            }
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code orders}
     * of the instance of {@link OrdersContainer}
     *
     * @return the value of {@code orders}
     * @see CopyOnWriteArrayList<>
     * @see OrdersContainer
     * @see Order
     */
    public CopyOnWriteArrayList<Order> getOrders() {

        return orders;
    }


    /**
     * <p>First of all, it updates the container. After that, it checks
     * all the {@link Order}s, their {@link OrderItem}s and {@link Task}s
     * linked to them.</p>
     *
     * @return  {@link List} of unfinished {@link Order}, ie orders theirs
     *          {@link OrderItem}'s {@link Task} are not set to state of
     *          {@link ETaskState#DONE}.
     *
     * @see Task
     * @see Order
     * @see OrderItem
     * @see ETaskState
     * @see OrdersContainer#autoUpdate(boolean)
     */
    public List<Order> getUnfinished() {

        CopyOnWriteArrayList<Order> unfinished = new CopyOnWriteArrayList<>();

        synchronized (this.orders) {

            try {

                this.autoUpdate(true);

            } catch (SQLException e) {

                LOG.log(Level.SEVERE, String.format(
                        "Not able to connect to DB: \nError code: %s\ncaused by %s",
                        Integer.toString(e.getErrorCode()), e.getMessage()));

            } catch (NullPointerException e) {

                LOG.log(Level.SEVERE, "Probably connection with DB was not initialized" +
                        " and/or the access (with address, username and password) was not granted.");

            } finally {

                for (Order order : this.orders) {

                    if(order.getOrderItems() != null) {
                        if(order.getOrderItems().size() > 0) {
                            for (OrderItem orderItem : order.getOrderItems()) {

                                if (orderItem.getTasks() != null) {

                                    if (orderItem.getTasks().size() > 0) {

                                        for (Task task : orderItem.getTasks()) {

                                            if (task != null) {

                                                if (!task.getState().equals(ETaskState.DONE)) {

                                                    unfinished.addIfAbsent(order);
                                                    break;
                                                }
                                            } else {

                                                LOG.log(Level.SEVERE, "TASK IS NULL");
                                            }
                                        }

                                    } else {

                                        LOG.log(Level.SEVERE, "ORDERITEM DOES NOT HAVE ANY TASKS! " + orderItem.getId());
                                        break;
                                    }
                                } else {

                                    LOG.log(Level.SEVERE, "ORDERITEM HAS NOT TASKS INITIALIZED! " + orderItem.getId());
                                    break;
                                }
                            }
                        } else {

                            LOG.log(Level.SEVERE, "Order does not have any orderItems");
                        }
                    } else {

                        LOG.log(Level.SEVERE, "Order with ID={0} does not have any order items", order.getId());
                    }
                }
            }
        }
        return unfinished;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/



}
