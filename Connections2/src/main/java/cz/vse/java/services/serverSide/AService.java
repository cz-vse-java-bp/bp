package cz.vse.java.services.serverSide;


import cz.vse.java.util.observerDP.ISubject;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AService} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.services
 */
public abstract class AService implements Runnable, IService, ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EServiceType type;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AService(EServiceType type) {

        this.type = type;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link EServiceType} formed {@code type}
     * of the instance of {@link AService}
     *
     * @return the value of {@code type}
     * @see EServiceType
     * @see AService
     */
    public EServiceType getServiceType() {

        return type;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
