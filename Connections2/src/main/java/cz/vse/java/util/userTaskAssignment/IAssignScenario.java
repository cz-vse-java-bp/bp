package cz.vse.java.util.userTaskAssignment;


import cz.vse.java.util.persistance.entities.tasks.Task;

/**************************************************************
 * <p>The interface of IAssignScenario is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 * <p>Defines the similarities of classes implementing this interface.</p>
 *
 * <p>Used for setting the assigner the basic method of
 * {@link IAssignScenario#assign(TaskSolverContainer, Task)}.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 10. 04. 2020
 *
 * @see cz.vse.java.utils.userTaskAssignment
 * @see Task
 * @see TaskSolverContainer
 * @see TaskSolver
 * @see RandomAssign
 */
public interface IAssignScenario {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Assigns the task to the user.</p>
     *
     * @param tsc   {@link TaskSolverContainer} to assign to
     * @param task  task to be assigned
     */
    void assign(TaskSolverContainer tsc, Task task);

    /* *****************************************************************/
    /* Default methods *************************************************/


}
