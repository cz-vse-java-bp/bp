package cz.vse.java.services.clientSide;


import cz.vse.java.util.persistance.entities.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductsContainer} is used to abstractly define
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
public class ProductsContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    /** Actual {@link Product}s container */
    private final CopyOnWriteArrayList<Product> products;

    private List<ISearchingEngine> searchingEngines;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductsContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    /**
     * <p>Non-parametric constructor just setting the final {@code products}
     * {@link CopyOnWriteArrayList} field. Then, it saves all the given
     * searching engines to it's places.</p>
     *
     * @param searchingEngines   engines providing search of products
     */
    public ProductsContainer(ISearchingEngine... searchingEngines) {

        this.products = new CopyOnWriteArrayList<>();
        this.searchingEngines = new ArrayList<>();

        this.searchingEngines.addAll(Arrays.asList(searchingEngines));
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Adds the given {@link Product} to the container. When there is one with
     * same ID, the previous is removed and replaced with given one.</p>
     *
     * @param product   to be added.
     */
    public void add(Product product) {

        synchronized (this.products) {

            Product toBeRemoved = null;

            for (Product p : this.products) {

                if(p.getId().equals(product.getId())) {

                    toBeRemoved = p;
                }
            }

            if(toBeRemoved != null) {

                this.products.remove(toBeRemoved);
            }

            products.addIfAbsent(product);
        }
    }


    /**
     * <p>Adds {@link ISearchingEngine} implementations to the container
     * of {@code searchEngines}. Using these searching scenarios can
     * be selected products corresponding given values.</p>
     *
     * @param searchingEngine   {@link ISearchingEngine} providing
     *                          the filtering of the products
     */
    public void addSearchingEngine(ISearchingEngine searchingEngine) {

        this.searchingEngines.add(searchingEngine);
    }


    /**
     * <p>Cycles through the field and adds all the given products
     * using {@link ProductsContainer#add(Product)} method.</p>
     *
     * <p>Also provides to clear the field before start of adding.
     * May positively impact speed of computing but may purge
     * some possibly useful data from this container.</p>
     *
     * @param products  {@link List} of {@link Product} to be added
     * @param clear     if should be the field cleared before adding
     */
    public void addAll(List<Product> products, boolean clear) {

        synchronized (this.products) {

            if(clear) {

                this.clear();
            }

            for (Product product : products) {

                this.add(product);
            }
        }
    }

    /**
     * <p>Removes all the {@link Product}s from the field of
     * {@code products}. Everything else stays the same.</p>
     */
    public void clear() {

        synchronized (this.products) {

            this.products.clear();
        }
    }


    /**
     * <p>Finds all the products the {@link ISearchingEngine}s
     * get.</p>
     *
     * @param keyword   word the {@link ISearchingEngine}s
     *                  are looking for
     * @return          the result {@link List} of {@link Product}s
     */
    public List<Product> findProducts(String keyword) {

        List<Product> products = new ArrayList<>();

        for (ISearchingEngine se : this.searchingEngines) {

            products.addAll(se.search(keyword));
        }

        return products;
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code products}
     * of the instance of {@link ProductsContainer}
     *
     * @return the value of {@code products}
     * @see CopyOnWriteArrayList<>
     * @see ProductsContainer
     */
    public CopyOnWriteArrayList<Product> getProducts() {

        return products;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

}
