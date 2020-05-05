package cz.vse.java.util.persistance.entities;


import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.util.persistance.entities.orders.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderItem} is used to abstractly define
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
public class OrderItem implements IEntity {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private Product product;
    private Integer quantity;
    private Order order;

    private List<Task> tasks;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderItem class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderItem(Long id, Product product, Integer quantity) {

        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.tasks = new ArrayList<>();
    }

    public OrderItem() {

        this.tasks = new ArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

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
        return "OrderItem{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", orderID=" + order.getId() +
                ", tasks=" + tasks +
                '}';
    }


    public void addTask(Task task) {

        boolean indicator = false;

        for (Task t : this.tasks) {

            if(t.getId().equals(task.getId())) {

                indicator = true;
            }
        }

        if(!indicator) {

            this.tasks.add(task);
        }

        task.setOrderItem(this);
    }



    public BigDecimal getPrice() {

        BigDecimal price = new BigDecimal(String.valueOf(this.getProduct().getPrice()));
        return price.multiply(BigDecimal.valueOf(this.quantity));
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link OrderItem}
     *
     * @return the value of {@code id}
     * @see Long
     * @see OrderItem
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for {@link Product} formed {@code product}
     * of the instance of {@link OrderItem}
     *
     * @return the value of {@code product}
     * @see Product
     * @see OrderItem
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Getter for {@link Integer} formed {@code quantity}
     * of the instance of {@link OrderItem}
     *
     * @return the value of {@code quantity}
     * @see Integer
     * @see OrderItem
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Getter for {@link Order} formed {@code order}
     * of the instance of {@link OrderItem}
     *
     * @return the value of {@code order}
     * @see Order
     * @see OrderItem
     */
    public Order getOrder() {

        return order;
    }

    /**
     * Getter for {@link List<>} formed {@code tasks}
     * of the instance of {@link OrderItem}
     *
     * @return the value of {@code tasks}
     * @see List<>
     * @see OrderItem
     */
    public List<Task> getTasks() {
        return tasks;
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
     * @see OrderItem
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code Product} formed
     * {@code product} variable.</p>
     *
     * @param product given Product value to
     *                be set to the variable
     * @see Product
     * @see OrderItem
     */
    public void setProduct(Product product) {

        this.product = product;
    }

    /**
     * <p>Setter for the {@code Integer} formed
     * {@code quantity} variable.</p>
     *
     * @param quantity given Integer value to
     *                 be set to the variable
     * @see Integer
     * @see OrderItem
     */
    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }

    /**
     * <p>Setter for the {@code Order} formed
     * {@code order} variable.</p>
     *
     * @param order given Order value to
     *              be set to the variable
     * @see Order
     * @see OrderItem
     */
    public void setOrder(Order order) {

        this.order = order;
    }

    /**
     * <p>Setter for the {@code List of Tasks} formed
     * {@code tasks} variable.</p>
     *
     * @param tasks given tasks values to
     *              be set to the variable
     * @see List<>
     * @see OrderItem
     */
    public void setTasks(List<Task> tasks) {

        tasks = tasks;
    }

}
