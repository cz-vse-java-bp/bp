package cz.vse.java.services.serverSide.config;


import cz.vse.java.services.serverSide.*;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServiceConfiguration} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 28. 04. 2020
 *
 *
 * @see cz.vse.java.services.serverSide.config
 */
public class ServiceConfiguration {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EServiceType type;

    private int port;

    private ServerConfiguration sc;

    private int numOfClients;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServiceConfiguration class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServiceConfiguration(EServiceType type,
                                int port,
                                int numOfClients,
                                ServerConfiguration sc) {

        this.type = type;
        this.port = port;
        this.numOfClients = numOfClients;
        this.sc = sc;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Creates and registers (at the {@link Server}) the service
     * defined by this instance.</p>
     *
     * <p>Chooses the possible {@link IService} by the
     * {@link EServiceType} stored in {@code type} field. When
     * no suitable is found, won't create or register anything and throws
     * {@link IllegalArgumentException}.</p>
     *
     */
    public void create() {

        switch (this.type) {

            case ORDER_MANAGEMENT: {

                Server.getInstance().addService(
                        new OrderManagement(
                                sc.getRouterIP(),
                                sc.getRouterPort(),
                                this.port,
                                this.numOfClients,
                                sc.getTsPath(),
                                sc.getTsPass(),
                                sc.getKsPath(),
                                sc.getKsPass()
                        )
                );

                break;
            }
            case TASK_SERVICE: {

                Server.getInstance().addService(
                        new TaskManagement(
                                sc.getRouterIP(),
                                sc.getRouterPort(),
                                this.port,
                                this.numOfClients,
                                sc.getTsPath(),
                                sc.getTsPass(),
                                sc.getKsPath(),
                                sc.getKsPass()
                        )
                );

                break;
            }
            case STORAGE_MANAGEMENT: {

                Server.getInstance().addService(
                        new StorageService(
                                sc.getRouterIP(),
                                sc.getRouterPort(),
                                this.port,
                                this.numOfClients,
                                sc.getTsPath(),
                                sc.getTsPass(),
                                sc.getKsPath(),
                                sc.getKsPass()
                        )
                );

                break;
            }

            default: {

                LOG.log(Level.SEVERE, "Not suitable service type: " + type.name());
                throw new IllegalArgumentException("Not suitable service type: " + type.name());
            }
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link EServiceType} formed {@code type}
     * of the instance of {@link ServiceConfiguration}
     *
     * @return the value of {@code type}
     * @see EServiceType
     * @see ServiceConfiguration
     */
    public EServiceType getType() {

        return type;
    }

    /**
     * Getter for {@link int} formed {@code port}
     * of the instance of {@link ServiceConfiguration}
     *
     * @return the value of {@code port}
     * @see int
     * @see ServiceConfiguration
     */
    public int getPort() {

        return port;
    }


    /**
     * Getter for {@link int} formed {@code numOfClients}
     * of the instance of {@link ServiceConfiguration}
     *
     * @return the value of {@code numOfClients}
     * @see int
     * @see ServiceConfiguration
     */
    public int getNumOfClients() {

        return numOfClients;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/


}
