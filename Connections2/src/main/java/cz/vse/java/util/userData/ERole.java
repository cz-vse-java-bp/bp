package cz.vse.java.util.userData;


/**************************************************************
 * <p>Enumeration of ERole is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 * @see cz.vse.java.utils.userData
 */
public enum ERole {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    ADMIN ("ADMIN", 1L),
    SUPERVISOR ("SUPERVISOR", 2L),
    SKLADNIK ("SKLADNIK", 3L),
    ZAKAZNIK ("ZÁKAZNÍK", 4L)


    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/

    private String roleName;
    private Long id;

    /* *****************************************************************/
    /* Constructor *****************************************************/


    /**
     * <p>Constructor used just for filling the Enum type
     * instances' values.</p>
     * @param roleName  name the role has
     * @param id        id the role has in DB
     */
    ERole(String roleName, Long id) {

        this.roleName = roleName;
        this.id = id;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Finds the role by it's ID. When nothing found, returns null.</p>
     *
     * @param id    by it's looking for
     *
     * @return      {@link ERole} with given ID or null,
     *              when nothing suitable found
     */
    public static ERole getById(Long id) {

        ERole role = null;

        for (ERole r : values()) {

            if(r.getId().equals(id)) {

                role = r;
            }
        }

        return role;
    }


    /**
     * <p>Returns {@link ERole} by given name.</p>
     *
     * @param name  to be searched by
     *
     * @return      ERole corresponding this name.
     */
    public static ERole getByName(String name) {

        ERole role = null;

        for (ERole r : values()) {

            if(r.getRoleName().equals(name)) {

                role = r;
            }
        }

        return role;
    }

    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code roleName}
     * of the instance of {@link ERole}
     *
     * @return the value of {@code roleName}
     * @see String
     * @see ERole
     */
    public String getRoleName() {

        return roleName;
    }

    /**
     * Getter for {@link Long} formed {@code id}
     * of the instance of {@link ERole}
     *
     * @return the value of {@code id}
     * @see Long
     * @see ERole
     */
    public Long getId() {

        return id;
    }
}
