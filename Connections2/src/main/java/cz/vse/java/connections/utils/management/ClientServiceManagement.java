package cz.vse.java.connections.utils.management;


import cz.vse.java.connections.clientSide.C2SConnection;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.services.references.ServiceReferenceContainer;
import cz.vse.java.services.serverSide.AService;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.Token;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ClientServiceManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 06. 04. 2020
 *
 *
 * @see cz.vse.java.connections.utils.management
 */
public class ClientServiceManagement extends AConnectionManager implements ISubject, IObserver, Runnable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private CopyOnWriteArrayList<C2SConnection> services;
    private final CopyOnWriteArrayList<IObserver> observers;

    private String pathToTrustStore;
    private String passwordToTrustStore;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ClientServiceManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ClientServiceManagement(AService service, String pathToTrustStore, String passwordToTrustStore) {

        super(service);
        this.services = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();

        this.pathToTrustStore = pathToTrustStore;
        this.passwordToTrustStore = passwordToTrustStore;
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


    }

    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.addIfAbsent(observer);
        }
    }

    /**
     * <p>Removes the {@link IObserver} from the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void removeObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.remove(observer);
        }
    }

    /**
     * <p>Notifies all the {@link IObserver}s about
     * the change of state</p>
     */
    @Override
    public void notifyObservers() {

        synchronized (this.observers) {

            for (IObserver observer : this.observers) {

                observer.update();
            }
        }
    }

    /**
     * <p>Gets updated by notification of the {@link ISubject}
     * implementing instance.</p>
     */
    @Override
    public void update() {

        ArrayList<ServiceReferenceContainer> services =
                Client.getInstance().getSrcContainer().getReadyForConnection();

        if(services.size() > 0) {

            for (ServiceReferenceContainer src : services) {

                ServiceReference sr = src.getServiceReference();
                Token token = src.getToken();

                C2SConnection connection = new C2SConnection(
                        sr.getIp(),
                        sr.getPort(),
                        sr.getType(),
                        token,
                        this,
                        this.pathToTrustStore,
                        this.passwordToTrustStore
                );

                this.services.add(connection);
                new Thread(connection).start();
            }
        }
    }

    /**
     * <p>Returns {@link C2SConnection} with a service by given
     * {@code type} of the service. When not found, returns null.</p>
     *
     * @param type  {@link EServiceType} of the service the connection
     *              should be with.
     * @return      the connection or null.
     */
    public C2SConnection getServiceConnection(EServiceType type) {

        for (C2SConnection connection : this.services) {

            if(connection.getOtherSideServiceType().equals(type)) {

                return connection;
            }
        }
        return null;
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
