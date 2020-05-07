package cz.vse.java.util.persistance.entities.tasks;


import cz.vse.java.util.persistance.entities.OrderItem;

import java.sql.SQLException;

/**************************************************************
 * <p>The interface of ISideTaskAssigner is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 */
public interface ISideTaskAssigner {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Assigns an side task to the main {@link Task} assignment flow,
     * next to the main task (as "prepare something").</p>
     *
     * @param orderItem     which the side task should be assigned to.
     *
     * @return {@link Task} to be processed.
     *
     * @throws SQLException when the error with DB occurs
     */
    Task assign(OrderItem orderItem) throws SQLException;


    /* *****************************************************************/
    /* Default methods *************************************************/


}
