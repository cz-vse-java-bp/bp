package cz.vse.java.connections.utils.problemSolvers.utils;


import cz.vse.java.messages.utils.EErrorType;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code AProblemSolver} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.problemSolvers
 */
public abstract class AProblemSolver implements IProblemSolver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final EErrorType solving;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link AProblemSolver class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public AProblemSolver(EErrorType solving) {

        this.solving = solving;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    protected boolean check(EErrorType type) {

        return this.solving.equals(type);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link EErrorType} formed {@code solving}
     * of the instance of {@link AProblemSolver}
     *
     * @return the value of {@code solving}
     * @see EErrorType
     * @see AProblemSolver
     */
    public EErrorType getSolving() {

        return solving;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
