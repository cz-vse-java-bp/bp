package cz.vse.java.connections.utils.problemSolvers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.problemSolvers.utils.AProblemSolver;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.connections.utils.problemSolvers.utils.IProblemSolver;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.ServiceTypeRequest;
import cz.vse.java.messages.utils.EErrorType;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code WrongServiceType} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 06. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.problemSolvers.utils
 */
public class WrongServiceType extends AProblemSolver implements IProblemSolver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ESolveMethod method;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link WrongServiceType class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public WrongServiceType(ESolveMethod method) {

        super(EErrorType.WRONG_SERVICE_TYPE);
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

            LOG.log(Level.INFO, "Solving " + type + " using method " + method.getShortDesc());

            switch (method) {

                case DN: {

                    return true;
                }
                case POLITE_CLOSE: {

                    connection.send(new QuitMessage());
                    return true;
                }
                case CLOSE: {

                    connection.close();
                    return true;
                }
                case REPEAT: {

                    connection.send(new ServiceTypeRequest());
                    return true;
                }
                default: {

                    LOG.log(Level.SEVERE, "Given method is not suitable for this kind of problem!");
                }
            }
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
