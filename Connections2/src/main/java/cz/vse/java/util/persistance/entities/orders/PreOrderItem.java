package cz.vse.java.util.persistance.entities.orders;


import cz.vse.java.util.persistance.entities.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PreOrderItem} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities.orders
 */
public class PreOrderItem implements Serializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Product product;
    private int quantity;

    private String productName;
    private BigDecimal price;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PreOrderItem class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PreOrderItem(Product product, int quantity) {

        this.product = product;
        this.quantity = quantity;

        this.productName = product.getProductName();
        this.price = product.getPrice();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public void add(int quantity) {

        this.quantity += quantity;

        this.price = product.getPrice();
    }

    public void remove(int quantity) {

        if(quantity < this.quantity) {

            this.quantity -= quantity;

        } else {

            this.quantity = 0;
            LOG.log(Level.SEVERE, "Not possible to have reserved " +
                    "number of products below number of 0!");
        }
    }


    public boolean isProduct(Long id) {

        return this.product.getId().equals(id);
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
        return "PreOrderItem{" +
                "productName=" + product.getProductName() +
                ", quantity=" + quantity +
                '}';
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Product} formed {@code product}
     * of the instance of {@link PreOrderItem}
     *
     * @return the value of {@code product}
     * @see Product
     * @see PreOrderItem
     */
    public Product getProduct() {

        return product;
    }

    /**
     * Getter for {@link int} formed {@code quantity}
     * of the instance of {@link PreOrderItem}
     *
     * @return the value of {@code quantity}
     * @see int
     * @see PreOrderItem
     */
    public int getQuantity() {

        return quantity;
    }

    /**
     * Getter for {@link String} formed {@code productName}
     * of the instance of {@link PreOrderItem}
     *
     * @return the value of {@code productName}
     * @see String
     * @see PreOrderItem
     */
    public String getProductName() {

        return productName;
    }

    /**
     * Getter for {@link BigDecimal} formed {@code price}
     * of the instance of {@link PreOrderItem}
     *
     * @return the value of {@code price}
     * @see BigDecimal
     * @see PreOrderItem
     */
    public BigDecimal getPrice() {

        return price;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code int} formed
     * {@code quantity} variable.</p>
     *
     * @param quantity given int value to
     *                 be set to the variable
     * @see int
     * @see PreOrderItem
     */
    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }
}
