package cz.vse.java.util.database;


/**************************************************************
 * <p>Enumeration of EDBUse is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 * @see cz.vse.java.utils.database
 */
public enum EDBUse {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    USER_AUTHENTICATION,
    FINGERPRINT_AUTHENTICATION,
    EMPLOYEE_MANAGEMENT,
    STORAGE_MANAGEMENT,
    TASK_MANAGEMENT,
    ORDERS_MANAGEMENT


    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/



    /* *****************************************************************/
    /* Constructor *****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    public static EDBUse getByName(String name) {

        EDBUse use = null;

        for (EDBUse u : values()) {

            if(name.equals(u.name())) {

                use = u;
                break;
            }
        }

        return use;
    }

    /* *****************************************************************/
    /* Getters *********************************************************/


}
