package cz.vse.java.services.serverSide;


import java.io.Serializable;

/**************************************************************
 * <p>Enumeration of EServiceType is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 * @see cz.vse.java.services
 */
public enum EServiceType implements Serializable {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    CLIENT,
    ROUTER,
    EMPLOYEE_MANAGEMENT,
    TEXT_PRINTER,
    STORAGE_MANAGEMENT,
    TASK_SERVICE,
    ORDER_MANAGEMENT


    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/



    /* *****************************************************************/
    /* Constructor *****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    public static EServiceType getByName(String name) {

        EServiceType type = null;

        for (EServiceType t : values()) {

            if(t.name().equals(name)) {

                type = t;
                break;
            }
        }

        return type;
    }

    /* *****************************************************************/
    /* Getters *********************************************************/


}
