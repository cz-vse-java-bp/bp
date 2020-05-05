package cz.vse.java.util.persistance.entities;


/**************************************************************
 * <p>Enumeration of EUnit is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public enum EUnit {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    PIECE ("kus", "ks", 1L),
    KG ("kilogram", "kg", 2L),
    GRAM ("gram", "g", 3L),
    LITER ("litr", "l", 4L),



    ;




    /* *****************************************************************/
    /* Global parameters ***********************************************/

    private String desc;
    private String abbr;
    private Long id;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    EUnit(String desc, String abbr, long id) {

        this.desc = desc;
        this.abbr = abbr;
        this.id = id;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public static EUnit getUnit(String name) {

        for (EUnit unit : values()) {

            if(unit.getDesc().equals(name)) {

                return unit;
            }
        }

        return null;
    }


    public static EUnit getById(Long id) {

        for (EUnit unit : values()) {

            if(unit.getId().equals(id)) {

                return unit;
            }
        }

        return null;
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code desc}
     * of the instance of {@link EUnit}
     *
     * @return the value of {@code desc}
     * @see String
     * @see EUnit
     */
    public String getDesc() {

        return desc;
    }

    /**
     * Getter for {@link String} formed {@code abbr}
     * of the instance of {@link EUnit}
     *
     * @return the value of {@code abbr}
     * @see String
     * @see EUnit
     */
    public String getAbbr() {

        return abbr;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link EUnit}
     *
     * @return the value of {@code id}
     * @see Long
     * @see EUnit
     */
    public Long getId() {
        return id;
    }
}
