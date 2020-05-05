package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.Product;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ChangeProductQuantity} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.messages
 */
public class ChangeProductQuantity extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private int numberChange;
    private long productID;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ChangeProductQuantity class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ChangeProductQuantity(int numberChange, Long productID) {

        this.numberChange = numberChange;
        this.productID = productID;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * <p>First cell of the array contains the id of the
     * {@link Product} instance
     * persisted in DB. The value in index n. 1 is the change of the
     * quantity. Positives adds value, negatives subtracts.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public long[] getContent() {

        return new long[]{this.productID, this.numberChange};
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
