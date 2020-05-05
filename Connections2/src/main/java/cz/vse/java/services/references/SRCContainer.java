package cz.vse.java.services.references;


import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.Token;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code SRCContainer} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 05. 04. 2020
 *
 *
 * @see cz.vse.java.services.references
 */
public class SRCContainer implements ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private final CopyOnWriteArrayList<ServiceReferenceContainer> container;
    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link SRCContainer class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public SRCContainer() {

        this.container = new CopyOnWriteArrayList<>();
        this.observers = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    public void add(ServiceReferenceContainer container) {

        boolean indicatorOfPresence = false;

        synchronized (this.container) {

            for (ServiceReferenceContainer src : this.container) {

                if(src.getServiceReference().getType().equals(container.getServiceReference().getType())) {

                    indicatorOfPresence = true;
                }
            }

            if(!indicatorOfPresence) {

                this.container.addIfAbsent(container);
            }
        }
    }



    public void addToken(Token token, EServiceType type) {

        synchronized (this.container) {

            for (ServiceReferenceContainer src : this.container) {

                if(src.getServiceReference().getType().equals(type)) {

                    src.setToken(token);
                    break;
                }
            }
        }

        this.notifyObservers();
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


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    public ServiceReferenceContainer get(EServiceType type) {

        ServiceReferenceContainer serviceReferenceContainer = null;

        synchronized (this.container) {

            for (ServiceReferenceContainer src : this.container) {

                if (src.getServiceReference().getType().equals(type)) {

                    if (!src.wasUsed()) {

                        serviceReferenceContainer = src;
                        serviceReferenceContainer.setUsed(true);
                        break;

                    } else {

                        LOG.log(Level.INFO, "Already was used " + src.toString());
                    }
                }
            }

            this.container.remove(serviceReferenceContainer);
        }

        if(serviceReferenceContainer == null) {

            LOG.log(Level.INFO, "No suitable service reference container found.");
        }

        return serviceReferenceContainer;
    }


    public ArrayList<ServiceReferenceContainer> getReadyForConnection() {

        ArrayList<ServiceReferenceContainer> ready = new ArrayList<>();

        synchronized (this.container) {

            for (ServiceReferenceContainer src : this.container) {

                if(src.isReadyToCreate()) {

                    ready.add(src);
                }
            }

            for (ServiceReferenceContainer src : ready) {

                this.container.remove(src);
            }
        }

        return ready;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/



}
