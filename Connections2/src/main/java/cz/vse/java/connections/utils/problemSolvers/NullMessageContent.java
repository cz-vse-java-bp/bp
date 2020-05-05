package cz.vse.java.connections.utils.problemSolvers;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.problemSolvers.utils.AProblemSolver;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.utils.EErrorType;

import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code NullMessageContent} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.problemSolvers
 */
public class NullMessageContent extends AProblemSolver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private ESolveMethod method;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link NullMessageContent class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public NullMessageContent(ESolveMethod method) {

        super(EErrorType.NULL_MESSAGE_CONTENT);
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

            switch (method) {

                case CLOSE: {

                    connection.close();
                    return true;
                }
                case POLITE_CLOSE: {

                    connection.send(new QuitMessage());
                    return true;
                }
                default: {

                    LOG.log(Level.SEVERE, "No suitable method for solving this kind of problem!");
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
