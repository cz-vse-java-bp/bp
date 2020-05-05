package cz.vse.java.util.persistance.entities;


/**************************************************************
 * <p>Enumeration of EProductStamp is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 07. 04. 2020
 *
 * @see cz.vse.java.utils.persistance.entities
 */
public enum EProductStamp {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    MRAZENE("Mražené", 1L),
	CHLAZENE("Chlazené", 2L),
    PULTOVE("Pultové", 3L),
	CERSTVE("Čerstvé", 4L),
	TRVANLIVE("Trvanlivé", 5L),
	DROGERIE("Drogerie", 6L),
	BIO("BIO", 7L),
	ALKO("Alkoholické", 8L),
	TABAK("Tabákový výrobek", 9L),
	DOMACNOST("Domácnost", 10L),
	CISTICI_PROSTREDEK("Čistící prostředek", 11L),
	ZABAVA("Zábava", 12L),
	OVOCE_A_ZELENINA("Ovoce a zelenina", 13L),
	DOPLNKY_STRAVY("Doplňky stravy", 14L),
    KNIHY("Knihy", 15L),



    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/

    private String name;
    private Long id;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    EProductStamp(String name, Long id) {

        this.name = name;
        this.id = id;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /* *****************************************************************/
    /* Static methods **************************************************/

    public static EProductStamp getStamp(String name) {

        for (EProductStamp stamp : values()) {

            if(stamp.getName().equals(name)) {

                return stamp;
            }
        }

        return null;
    }


    public static EProductStamp getById(Long id) {

        for (EProductStamp stamp : values()) {

            if(stamp.getId().equals(id)) {

                return stamp;
            }
        }

        return null;
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code name}
     * of the instance of {@link EProductStamp}
     *
     * @return the value of {@code name}
     * @see String
     * @see EProductStamp
     */
    public String getName() {

        return name;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link EProductStamp}
     *
     * @return the value of {@code id}
     * @see Long
     * @see EProductStamp
     */
    public Long getId() {

        return id;
    }
}
