package cz.vse.java.util.persistance.entities.tasks;


/**************************************************************
 * <p>Enumeration of ETaskState is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 09. 04. 2020
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public enum ETaskState {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    NOT_ASSIGNED ("NEZADÁNO", 1L),
    ASSIGNED ("ZADÁNO", 2L),
    CONFIRMED ("POTVRZENO", 3L),
    DONE ("VYHOTOVENO", 4L),
    NOT_DONE ("NEVYHOTOVENO", 5L)


    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/

    private String desc;
    private Long id;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    ETaskState(String desc, Long id) {

        this.desc = desc;
        this.id = id;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public static ETaskState getById(Long id) {

        for (ETaskState state : values()) {

            if(state.getId().equals(id)) {

                return state;
            }
        }

        return null;
    }

    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code desc}
     * of the instance of {@link ETaskState}
     *
     * @return the value of {@code desc}
     * @see String
     * @see ETaskState
     */
    public String getDesc() {

        return desc;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link ETaskState}
     *
     * @return the value of {@code id}
     * @see Long
     * @see ETaskState
     */
    public Long getId() {

        return id;
    }
}
