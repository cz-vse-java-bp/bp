package cz.vse.java.messages;


import cz.vse.java.messages.utils.AMessage;
import cz.vse.java.messages.utils.IDataContainer;
import cz.vse.java.util.persistance.entities.Product;

import java.util.List;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductAllContainer} is used to abstractly define
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
public class ProductAllContainer extends AMessage implements IDataContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private List<Product> products;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductAllContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProductAllContainer(List<Product> products) {

        this.products = products;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Returns the content of the message.</p>
     *
     * @return the content the message contains.
     */
    @Override
    public List<Product> getContent() {

        return this.products;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
