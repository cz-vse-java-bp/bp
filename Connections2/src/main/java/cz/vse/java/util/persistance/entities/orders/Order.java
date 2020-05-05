package cz.vse.java.util.persistance.entities.orders;


import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.entities.IEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Order} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 11. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public class Order implements IEntity {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private String submitter;
    private String contact;
    private String note;

    private List<OrderItem> orderItems;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Order class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Parametric cosnstructor for the instance of {@link Order}</p>
     *
     * @param id            to be set to order. Should correspond with
     *                      the ID in DB. Else, it may cause some errors.
     * @param submitter     submitter of the order
     * @param contact       contact to the order submitter. Can be email,
     *                      telephone number or any other contact. Should
     *                      not be longer than 50 characters, otherwise it
     *                      may cause errors with DB. <i>By default design
     *                      implementation</i>
     * @param note          submitter added to the order
     */
    public Order(Long id, String submitter, String contact, String note) {

        this.id = id;
        this.submitter = submitter;
        this.contact = contact;
        this.note = note;

        this.orderItems = new ArrayList<>();
    }


    /**
     * <p>Non-parametric order just for initializing the field of
     * {@link OrderItem} instances in to {@link ArrayList}.</p>
     */
    public Order() {

        this.orderItems = new ArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", submitter='" + submitter + '\'' +
                ", orderItems=" + orderItems +
                ", contact='" + contact + '\'' +
                ", note='" + note + '\'' +
                '}';
    }



    /**
     * <p>Checks the validity of the given entity.</p>
     * <p>Mostly checks null values.</p>
     *
     * @return result, if the entity is valid against
     * schema.
     */
    @Override
    public boolean check() {

        return false;
    }


    /**
     * <p>Adds the {@link OrderItem} instance to this order.
     * At the same time, it sets the reference to {@link Order}
     * in OrderItem to this instance for double way connectivity.</p>
     *
     * <p>When there is order with same ID already, it's gonna be replaced.</p>
     *
     * @param orderItem     {@link OrderItem} this {@link Order} should
     *                      contain
     */
    public void addOrderItem(OrderItem orderItem) {

        OrderItem toBeRemoved = null;

        for (OrderItem oi : this.orderItems) {

            if(oi.getId().equals(orderItem.getId())) {

                toBeRemoved = oi;
                break;
            }
        }

        if(toBeRemoved != null) {

            this.orderItems.remove(toBeRemoved);
        }

        this.orderItems.add(orderItem);

        orderItem.setOrder(this);
    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /**
     * <p>Counts the price for the whole order.</p>
     *
     * @return  {@link BigDecimal}-formed price for the product.
     */
    public BigDecimal getPrice() {

        BigDecimal price = new BigDecimal(0);

        for (OrderItem oi : this.orderItems) {

            price = price.add(oi.getPrice());
        }

        return price;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link Order}
     *
     * @return the value of {@code id}
     * @see Long
     * @see Order
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code submitter}
     * of the instance of {@link Order}
     *
     * @return the value of {@code submitter}
     * @see String
     * @see Order
     */
    public String getSubmitter() {

        return submitter;
    }

    /**
     * Getter for {@link List<>} formed {@code orderItems}
     * of the instance of {@link Order}
     *
     * @return the value of {@code orderItems}
     * @see List<>
     * @see Order
     */
    public List<OrderItem> getOrderItems() {

        return orderItems;
    }

    /**
     * Getter for {@link String} formed {@code contact}
     * of the instance of {@link Order}
     *
     * @return the value of {@code contact}
     * @see String
     * @see Order
     */
    public String getContact() {

        return contact;
    }

    /**
     * Getter for {@link String} formed {@code note}
     * of the instance of {@link Order}
     *
     * @return the value of {@code note}
     * @see String
     * @see Order
     */
    public String getNote() {

        return note;
    }


    /**
     * <p>Returns all the {@link Task}s from all the {@link OrderItem}s</p>
     *
     * @return  {@link List} of {@link Task} assigned to this order.
     */
    public List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();

        for (OrderItem oi : this.orderItems) {

            tasks.addAll(oi.getTasks());
        }

        return tasks;
    }


    /**
     * <p>Returns all the {@link OrderItem}s of this order
     * having no {@link Task} assigned.</p>
     *
     * @return  {@link List} of {@link OrderItem}, where there
     *          are no {@link Task} assigned to
     */
    public List<OrderItem> getOrderItemsWithoutTasks() {

        List<OrderItem> ois = new ArrayList<>();

        for (OrderItem oi : this.orderItems) {

            if(oi.getTasks().size() == 0) {

                ois.add(oi);
            }
        }

        return ois;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Long} formed
     * {@code id} variable.</p>
     *
     * @param id given Long value to
     *           be set to the variable
     * @see Long
     * @see Order
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code submitter} variable.</p>
     *
     * @param submitter given String value to
     *                  be set to the variable
     * @see String
     * @see Order
     */
    public void setSubmitter(String submitter) {

        this.submitter = submitter;
    }

    /**
     * <p>Setter for the {@link List}-formed
     * {@code orderItems} variable.</p>
     *
     * @param orderItems given OrderItem> value to
     *                   be set to the variable
     * @see List<>
     * @see Order
     */
    public void setOrderItems(List<OrderItem> orderItems) {

        this.orderItems = orderItems;

        for (OrderItem oi : orderItems) {

            oi.setOrder(this);
        }
    }


    /**
     * <p>Setter for the {@code String} formed
     * {@code contact} variable.</p>
     *
     * @param contact given String value to
     *                be set to the variable
     * @see String
     * @see Order
     */
    public void setContact(String contact) {

        this.contact = contact;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code note} variable.</p>
     *
     * @param note given String value to
     *             be set to the variable
     * @see String
     * @see Order
     */
    public void setNote(String note) {

        this.note = note;
    }
}
