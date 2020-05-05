package cz.vse.java.util.userTaskAssignment;


import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.utils.random.RandomNumberGenerator;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RandomAssign} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>Instances of this class are used for assigning tasks to the
 * specified users by random.</p>
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 *
 *
 * @see cz.vse.java.utils.userTaskAssignment
 */
public class RandomAssign implements IAssignScenario {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RandomAssign class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Non-parametric constructor used for initializing the space.</p>
     */
    public RandomAssign() { }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Assigns the task to the user.</p>
     *
     * <p>The assignment is by random, when there is at least one
     * listener to the tasks.</p>
     *
     * @param task task to be assigned
     */
    @Override
    public void assign(TaskSolverContainer tsc, Task task) {

        int size = tsc.getContainer().size();

        if (size > 0) {

            TaskSolver ts = tsc.getContainer().get(
                    RandomNumberGenerator
                            .getRandomNumberInRange(0, size - 1));

            ts.add(task);
        }
    }
}
