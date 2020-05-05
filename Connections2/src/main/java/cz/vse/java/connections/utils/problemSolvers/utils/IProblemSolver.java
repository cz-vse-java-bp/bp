package cz.vse.java.connections.utils.problemSolvers.utils;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.EErrorType;

/**************************************************************
 * <p>The interface of IProblemSolver is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 * @see cz.vse.java.connections.utils.problemSolvers
 */
public interface IProblemSolver {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/

    /**
     * <p>Method responsible for problem solving.</p>
     *
     * @param connection    the solver of the error
     * @param type          type of error
     *
     * @return  if the given type of error is solvable
     */
    boolean solve(IConnection connection, EErrorType type);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
