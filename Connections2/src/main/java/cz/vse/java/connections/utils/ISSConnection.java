package cz.vse.java.connections.utils;


import cz.vse.java.services.serverSide.EServiceType;

/**************************************************************
 * <p>The interface of ISSConnection is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.connections
 */
public interface ISSConnection extends IConnection {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Returns the service type</p>
     *
     * @return  type of the service.
     */
    EServiceType getServiceType();


    /* *****************************************************************/
    /* Default methods *************************************************/


}
