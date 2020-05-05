package cz.vse.java.admin.utils;


import cz.vse.java.util.persistance.entities.Product;

import java.math.BigDecimal;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductSealer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 17. 04. 2020
 *
 *
 * @see cz.vse.java.admin.utils
 */
public class ProductSealer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private String productName;
    private String barcode;
    private String shortDesc;
    private String longDesc;
    private int quantity;
    private BigDecimal price;
    private String unit;
    private String location;

    private Product product;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductSealer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProductSealer(Product product) {

        this.product = product;

        this.id = product.getId();
        this.barcode = product.getBarcode();
        this.location = product.getLocation();
        this.productName = product.getProductName();
        this.longDesc = product.getLongDesc();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.shortDesc = product.getShortDesc();
        this.unit = product.getUnit().getDesc();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Overriden method of <strong>toString</strong>.</p>
     *
     * @return {@link String} interpretation of the ProductSealer instance.
     */
    @Override
    public String toString() {

        return "ProductSealer.toString() - NOT DEFINED YET!";
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code id}
     * @see Long
     * @see ProductSealer
     */
    public Long getId() {

        return id;
    }

    /**
     * Getter for {@link String} formed {@code productName}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code productName}
     * @see String
     * @see ProductSealer
     */
    public String getProductName() {

        return productName;
    }

    /**
     * Getter for {@link String} formed {@code barcode}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code barcode}
     * @see String
     * @see ProductSealer
     */
    public String getBarcode() {

        return barcode;
    }

    /**
     * Getter for {@link String} formed {@code shortDesc}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code shortDesc}
     * @see String
     * @see ProductSealer
     */
    public String getShortDesc() {

        return shortDesc;
    }

    /**
     * Getter for {@link String} formed {@code longDesc}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code longDesc}
     * @see String
     * @see ProductSealer
     */
    public String getLongDesc() {

        return longDesc;
    }

    /**
     * Getter for {@link int} formed {@code quantity}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code quantity}
     * @see int
     * @see ProductSealer
     */
    public int getQuantity() {

        return quantity;
    }

    /**
     * Getter for {@link BigDecimal} formed {@code price}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code price}
     * @see BigDecimal
     * @see ProductSealer
     */
    public BigDecimal getPrice() {

        return price;
    }

    /**
     * Getter for {@link String} formed {@code unit}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code unit}
     * @see String
     * @see ProductSealer
     */
    public String getUnit() {

        return unit;
    }

    /**
     * Getter for {@link String} formed {@code location}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code location}
     * @see String
     * @see ProductSealer
     */
    public String getLocation() {

        return location;
    }

    /**
     * Getter for {@link Product} formed {@code product}
     * of the instance of {@link ProductSealer}
     *
     * @return the value of {@code product}
     * @see Product
     * @see ProductSealer
     */
    public Product getProduct() {

        return product;
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
     * @see ProductSealer
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
     * @see ProductSealer
     */
    public void setProductName(String productName) {

        this.productName = productName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code barcode} variable.</p>
     *
     * @param barcode given String value to
     *                be set to the variable
     * @see String
     * @see ProductSealer
     */
    public void setBarcode(String barcode) {

        this.barcode = barcode;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code shortDesc} variable.</p>
     *
     * @param shortDesc given String value to
     *                  be set to the variable
     * @see String
     * @see ProductSealer
     */
    public void setShortDesc(String shortDesc) {

        this.shortDesc = shortDesc;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code longDesc} variable.</p>
     *
     * @param longDesc given String value to
     *                 be set to the variable
     * @see String
     * @see ProductSealer
     */
    public void setLongDesc(String longDesc) {

        this.longDesc = longDesc;
    }

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code quantity} variable.</p>
     *
     * @param quantity given $field.typeName value to
     *                 be set to the variable
     * @see int
     * @see ProductSealer
     */
    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    /**
     * <p>Setter for the {@code BigDecimal} formed
     * {@code price} variable.</p>
     *
     * @param price given BigDecimal value to
     *              be set to the variable
     * @see BigDecimal
     * @see ProductSealer
     */
    public void setPrice(BigDecimal price) {

        this.price = price;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code unit} variable.</p>
     *
     * @param unit given String value to
     *             be set to the variable
     * @see String
     * @see ProductSealer
     */
    public void setUnit(String unit) {

        this.unit = unit;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code location} variable.</p>
     *
     * @param location given String value to
     *                 be set to the variable
     * @see String
     * @see ProductSealer
     */
    public void setLocation(String location) {

        this.location = location;
    }

    /**
     * <p>Setter for the {@code Product} formed
     * {@code product} variable.</p>
     *
     * @param product given Product value to
     *                be set to the variable
     * @see Product
     * @see ProductSealer
     */
    public void setProduct(Product product) {

        this.product = product;
    }
}
