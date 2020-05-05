package cz.vse.java.connections.utils.problemSolvers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.problemSolvers.utils.AProblemSolver;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.utils.EErrorType;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code WrongUsername} is used to abstractly define
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
public class WrongUsername extends AProblemSolver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ESolveMethod method;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link WrongUsername class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public WrongUsername(ESolveMethod method) {

        super(EErrorType.WRONG_USERNAME);
        this.method = method;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Method responsible for problem solving.</p>
     *
     * @param type type of error
     * @return if the given type of error is solvable
     */
    @Override
    public boolean solve(IConnection connection, EErrorType type) {

        if(super.check(type)) {

            switch (this.method) {

                case DN: {

                    LOG.log(Level.SEVERE, "Doing nothing.");
                    return true;
                }
                case CLOSE: {

                    LOG.log(Level.SEVERE, "Problem leads to connection close.");
                    connection.close();
                    return true;
                }
                case POLITE_CLOSE: {

                    LOG.log(Level.SEVERE, "Problem leads to connection polite close.");
                    connection.send(new QuitMessage());
                    return true;
                }
                default: {

                    LOG.log(Level.SEVERE, "UNSUPPORTED METHOD: " + method.getShortDesc());
                    break;
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
