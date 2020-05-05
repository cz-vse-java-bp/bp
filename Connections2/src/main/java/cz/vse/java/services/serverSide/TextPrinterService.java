package cz.vse.java.services.serverSide;


import cz.vse.java.handlers.TextMessageHandler;
import cz.vse.java.util.observerDP.IObserver;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code TextPrinterService} is used to abstractly define
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
public class TextPrinterService extends AGeneralService {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link TextPrinterService class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public TextPrinterService(String routerIP,
                              int routerPort,
                              int clientsPort,
                              int maxClients,
                              String pathToTrustStore,
                              String passwordToTrustStore,
                              String pathToKeyStore,
                              String passwordToKeyStore) {

        super(
                EServiceType.TEXT_PRINTER,
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

        this.observers = new CopyOnWriteArrayList<>();

        super.getRouter().addMessageHandler(new TextMessageHandler(null));
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



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of TextPrinterService.
     *
     */
    public static void main(String[] args){

        TextPrinterService service = new TextPrinterService(
                "localhost",
                888,
                7878,
                50,
                "C:\\Users\\user\\Desktop\\skola\\BP\\Projects\\Connections2\\src\\main\\Resources\\trustStore.jts", "changeit",
                "C:\\Users\\user\\Desktop\\skola\\BP\\Projects\\Connections2\\src\\main\\Resources\\keyStore.jks", "changeit"
        );

        new Thread(service).start();

        System.err.println(">>> Creation successfull...");
    }
}
