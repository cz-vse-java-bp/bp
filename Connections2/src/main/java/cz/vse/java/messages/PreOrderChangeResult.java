package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.PreOrder;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code PreOrderChangeResult} is used to abstractly define
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
public class PreOrderChangeResult extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Product p;
    private int quantity;
    private String identificator;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link PreOrderChangeResult class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public PreOrderChangeResult(Product p, int quantity, String identificator) {

        this.p = p;
        this.quantity = quantity;
        this.identificator = identificator;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains - {@link Object}[],
     * where first index contains product, the second one contains
     * quantity of the product and the third has
     * identificator of the {@link PreOrder}.
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.p, this.quantity, this.identificator};
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
