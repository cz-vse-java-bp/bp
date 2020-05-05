package cz.vse.java.util.observerDP;


/**************************************************************
 * <p>The interface of IObserver is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 * @see cz.vse.java.utils.observerDP
 */
public interface IObserver {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     */
    void update();


    /* *****************************************************************/
    /* Default methods *************************************************/


}
