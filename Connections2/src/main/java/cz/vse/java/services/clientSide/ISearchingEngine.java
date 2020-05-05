package cz.vse.java.services.clientSide;


import cz.vse.java.util.persistance.entities.Product;

import java.util.List;

/**************************************************************
 * <p>The interface of ISearchingEngine is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 12. 04. 2020
 *
 * @see cz.vse.java.services.clientSide
 */
public interface ISearchingEngine {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Provides searching in the {@link ProductsContainer} instance.</p>
     *
     * @param findingBy     interpretation of what should the searching engine
     *                      use for searching.
     * @return              {@link List} of {@link Product}s found by this method
     */
    List<Product> search(String findingBy);


    /* *****************************************************************/
    /* Default methods *************************************************/


}
