package cz.vse.java.util.persistance.entities.tasks;


import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.User;
import cz.vse.java.util.persistance.entities.*;

import java.time.LocalDateTime;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Task} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public class Task implements IEntity {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private User user;
    private LocalDateTime created;
    private ETaskState state;
    private OrderItem orderItem;
    private String description;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Task class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public Task(Long id,
                User user,
                LocalDateTime created,
                ETaskState state,
                OrderItem orderItem,
                String description) {

        this.id = id;
        this.user = user;
        this.created = created;
        this.orderItem = orderItem;
        this.state = state;
        this.description = description;
    }

    public Task() {}

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
        return "Task{" +
                "id=" + id +
                ", user=" + user +
                ", created=" + created +
                ", state=" + state +
                ", orderItemID=" + orderItem.getId() + //TODO - muze vracet null u orderItem - TaskManagement!!!!
                ", description='" + description + '\'' +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link Task}
     *
     * @return the value of {@code id}
     * @see Long
     * @see Task
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link User} formed {@code user}
     * of the instance of {@link Task}
     *
     * @return the value of {@code user}
     * @see User
     * @see Task
     */
    public User getUser() {

        return user;
    }

    /**
     * Getter for {@link LocalDateTime} formed {@code created}
     * of the instance of {@link Task}
     *
     * @return the value of {@code created}
     * @see LocalDateTime
     * @see Task
     */
    public LocalDateTime getCreated() {

        return created;
    }


    /**
     * Getter for {@link ETaskState} formed {@code state}
     * of the instance of {@link Task}
     *
     * @return the value of {@code state}
     * @see ETaskState
     * @see Task
     */
    public ETaskState getState() {

        return state;
    }

    /**
     * Getter for {@link String} formed {@code description}
     * of the instance of {@link Task}
     *
     * @return the value of {@code description}
     * @see String
     * @see Task
     */
    public String getDescription() {

        return description;
    }

    /**
     * Getter for {@link OrderItem} formed {@code orderItem}
     * of the instance of {@link Task}
     *
     * @return the value of {@code orderItem}
     * @see OrderItem
     * @see Task
     */
    public OrderItem getOrderItem() {

        return orderItem;
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
     * @see Task
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code User} formed
     * {@code user} variable.</p>
     *
     * @param user given User value to
     *             be set to the variable
     * @see User
     * @see Task
     */
    public void setUser(User user) {

        this.user = user;
    }

    /**
     * <p>Setter for the {@code LocalDateTime} formed
     * {@code created} variable.</p>
     *
     * @param created given LocalDateTime value to
     *                be set to the variable
     * @see LocalDateTime
     * @see Task
     */
    public void setCreated(LocalDateTime created) {

        this.created = created;
    }


    /**
     * <p>Setter for the {@code ETaskState} formed
     * {@code state} variable.</p>
     *
     * @param state given ETaskState value to
     *              be set to the variable
     * @see ETaskState
     * @see Task
     */
    public void setState(ETaskState state) {

        this.state = state;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code description} variable.</p>
     *
     * @param description given String value to
     *                    be set to the variable
     * @see String
     * @see Task
     */
    public void setDescription(String description) {

        this.description = description;
    }


    /**
     * <p>Setter for the {@code OrderItem} formed
     * {@code orderItem} variable.</p>
     *
     * @param orderItem given OrderItem value to
     *                  be set to the variable
     * @see OrderItem
     * @see Task
     */
    public void setOrderItem(OrderItem orderItem) {

        this.orderItem = orderItem;
    }
}
