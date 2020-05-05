package cz.vse.java.util.persistance.entities;


import java.io.Serializable;

/**************************************************************
 * <p>The interface of IEntity is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public interface IEntity extends Serializable {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Checks the validity of the given entity.</p>
     * <p>Mostly checks null values.</p>
     *
     * @return  result, if the entity is valid against
     *          schema.
     */
    boolean check();

    /* *****************************************************************/
    /* Default methods *************************************************/


}
