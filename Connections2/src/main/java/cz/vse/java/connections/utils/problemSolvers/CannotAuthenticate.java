package cz.vse.java.connections.utils.problemSolvers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.problemSolvers.utils.AProblemSolver;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.connections.utils.problemSolvers.utils.IProblemSolver;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.utils.EErrorType;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code CannotAuthenticate} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.problemSolvers
 */
public class CannotAuthenticate extends AProblemSolver implements IProblemSolver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ESolveMethod method;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link CannotAuthenticate class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public CannotAuthenticate(ESolveMethod method) {

        super(EErrorType.CANNOT_AUTHENTICATE);
        this.method = method;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method responsible for problem solving.</p>
     *
     * @param connection the solver of the error
     * @param type       type of error
     * @return if the given type of error is solvable
     */
    @Override
    public boolean solve(IConnection connection, EErrorType type) {

        if(super.check(type)) {

            LOG.log(Level.INFO, "Solving " + type.name() + " with method " + this.method.getShortDesc());

            switch (this.method) {

                case CLOSE: {

                    connection.close();
                    break;
                }
                case POLITE_CLOSE: {

                    connection.send(new QuitMessage());
                    break;
                }
                default: {

                    connection.close();
                    LOG.log(Level.SEVERE, "No acceptable method for this error!");
                }
            }
            return true;
        }

        return false;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
