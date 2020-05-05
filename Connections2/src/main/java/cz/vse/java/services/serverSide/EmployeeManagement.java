package cz.vse.java.services.serverSide;


import cz.vse.java.util.observerDP.IObserver;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code EmployeeManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 04. 04. 2020
 *
 *
 * @see cz.vse.java.services
 */
public class EmployeeManagement extends AGeneralService {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link EmployeeManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public EmployeeManagement(EServiceType type,
                              String routerIP,
                              int routerPort,
                              int clientsPort,
                              int maxClients,
                              String pathToTrustStore,
                              String passwordToTrustStore,
                              String pathToKeyStore,
                              String passwordToKeyStore) {

        super(
                type,
                routerIP,
                routerPort,
                clientsPort,
                maxClients,
                false,
                pathToTrustStore,
                passwordToTrustStore,
                pathToKeyStore,
                passwordToKeyStore
        );


    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        new Thread(super.router).start();
        new Thread(super.clients).start();
    }

    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

    }

    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void removeObserver(IObserver observer) {

    }

    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    @Override
    public void notifyObservers() {

    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



}
