package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RemovePreOrderItem} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 05. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class RemovePreOrderItem extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Long productID;
    private String identificator;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RemovePreOrderItem class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RemovePreOrderItem(long productID, String identificator) {

        this.productID = productID;
        this.identificator = identificator;
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
     * @return the content the message contains. In the first index, there is
     * ID of the product to be removed from the cart, in the second is identificator
     * of the {@link cz.vse.java.util.persistance.entities.orders.PreOrder}
     */
    @Override
    public Object[] getContent() {

        return new Object[]{this.productID, this.identificator};
    }


    /* *****************************************************************/
    /* Setters *********************************************************/



}
