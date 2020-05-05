package cz.vse.java.services.serverSide;


import cz.vse.java.connections.utils.management.RouterClientsManagement;
import cz.vse.java.connections.utils.management.ServicesManagement;
import cz.vse.java.util.observerDP.IObserver;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Router} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.services
 */
public class Router extends AService {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private RouterClientsManagement clients;
    private ServicesManagement services;

    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Router class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public Router(int maxClients,
                  int maxServices,
                  int clientPort,
                  int servicePort,
                  String pathToKeyStore,
                  String passwordToKeyStore) {

        super(EServiceType.ROUTER);
        this.observers = new CopyOnWriteArrayList<>();

        clients = new RouterClientsManagement(
                maxClients,
                clientPort,
                this,
                pathToKeyStore,
                passwordToKeyStore
        );

        services = new ServicesManagement(
                maxServices,
                servicePort,
                this,
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

        new Thread(services).start();
        new Thread(clients).start();
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

            for (IObserver observer : observers) {

                observer.update();
            }
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link RouterClientsManagement} formed {@code clients}
     * of the instance of {@link Router}
     *
     * @return the value of {@code clients}
     * @see RouterClientsManagement
     * @see Router
     */
    public RouterClientsManagement getClients() {

        return clients;
    }

    /**
     * <p>Returns the port the clients can connect to the service at.</p>
     *
     * @return integer representation of port
     */
    @Override
    public int getClientsPort() {

        return clients.getPort();
    }

    /**
     * <p>Returns the IP address this service is working at.</p>
     *
     * @return {@link String} representation of the address
     * the clients can connect with this service at.
     */
    @Override
    public String getClientsIP() {

        return clients.getIp();
    }


    /**
     * Getter for {@link ServicesManagement} formed {@code services}
     * of the instance of {@link Router}
     *
     * @return the value of {@code services}
     * @see ServicesManagement
     * @see Router
     */
    public ServicesManagement getServices() {

        return services;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/


}
