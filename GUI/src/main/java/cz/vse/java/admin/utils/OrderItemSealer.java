package cz.vse.java.admin.utils;


import cz.vse.java.util.persistance.entities.OrderItem;

import java.math.BigDecimal;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderItemSealer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 04. 05. 2020
 *
 *
 * @see cz.vse.java.admin.utils
 */
public class OrderItemSealer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private OrderItem oi;

    private Long id;
    private String productName;
    private Integer quantity;

    private BigDecimal unitPrice;
    private BigDecimal price;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderItemSealer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public OrderItemSealer(OrderItem oi) {

        this.oi = oi;

        if(oi != null) {

            this.id = oi.getId();
            this.quantity = oi.getQuantity();
            this.price = oi.getPrice();
            this.unitPrice = oi.getProduct().getPrice();

            if(oi.getProduct() != null) {

                this.productName = oi.getProduct().getProductName();
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
     * Getter for {@link OrderItem} formed {@code oi}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code oi}
     * @see OrderItem
     * @see OrderItemSealer
     */
    public OrderItem getOi() {

        return oi;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code id}
     * @see Long
     * @see OrderItemSealer
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code productName}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code productName}
     * @see String
     * @see OrderItemSealer
     */
    public String getProductName() {

        return productName;
    }

    /**
     * Getter for {@link Integer} formed {@code quantity}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code quantity}
     * @see Integer
     * @see OrderItemSealer
     */
    public Integer getQuantity() {

        return quantity;
    }

    /**
     * Getter for {@link BigDecimal} formed {@code price}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code price}
     * @see BigDecimal
     * @see OrderItemSealer
     */
    public BigDecimal getPrice() {

        return price;
    }

    /**
     * Getter for {@link BigDecimal} formed {@code unitPrice}
     * of the instance of {@link OrderItemSealer}
     *
     * @return the value of {@code unitPrice}
     * @see BigDecimal
     * @see OrderItemSealer
     */
    public BigDecimal getUnitPrice() {

        return unitPrice;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/


    /**
     * <p>Setter for the {@code OrderItem} formed
     * {@code oi} variable.</p>
     *
     * @param oi given OrderItem value to
     *           be set to the variable
     * @see OrderItem
     * @see OrderItemSealer
     */
    public void setOi(OrderItem oi) {

        this.oi = oi;
    }

    /**
     * <p>Setter for the {@code Long} formed
     * {@code id} variable.</p>
     *
     * @param id given Long value to
     *           be set to the variable
     * @see Long
     * @see OrderItemSealer
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code productName} variable.</p>
     *
     * @param productName given String value to
     *                    be set to the variable
     * @see String
     * @see OrderItemSealer
     */
    public void setProductName(String productName) {

        this.productName = productName;
    }

    /**
     * <p>Setter for the {@code Integer} formed
     * {@code quantity} variable.</p>
     *
     * @param quantity given Integer value to
     *                 be set to the variable
     * @see Integer
     * @see OrderItemSealer
     */
    public void setQuantity(Integer quantity) {

        this.quantity = quantity;
    }

    /**
     * <p>Setter for the {@code BigDecimal} formed
     * {@code price} variable.</p>
     *
     * @param price given BigDecimal value to
     *              be set to the variable
     * @see BigDecimal
     * @see OrderItemSealer
     */
    public void setPrice(BigDecimal price) {

        this.price = price;
    }

    /**
     * <p>Setter for the {@code BigDecimal} formed
     * {@code unitPrice} variable.</p>
     *
     * @param unitPrice given BigDecimal value to
     *                  be set to the variable
     * @see BigDecimal
     * @see OrderItemSealer
     */
    public void setUnitPrice(BigDecimal unitPrice) {

        this.unitPrice = unitPrice;
    }
}
