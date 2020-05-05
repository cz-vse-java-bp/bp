package cz.vse.java.admin.utils;


/**************************************************************
 * <p>Enumeration of EOrderState is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "GUI".
 * @author Vojtěch Pavlů
 * @version 03. 05. 2020
 *
 * @see cz.vse.java.admin.utils
 */
public enum EOrderState {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    NOT_DONE_YET ("Nedokončeno"),
    DONE ("Dokončeno"),
    FAILED ("Chyba")



    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/

    private String desc;

    /* *****************************************************************/
    /* Constructor *****************************************************/


    EOrderState(String desc) {

        this.desc = desc;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code desc}
     * of the instance of {@link EOrderState}
     *
     * @return the value of {@code desc}
     * @see String
     * @see EOrderState
     */
    public String getDesc() {

        return desc;
    }
}
