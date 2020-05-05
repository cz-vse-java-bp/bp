package cz.vse.java.connections.utils.management;


import cz.vse.java.authentication.TokenContainer;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.ISSConnectionManager;
import cz.vse.java.connections.routerSide.R2SConnection;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.services.serverSide.AService;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.references.EReferenceFor;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.util.SSLServerConfigManager;
import cz.vse.java.util.Token;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ServicesManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 03. 04. 2020
 *
 *
 * @see cz.vse.java.connections.routerSide
 */
public class ServicesManagement extends AConnectionManager implements Runnable, ISSConnectionManager {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private int maxServices;
    private int port;

    private TokenContainer tokens;

    private final CopyOnWriteArrayList<IConnection> services;

    private SSLServerSocket socket;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ServicesManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public ServicesManagement(int maxServices,
                              int port,
                              AService service,
                              String pathToKeystore,
                              String passwordToKeystore) {

        super(service);

        if(maxServices < 1) {

            throw new IllegalArgumentException("Not possible to create service management for " + maxServices + " services.");

        } else if(port < 1) {

            throw new IllegalArgumentException("Port not explicitly defined. Given value: " + port);

        } else {

            this.maxServices = maxServices;
            this.port = port;
            this.services = new CopyOnWriteArrayList<>();

            SSLServerConfigManager config = SSLServerConfigManager.getInstance();
            config.init(pathToKeystore, passwordToKeystore);

            try {
                SSLServerSocketFactory serverSocketFactory =
                        (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

                this.socket = (SSLServerSocket) serverSocketFactory.createServerSocket(this.port);

                this.socket.setEnabledCipherSuites(config.getEnabledCiphers());
                this.socket.setEnabledProtocols(config.getEnabledProtocols());

            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Cannot create the Server Socket: " + e.getMessage());
            }
        }
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

        LOG.log(Level.SEVERE, "Starting listening at port " + port + " to new services!");
        listenToNewServices();
    }


    public void listenToNewServices() {

        while (this.services.size() <= maxServices) {

            LOG.log(Level.INFO, "Listening to new connection. Capacity is filled from "
                    + this.services.size() + " out of " + maxServices);
            try {
                R2SConnection connection = new R2SConnection(socket.accept(), this);

                LOG.log(Level.FINE, "Someone connected!");

                Thread t = new Thread(connection);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.log(Level.INFO, "Max connections reached.");
    }

    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler handler to be added.
     */
    @Override
    public void addMessageHandler(IHandler handler) {

        synchronized (this.services) {

            for (IConnection connection : this.services) {

                connection.addMessageHandler(handler.copy(connection.getMessageHandlerContainer()));
            }
        }
    }

    public ServiceReference getServiceReference(EServiceType type) {

        boolean done = false;

        while(!done) {

            done = true;

            for (IConnection connection : this.services) {

                EServiceType st = ((R2SConnection) connection).getServiceType();

                if(st == null) {

                    done = false;
                    break;

                } else if (st.equals(type)){

                    return ((R2SConnection) connection).getSrForClients();
                }
            }
        }
        return null;
    }


    public void register(IConnection connection) {

        synchronized (this.services) {

            //connection.send(new ServiceTypeRequest());
            //connection.send(new ServiceReferenceRequest(null));

            services.addIfAbsent(connection);
        }
    }


    public void unregister(IConnection connection) {

        synchronized (this.services) {

            services.remove(connection);
        }
    }

    /**
     * <p>Validates the token against the given and saved ones.</p>
     *
     * @param connection to be set to use the given token only if
     *                   it corresponds and is valid.
     * @param token      to be set to the connection, if it is valid.
     * @return the result of validity test
     */
    @Override
    public boolean validateToken(IConnection connection, Token token) {

        return this.tokens.validate(connection, token);
    }

    /**
     * <p>Adds the token to the {@link TokenContainer}
     * for future token validation.</p>
     *
     * @param token token
     */
    @Override
    public void addToken(Token token) {

        this.tokens.add(token);
    }



    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link int} formed {@code maxServices}
     * of the instance of {@link ServicesManagement}
     *
     * @return the value of {@code maxServices}
     * @see int
     * @see ServicesManagement
     */
    public int getMaxServices() {

        return maxServices;
    }

    /**
     * <p>Builds the {@link ServiceReference} instance.</p>
     *
     * @return the service reference for creating the connection usage
     */
    @Override
    public ServiceReference getServiceReference() {

        return new ServiceReference(
                EReferenceFor.SERVICE,
                this.getIp(),
                this.getPort(),
                EServiceType.ROUTER
        );
    }

    /**
     * Getter for {@link int} formed {@code port}
     * of the instance of {@link ServicesManagement}
     *
     * @return the value of {@code port}
     * @see int
     * @see ServicesManagement
     */
    public int getPort() {

        return this.socket.getLocalPort();
    }

    /**
     * Getter for {@link AService} formed {@code service}
     * of the instance of {@link ServicesManagement}
     *
     * @return the value of {@code service}
     * @see AService
     * @see ServicesManagement
     */
    public AService getService() {

        return service;
    }

    /**
     * Getter for {@link CopyOnWriteArrayList<>} formed {@code services}
     * of the instance of {@link ServicesManagement}
     *
     * @return the value of {@code services}
     * @see CopyOnWriteArrayList<>
     * @see ServicesManagement
     */
    public CopyOnWriteArrayList<IConnection> getServices() {

        return services;
    }

    public String getIp() {

        return this.socket.getInetAddress().toString().split("/")[1];
    }


    public R2SConnection getServiceConnection(EServiceType type) {

        boolean done = false;

        while(!done) {

            done = true;

            for (IConnection connection : this.services) {

                R2SConnection r2s = ((R2SConnection) connection);
                EServiceType r2sType = r2s.getServiceType();

                if(r2sType == null) {

                    done = false;
                    break;

                } else if (r2sType.equals(type)) {

                    return r2s;
                }
            }
        }

        return null;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
