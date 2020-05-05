package cz.vse.java.services.serverSide;


/**************************************************************
 * <p>The interface of IService is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 * @see cz.vse.java.services
 */
public interface IService {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Returns the port the clients can connect to the service at.</p>
     *
     * @return  integer representation of port
     */
    int getClientsPort();


    /**
     * <p>Returns the IP address this service is working at.</p>
     *
     * @return  {@link String} representation of the address
     *          the clients can connect with this service at.
     */
    String getClientsIP();


    /**
     * <p>Gets the service type represented by {@link EServiceType} enum instance.</p>
     *
     * @return  {@link EServiceType} instance.
     */
    EServiceType getServiceType();


    /* *****************************************************************/
    /* Default methods *************************************************/


}
