package cz.vse.java.connections.utils.problemSolvers.utils;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.utils.EErrorType;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProblemSolverContainer} is used to abstractly define
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
public class ProblemSolverContainer {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<IProblemSolver> container;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProblemSolverContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ProblemSolverContainer() {

        this.container = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public void add(IProblemSolver solver) {

        synchronized (this.container) {

            this.container.addIfAbsent(solver);
        }
    }

    public boolean solve(IConnection connection, EErrorType type) {

        boolean solved = false;

        synchronized (this.container) {

            for (IProblemSolver solver : this.container) {

                if(solver.solve(connection, type)) {

                    solved = true;
                    break;
                }
            }
        }

        return solved;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
