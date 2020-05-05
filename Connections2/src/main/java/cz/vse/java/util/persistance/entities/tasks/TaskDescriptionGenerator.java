package cz.vse.java.util.persistance.entities.tasks;


import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.Product;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TaskDescriptionGenerator} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 13. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities.tasks
 */
public class TaskDescriptionGenerator {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TaskDescriptionGenerator class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String PROBLEM_PHRASE =
            "V případě problémů či nesrovnalostí kontaktuj vedoucího směny.";

    private static final String PRODUCT_PREPARATION =
            "Připrav %d %s produktu '%s' ze skladového místa %s. "
                    + PROBLEM_PHRASE;;

    private static final String CHECK_PRODUCTS =
            "Zkontroluj produkty '%s' ze skladového místa %s, že je jich %d. " +
                    PROBLEM_PHRASE;

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/

    public static String getProductPreparation(Product product, OrderItem orderItem) {

        return String.format(
                PRODUCT_PREPARATION,
                orderItem.getQuantity(),
                product.getUnit().getAbbr(),
                product.getProductName(),
                product.getLocation()
        );
    }

    public static String getCheckProduct(Product product) {

        return String.format(
                CHECK_PRODUCTS,
                product.getProductName(),
                product.getLocation(),
                product.getQuantity()
        );
    }

    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
