package cz.vse.java.utils.cryptography.hashing;


/**************************************************************
 * <p>Enumeration of EHashAlgorithm is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Utils".
 * @author Vojtěch Pavlů
 * @version 25. 03. 2020
 *
 * @see cz.vse.java.utils.cryptography.hashing
 */
public enum EHashAlgorithm {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    /** Algorithm definitions */
        MD5     ("MD5"),
        SHA1    ("SHA-1"),
        SHA256  ("SHA-256"),
        SHA384  ("SHA-384"),
        SHA512  ("SHA-512");


    /* *****************************************************************/
    /* Global parameters ***********************************************/

    /** Value of the algorithm */
    private String value;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    /**
     * Private Enum constructor of the instance.
     *
     * @param value of the algorithm
     */
    private EHashAlgorithm(String value) {

        this.value = value;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code value}
     * of the instance of {@link EHashAlgorithm}
     *
     * @return the value of {@code value}
     * @see String
     * @see EHashAlgorithm
     */
    public String getValue() {

        return value;
    }
}
