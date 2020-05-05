package cz.vse.java.connections.utils.problemSolvers.utils;


/**************************************************************
 * <p>Enumeration of ESolveMethod is used to contain all the possible
 * and usable types (instances) of this kind.</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.connections.utils.problemSolvers.utils
 */
public enum ESolveMethod {

    /* *****************************************************************/
    /* Enumeration of elements *****************************************/

    /**
     * Closes the connection immediately
     */
    CLOSE ("Force close", "Closes the connection without sending anything to other side."),

    /**
     * Sends a {@link cz.vse.java.messages.QuitMessage} and waits for the closing of the connection
     */
    POLITE_CLOSE ("Polite close", "Closes the connection by sending Quit request."),

    /**
     * Does nothing about the problem. <b>This may cause fatal problems for the connection</b>
     */
    DN ("Do nothing", "Do nothing with the error."),

    /**
     * Repeats the operation. <p>This may cause the connection unstable because uncontrollable
     * cycling itself in the error.</p> For this reason it is allowed in just a few {@link IProblemSolver}s
     */
    REPEAT ("Repeat the operation", "Repeats the operation which threw the error.")

    ;

    /* *****************************************************************/
    /* Global parameters ***********************************************/

    /** Short description of the error-solving method */
    private String shortDesc;

    /** Long description of the error-solving method */
    private String longDesc;

    /* *****************************************************************/
    /* Constructor *****************************************************/

    /**
     * <p>Parametric constructor saving these descriptions and linking
     * them to the enum instances.</p>
     *
     * @param shortDesc     short description of the method
     * @param longDesc      long description of the method
     */
    ESolveMethod(String shortDesc, String longDesc) {

        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
    }


    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link String} formed {@code shortDesc}
     * of the instance of {@link ESolveMethod}
     *
     * @return the value of {@code shortDesc}
     * @see String
     * @see ESolveMethod
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Getter for {@link String} formed {@code longDesc}
     * of the instance of {@link ESolveMethod}
     *
     * @return the value of {@code longDesc}
     * @see String
     * @see ESolveMethod
     */
    public String getLongDesc() {
        return longDesc;
    }
}
