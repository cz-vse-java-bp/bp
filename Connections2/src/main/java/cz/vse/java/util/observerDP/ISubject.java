package cz.vse.java.util.observerDP;


/**************************************************************
 * <p>The interface of ISubject is used to contain all methods
 * of the type to be overriden (implemented).</p>
 *
 * <p>This interface defines the <b>Subject</b> from <b>Observer</b>
 * design pattern. This side of the relation has to be able to add {@link IObserver},
 * to remove it and to notify all the observers.</p>
 *
 * Written for project "Connections2".
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 */
public interface ISubject {


    /* *****************************************************************/
    /* Methods to be overriden *****************************************/


    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer  the listener to any change.
     */
    void addObserver(IObserver observer);


    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer  the listener to any change.
     */
    void removeObserver(IObserver observer);


    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    void notifyObservers();

    /* *****************************************************************/
    /* Default methods *************************************************/


}
