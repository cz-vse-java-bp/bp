package cz.vse.java.util.persistance.entities;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Product} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public class Product implements IEntity {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long id;
    private String barcode;
    private String productName;
    private String shortDesc;
    private String longDesc;
    private BigDecimal price;
    private Integer quantity;
    private String location;

    private EUnit unit;

    private String unitString;

    private List<EProductStamp> stamps;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Product class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public Product() {

        this.stamps = new ArrayList<>();
        this.unitString = "";
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


    public void addStamp(String stamp) {

        EProductStamp givenStamp = EProductStamp.getStamp(stamp);

        if(givenStamp == null) {

            LOG.log(Level.SEVERE, "Stamp of name '" + stamp + "' not found!");
            throw new IllegalArgumentException("Category not found!");
        }
        if(!this.stamps.contains(givenStamp)) {

            this.stamps.add(givenStamp);
        }
    }

    public void addStamp(EProductStamp stamp) {

        if(!this.stamps.contains(stamp)) {

            this.stamps.add(stamp);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", productName='" + productName + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", location='" + location + '\'' +
                ", unit=" + unit +
                ", stamps=" + stamps +
                '}';
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link Product}
     *
     * @return the value of {@code id}
     * @see Long
     * @see Product
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for {@link String} formed {@code barcode}
     * of the instance of {@link Product}
     *
     * @return the value of {@code barcode}
     * @see String
     * @see Product
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Getter for {@link String} formed {@code productName}
     * of the instance of {@link Product}
     *
     * @return the value of {@code productName}
     * @see String
     * @see Product
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Getter for {@link String} formed {@code shortDesc}
     * of the instance of {@link Product}
     *
     * @return the value of {@code shortDesc}
     * @see String
     * @see Product
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Getter for {@link String} formed {@code longDesc}
     * of the instance of {@link Product}
     *
     * @return the value of {@code longDesc}
     * @see String
     * @see Product
     */
    public String getLongDesc() {
        return longDesc;
    }

    /**
     * Getter for {@link BigDecimal} formed {@code price}
     * of the instance of {@link Product}
     *
     * @return the value of {@code price}
     * @see BigDecimal
     * @see Product
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Getter for {@link Integer} formed {@code quantity}
     * of the instance of {@link Product}
     *
     * @return the value of {@code quantity}
     * @see Integer
     * @see Product
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Getter for {@link EUnit} formed {@code unit}
     * of the instance of {@link Product}
     *
     * @return the value of {@code unit}
     * @see EUnit
     * @see Product
     */
    public EUnit getUnit() {
        return unit;
    }

    /**
     * Getter for {@link List<>} formed {@code stamps}
     * of the instance of {@link Product}
     *
     * @return the value of {@code stamps}
     * @see List<>
     * @see Product
     */
    public List<EProductStamp> getStamps() {

        return stamps;
    }

    /**
     * Getter for {@link String} formed {@code location}
     * of the instance of {@link Product}
     *
     * @return the value of {@code location}
     * @see String
     * @see Product
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for {@link String} formed {@code unitString}
     * of the instance of {@link Product}
     *
     * @return the value of {@code unitString}
     * @see String
     * @see Product
     */
    public String getUnitString() {

        return unitString;
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
     * @see Product
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code barcode} variable.</p>
     *
     * @param barcode given String value to
     *                be set to the variable
     * @see String
     * @see Product
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code productName} variable.</p>
     *
     * @param productName given String value to
     *                    be set to the variable
     * @see String
     * @see Product
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code shortDesc} variable.</p>
     *
     * @param shortDesc given String value to
     *                  be set to the variable
     * @see String
     * @see Product
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
     * @see Product
     */
    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    /**
     * <p>Setter for the {@code BigDecimal} formed
     * {@code price} variable.</p>
     *
     * @param price given BigDecimal value to
     *              be set to the variable
     * @see BigDecimal
     * @see Product
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * <p>Setter for the {@code Integer} formed
     * {@code quantity} variable.</p>
     *
     * @param quantity given Integer value to
     *                 be set to the variable
     * @see Integer
     * @see Product
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code location} variable.</p>
     *
     * @param location given String value to
     *                 be set to the variable
     * @see String
     * @see Product
     */
    public void setLocation(String location) {

        this.location = location;
    }

    /**
     * <p>Setter for the {@code EUnit} formed
     * {@code unit} variable.</p>
     *
     * @param unit given EUnit value to
     *             be set to the variable
     * @see EUnit
     * @see Product
     */
    public void setUnit(EUnit unit) {

        this.unit = unit;

        if(unit != null) {

            this.unitString = unit.getAbbr();
        }
    }

    /**
     * <p>Setter for the {@code EProductStamp} formed
     * {@code stamps} variable.</p>
     *
     * @param stamps given EProductStamp value to
     *               be set to the variable
     * @see List<>
     * @see Product
     */
    public void setStamps(List<EProductStamp> stamps) {
        this.stamps = stamps;
    }

}
