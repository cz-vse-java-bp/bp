package cz.vse.java.util.persistance.service;


import cz.vse.java.util.persistance.entities.IEntity;

import java.sql.SQLException;
import java.util.List;

/**************************************************************
 * <p>The interface of IPersistor is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 * @see cz.vse.java.utils.persistance.service
 */
public interface IPersistor {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Saves the {@link IEntity} to the database. First, it checks if
     * the instance is not already there. When it is, it's just updated;
     * otherwise the new creation is done.</p>
     *
     * @param entity        to be persisted
     *
     * @throws SQLException when something goes wrong
     */
    void persist(IEntity entity) throws SQLException;


    /**
     * <p>Checks the existence of the {@link IEntity} in the database.</p>
     *
     * @param entity            to be checked.
     *
     * @return                  result of the test
     *
     * @throws SQLException     when something goes wrong.
     */
    boolean exists(IEntity entity) throws SQLException;


    /**
     * <p>Returns all {@link IEntity} instances from the
     * given database.</p>
     *
     * @return  all entities from the database
     */
    List<IEntity> getAll() throws SQLException;


    /**
     * <p>Returns the {@link IEntity} with this id.</p>
     *
     * @param id    {@link Long} id the entity has.
     *
     * @return      {@link IEntity} with given ID.
     */
    IEntity get(Long id) throws SQLException;


    /**
     * <p>Updates the given {@link IEntity} in the database.</p>
     *
     * @param entity    entity to be updated in the database.
     */
    void update(IEntity entity) throws SQLException;


    /**
     * <p>Removes the {@link IEntity} from the database.</p>
     *
     * @param entity            {@link IEntity} to be removed
     *
     * @throws SQLException     when there is any error.
     */
    void delete(IEntity entity) throws SQLException;
}
