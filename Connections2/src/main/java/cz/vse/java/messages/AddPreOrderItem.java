package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.PreOrder;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AddPreOrderItem} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class AddPreOrderItem extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Product product;
    private Integer quantity;
    private String identificator;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AddPreOrderItem class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AddPreOrderItem(Product product, int quantity, String ident) {

        this.product = product;
        this.quantity = quantity;
        this.identificator = ident;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * <p>The returned value is {@link Object} array,
     * where in the indexes are stored data like this:</p>
     *
     * <p>0) {@link Product}</p>
     * <p>1) integer-formed quantity</p>
     * <p>2) ident of the {@link PreOrder}</p>
     *
     * @return the content the message contains.
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.product, this.quantity, this.identificator};
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/

}
