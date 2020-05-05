package cz.vse.java.util.persistance.entities.tasks;


import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.service.TaskService;
import cz.vse.java.utils.random.RandomNumberGenerator;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RandomCheckSideTask} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 16. 04. 2020
 *
 *
 * @see cz.vse.java.utils.persistance.entities.tasks
 */
public class RandomCheckSideTask implements ISideTaskAssigner {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private short probability;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RandomCheckSideTask class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/


    /**
     * <p>Prepares assigner with given probability. When assigning,
     * it picks a random number in between 0 and 100 bounds.
     * When the random number is smaller than the given probability,
     * it assigns the task.</p>
     *
     * <p>It means, when the given {@code probability} is not in range
     * of 0-100, it throws {@link IllegalArgumentException}.</p>
     *
     * <p>Recommendation for this value could be in between 20 and 30.</p>
     *
     * @param probability   the number representing probability of assigning
     *                      the task in percents
     */
    public RandomCheckSideTask(short probability) {

        if(probability < 0 || probability > 100) {

            throw new IllegalArgumentException(
                    "The number can be in range of 0 to 100 only. " +
                    "You entered: " + probability);
        }

        this.probability = probability;
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    /**
     * <p>Assigns an side task to the main {@link Task} assignment flow,
     * next to the main task (as "prepare something").</p>
     *
     * <p></p>
     *
     * @param orderItem which the side task should be assigned to.
     *
     * @return {@link Task} to be processed.
     */
    @Override
    public Task assign(OrderItem orderItem) throws SQLException {

        int random = RandomNumberGenerator.getRandomNumberInRange(0, 100);

        Task task = null;

        if(random <= probability) {

            task = new Task();
            task.setDescription(TaskDescriptionGenerator.getCheckProduct(orderItem.getProduct()));
            task.setCreated(LocalDateTime.now());
            task.setUser(null);
            task.setState(ETaskState.NOT_ASSIGNED);

            task.setId(new TaskService().getUniqueId());

            orderItem.addTask(task);
        }

        return task;
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
