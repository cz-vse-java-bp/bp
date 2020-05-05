package cz.vse.java.util.persistance;


import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;
import cz.vse.java.util.persistance.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**************************************************************
 * <p>Class Storage is Singleton by design pattern definition
 * with only one possible instance of this class.</p>
 *
 * <p>The class has <b>Thread-safe</b> getInstance() creation
 * mechanism implemented to be sure there is only one instance.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
 * Singleton at Wikipedia</a>
 */
public class Storage implements ISubject {

    /* *******************************************************/
    /* Instance variables ************************************/

    private final CopyOnWriteArrayList<Product> products;
    private final CopyOnWriteArrayList<IObserver> observers;


    /* *******************************************************/
    /* Static variables **************************************/

    /**
     * Volatile singleton container variable
     */
    private static volatile Storage singletonInstance = null;


    /**
     * Private {@link Logger} instance - Logger of the {@link Storage} class
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *******************************************************/
    /* Constructors ******************************************/

    /**
     * <p>Private Singleton design pattern constructor of
     * the only one instance of the Storage class.</p>
     */
    private Storage() {

        this.products = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
    }


    /* *******************************************************/
    /* Instance methods **************************************/


    /**
     * <p>Updates the container of {@link Product} represented
     * by {@link CopyOnWriteArrayList} stored in {@code products}
     * field.</p>
     *
     * <p>The update is done by clearing the previous version of the
     * container and refilled by given {@link List} of {@link Product}s.</p>
     *
     * @param productList   the list containing all products with their given
     *                      parameters in it.
     */
    public void updateProducts(List<Product> productList) {

        synchronized (this.products) {

            this.products.clear();
            this.products.addAll(productList);
            this.notifyObservers();
        }
    }


    public void updateProduct(Product product) {


        synchronized (this.products) {

            this.products.remove(this.getById(product.getId()));
            this.products.add(product);
            this.notifyObservers();
        }
    }


    /**
     * <p>Finds suitable instance of stored {@link Product} by it's ID.
     * <i>When there is no such product, it returns null</i></p>
     *
     * @param id    the key the finding process is provided with
     *
     * @return      {@link Product} with given ID or {@code null} when
     *              there is no product with such an ID.
     */
    public Product getById(long id) {

        synchronized (this.products) {

            for (Product product : this.products) {

                if(product.getId() == id) {

                    return product;
                }
            }
        }

        LOG.log(Level.SEVERE, "No such product with id like " + id + " found!");
        return null;
    }


    /**
     * <p>Finds products by it's {@code product name}. It's possible to
     * choose if the given String has to be the same as the product name or
     * to be just a part of it's name.</p>
     *
     * <p>For example, when the boolean variable {@code exact} is set to
     * {@code true} and {@code productName} is set to '<i>Milk</i>', the list of
     * {@link Product}s container won't contain a product with the name of
     * <i>'Milk, 1 litre'</i>. Otherwise, when the {@code exact} is set to
     * {@code false}, the {@link Product} instance is contained.</p>
     *
     * @param productName   Name of the products the algorithm tries to find
     * @param exact         When set to {@code true}, result will contain only
     *                      those products, which name is exactly same. Otherwise
     *                      the given {@code productName} just have to be contained.
     *
     * @return              {@link List} of the {@link Product} instances with such
     *                      a name.
     */
    public List<Product> findByName(String productName, boolean exact) {

        ArrayList<Product> products = new ArrayList<>();

        synchronized (this.products) {

            for (Product product : this.products) {

                if(exact) {

                    if(product.getProductName().equals(productName)) {

                        products.add(product);
                    }

                } else {

                    if(product.getProductName().contains(productName)) {

                        products.add(product);
                    }
                }
            }
        }

        return products;
    }

    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.addIfAbsent(observer);
        }
    }

    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void removeObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.remove(observer);
        }
    }

    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    @Override
    public void notifyObservers() {

        synchronized (this.observers) {

            for (IObserver observer : observers) {

                observer.update();
            }
        }
    }


    /* *******************************************************/
    /* Getters and setters ***********************************/


    /**
     * Getter for {@link CopyOnWriteArrayList} formed {@code products}
     * of the instance of {@link Storage}
     *
     * @return the value of {@code products}
     *
     * @see CopyOnWriteArrayList
     * @see Storage
     */
    public CopyOnWriteArrayList<Product> getProducts() {

        return products;
    }


    /* *******************************************************/
    /* Static methods ****************************************/

    /**
     * <p>Static Singleton method <strong>getInstance</strong>
     * for returning the only instance of the class of Storage.</p>
     *
     * <p><b>Thread-safe</b> implementation of {@code getInstance()}
     * method to prevent more instance creation while using
     * multithreading algorithm.</p>
     *
     * @return the only instance of the Storage class
     * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">
     * Singleton at Wikipedia</a>
     * @see <a href="https://en.wikipedia.org/wiki/Thread_safety">
     * Thread safety at Wikipedia</a>
     * @see Thread
     */
    public static Storage getInstance() {

        if (singletonInstance == null) {

            synchronized (Storage.class) {

                singletonInstance = new Storage();
            }
        }

        return singletonInstance;
    }


    /**
     * <p>Resets the static field of {@code singletonInstance}
     * to default state - it is set to {@code null} again.</p>
     *
     * <p><b>ALL SET DATA ARE GONNA BE REMOVED</b></p>
     */
    public static void reset() {

        Storage.getInstance().notifyObservers();
        singletonInstance = null;
    }
}
