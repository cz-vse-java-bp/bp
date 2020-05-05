package cz.vse.java.authentication;


/**************************************************************
 * <p>Enumeration of EAuthenticationScenarioType is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 * <p>Contains possible authentication methods with their short descriptions.</p>
 *
 * <p><b>NOT EVERY ELEMENT IN THE NETWORK IS ABLE TO WORK WITH ALL OF THEM!</b></p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.authentication
 */
public enum EAuthenticationScenarioType {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    USER_VIA_PASSWORD ("User password authentication"),
    IP_VIA_DB ("IP address check using database"),
    FP_VIA_DB ("Device's FingerPrint check using database"),
    TOKEN_CHECK ("Validation of the given token against tokens stored at connection manager instance");




    /* *****************************************************************/
    /* Global parameters ***********************************************/

    /** <p>Description of the method</p> */
    private String description;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    /**
     * <p>Self-initializing constructor of this instances.</p>
     *
     * @param description   {@link String}-formed description of the
     *                      authentication method
     */
    EAuthenticationScenarioType(String description) {

        this.description = description;
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code description}
     * of the instance of {@link EAuthenticationScenarioType}
     *
     * @return the value of {@code description}
     * @see String
     * @see EAuthenticationScenarioType
     */
    public String getDescription() {

        return description;
    }
}
