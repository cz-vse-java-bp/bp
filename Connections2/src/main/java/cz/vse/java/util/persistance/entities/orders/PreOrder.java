package cz.vse.java.util.persistance.entities.orders;


import cz.vse.java.util.Token;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.service.OrderItemService;
import cz.vse.java.util.persistance.service.OrderService;
import cz.vse.java.util.persistance.service.ProductService;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PreOrder} is used to abstractly define
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
public class PreOrder implements Serializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private String identificator;
    private final CopyOnWriteArrayList<PreOrderItem> preOrderItems;

    private String contact;
    private String note;
    private String submitter;

    private Token token;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PreOrder class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PreOrder() {

        this.preOrderItems = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Sets quantity of the {@link PreOrderItem}.</p>
     *
     * @param product       by it's searched
     * @param quantity      to be set the quantity to
     */
    public synchronized void addToPOI(Product product, int quantity) {

        synchronized (this.preOrderItems) {

            PreOrderItem poi = getPOI(product.getId());

            if (poi == null) {

                poi = new PreOrderItem(product, 0);
                this.preOrderItems.add(poi);
            }
            if(quantity > 0) {

                poi.add(quantity);

            } else {

                poi.remove(quantity);
            }
        }
    }


    /**
     * <p>Resets one specific {@link PreOrderItem} from this container.</p>
     *
     * @param poi           to be removed
     * @throws SQLException when there is a problem while connecting to DB
     */
    public void reset(PreOrderItem poi) throws SQLException {

        if(poi != null) {

            ProductService ps = new ProductService();

            Product orig = (Product) ps.get(poi.getProduct().getId());

            if(orig != null) {

                int origQ = orig.getQuantity();

                orig.setQuantity(origQ + poi.getQuantity());

                ps.update(orig);
            }
        }
    }


    /**
     * <p>Resets the {@link PreOrder} and tries to add all these
     * reserver products back to DB.</p>
     *
     * @throws SQLException     when error while using DB occurs
     */
    public void reset() throws SQLException {

        synchronized (this.getPreOrderItems()) {

            for (PreOrderItem poi : this.getPreOrderItems()) {

                this.reset(poi);
            }

            this.getPreOrderItems().clear();
        }
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
        return "PreOrder{" +
                "identificator='" + identificator + '\'' +
                ", preOrderItems=" + preOrderItems +
                '}';
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code identificator}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code identificator}
     * @see String
     * @see PreOrder
     */
    public String getIdentificator() {

        return identificator;
    }

    /**
     * Getter for {@link CopyOnWriteArrayList<PreOrderItem>} formed {@code preOrderItems}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code preOrderItems}
     * @see CopyOnWriteArrayList<PreOrderItem>
     * @see PreOrder
     */
    public CopyOnWriteArrayList<PreOrderItem> getPreOrderItems() {

        return preOrderItems;
    }

    /**
     * Getter for {@link Token} formed {@code token}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code token}
     * @see Token
     * @see PreOrder
     */
    public Token getToken() {

        return token;
    }

    /**
     * Getter for {@link String} formed {@code contact}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code contact}
     * @see String
     * @see PreOrder
     */
    public String getContact() {

        return contact;
    }

    /**
     * Getter for {@link String} formed {@code note}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code note}
     * @see String
     * @see PreOrder
     */
    public String getNote() {

        return note;
    }


    /**
     * <p>Returns {@link PreOrderItem}, where given {@link Long} formed
     * id corresponds with ID of the product it holds.</p>
     *
     * @param id    of the product
     *
     * @return      {@link PreOrderItem} holding product with ID like given
     *              {@code id} in the parameter, or null, when there is no such
     *              PreOrderItem.
     */
    public PreOrderItem getPOI(Long id) {

        synchronized (this.preOrderItems) {

            for (PreOrderItem poi : this.preOrderItems) {

                if (poi.isProduct(id)) {

                    return poi;
                }
            }
        }
        return null;
    }


    /**
     * <p>Generates {@link Order} from this {@link PreOrder}</p>
     *
     * @return              {@link Order} generated from
     *                      this {@link PreOrder}.
     *
     * @throws Exception    When the {@code contact} is not set.
     * @throws SQLException When the problem with DB while
     *                      getting unique ID occurs.
     */
    public Order prepareOrder() throws Exception, SQLException {

        if(this.getContact() != null) {

            if(this.note == null) {

                this.note = "";
            }
            if(submitter == null)  {

                this.submitter = "";
            }

            OrderService os = new OrderService();
            OrderItemService ois = new OrderItemService();

            Long id = os.getUniqueId();
            if(id == null) {

                throw new SQLException("ID is null!");
            }

            Order o = new Order();
            o.setNote(this.note);
            o.setContact(this.contact);
            o.setSubmitter(this.submitter);
            o.setId(id);

            for (PreOrderItem poi : this.getPreOrderItems()) {

                OrderItem oi = new OrderItem();

                Long oiId = ois.getUniqueId();

                if(oiId == null) {

                    throw new SQLException("ID is null!");
                }

                oi.setId(oiId);
                oi.setProduct(poi.getProduct());
                oi.setQuantity(poi.getQuantity());

                o.addOrderItem(oi);
            }

            return o;
        }
        throw new Exception("Not supported to have an order without contact!");
    }

    /**
     * Getter for {@link String} formed {@code submitter}
     * of the instance of {@link PreOrder}
     *
     * @return the value of {@code submitter}
     * @see String
     * @see PreOrder
     */
    public String getSubmitter() {

        return submitter;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


    /**
     * <p>Setter for the {@code String} formed
     * {@code identificator} variable.</p>
     *
     * @param identificator given String value to
     *                      be set to the variable
     * @see String
     * @see PreOrder
     */
    public void setIdentificator(String identificator) {

        this.identificator = identificator;
    }


    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see PreOrder
     */
    public void setToken(Token token) {

        this.token = token;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code contact} variable.</p>
     *
     * @param contact given String value to
     *                be set to the variable
     * @see String
     * @see PreOrder
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
     * @see PreOrder
     */
    public void setNote(String note) {

        this.note = note;
    }

    /**
     * <p>Setter for the {@code String} formed
     * {@code submitter} variable.</p>
     *
     * @param submitter given String value to
     *                  be set to the variable
     * @see String
     * @see PreOrder
     */
    public void setSubmitter(String submitter) {

        this.submitter = submitter;
    }
}
