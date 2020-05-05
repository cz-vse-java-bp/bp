package cz.vse.java.admin.utils;


import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.entities.tasks.Task;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderSealer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 03. 05. 2020
 *
 *
 * @see cz.vse.java.admin.utils
 */
public class OrderSealer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Order order;

    private Long id;
    private String submitter, contact, note, state;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderSealer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderSealer(Order order) {

        this.order = order;

        if(order != null) {

            this.id = order.getId();
            this.contact = order.getContact();
            this.note = order.getNote();
            this.submitter = order.getSubmitter();

            this.state = EOrderState.DONE.getDesc();

            for (Task t : order.getTasks()) {

                if(t.getState().equals(ETaskState.NOT_DONE)) {

                    this.state = EOrderState.FAILED.getDesc();
                    break;

                } else if(!t.getState().equals(ETaskState.DONE)) {

                    this.state = EOrderState.NOT_DONE_YET.getDesc();
                }
            }
        }
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Order} formed {@code order}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code order}
     * @see Order
     * @see OrderSealer
     */
    public Order getOrder() {

        return order;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code id}
     * @see Long
     * @see OrderSealer
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code submitter}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code submitter}
     * @see String
     * @see OrderSealer
     */
    public String getSubmitter() {

        return submitter;
    }

    /**
     * Getter for {@link String} formed {@code contact}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code contact}
     * @see String
     * @see OrderSealer
     */
    public String getContact() {

        return contact;
    }

    /**
     * Getter for {@link String} formed {@code note}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code note}
     * @see String
     * @see OrderSealer
     */
    public String getNote() {

        return note;
    }

    /**
     * Getter for {@link String} formed {@code state}
     * of the instance of {@link OrderSealer}
     *
     * @return the value of {@code state}
     * @see String
     * @see OrderSealer
     */
    public String getState() {

        return state;
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
     * @see OrderSealer
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code Order} formed
     * {@code order} variable.</p>
     *
     * @param order given Order value to
     *              be set to the variable
     * @see Order
     * @see OrderSealer
     */
    public void setOrder(Order order) {

        this.order = order;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code submitter} variable.</p>
     *
     * @param submitter given String value to
     *                  be set to the variable
     * @see String
     * @see OrderSealer
     */
    public void setSubmitter(String submitter) {

        this.submitter = submitter;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code contact} variable.</p>
     *
     * @param contact given String value to
     *                be set to the variable
     * @see String
     * @see OrderSealer
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
     * @see OrderSealer
     */
    public void setNote(String note) {

        this.note = note;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code state} variable.</p>
     *
     * @param state given String value to
     *              be set to the variable
     * @see String
     * @see OrderSealer
     */
    public void setState(String state) {

        this.state = state;
    }
}
