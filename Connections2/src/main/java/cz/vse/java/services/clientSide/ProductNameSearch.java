package cz.vse.java.services.clientSide;


import cz.vse.java.util.persistance.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductNameSearch} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 12. 04. 2020
 *
 *
 * @see cz.vse.java.services.clientSide
 */
public class ProductNameSearch implements ISearchingEngine {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private boolean exactSearch;
    private ProductsContainer container;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductNameSearch class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Sets the container to be searching in. Then it sets the boolean value
     * of {@code exactSearch} - it defines if the value has to be same exactly to be
     * the product name evaluated as possibly looked for.</p>
     *
     * @param container     the container of the products
     * @param exactSearch   if the word match has to be 100%,
     *                      or just containing one another is wanted
     */
    public ProductNameSearch(ProductsContainer container, boolean exactSearch) {

        this.exactSearch = exactSearch;
        this.container = container;

        this.container.addSearchingEngine(this);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Provides searching in the {@link ProductsContainer} instance.</p>
     *
     * <p>This kind of searcher provides going through all the given
     * {@link Product}s from the product container and picks all those
     * with corresponding name.</p>
     *
     * @param findingBy interpretation of what should the searching engine
     *                  use for searching.
     *
     * @return {@link List} of {@link Product}s found by this method
     */
    @Override
    public List<Product> search(String findingBy) {

        List<Product> products = new ArrayList<>();

        for (Product product : this.container.getProducts()) {

            if(this.exactSearch) {

                if(product.getProductName().equals(findingBy)) {

                    products.add(product);
                }

            } else {

                if(product.getProductName().contains(findingBy) ||
                        findingBy.contains(product.getProductName())) {

                    products.add(product);
                }
            }
        }

        LOG.log(Level.INFO,
                String.format("Found %s products by ProductNameSearch with %s.",
                        Integer.toString(products.size()),
                        Boolean.toString(exactSearch)
                )
        );

        return products;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for boolean formed {@code exactSearch}
     * of the instance of {@link ProductNameSearch}
     *
     * @return the value of {@code exactSearch}
     * @see ProductNameSearch
     */
    public boolean isExactSearch() {

        return exactSearch;
    }

    /**
     * Getter for {@link ProductsContainer} formed {@code container}
     * of the instance of {@link ProductNameSearch}
     *
     * @return the value of {@code container}
     * @see ProductsContainer
     * @see ProductNameSearch
     */
    public ProductsContainer getContainer() {

        return container;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code boolean} formed
     * {@code exactSearch} variable.</p>
     *
     * @param exactSearch given boolean value to
     *                    be set to the variable
     * @see ProductNameSearch
     */
    public void setExactSearch(boolean exactSearch) {

        this.exactSearch = exactSearch;
    }
}
