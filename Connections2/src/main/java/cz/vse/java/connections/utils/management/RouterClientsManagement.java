package cz.vse.java.connections.utils.management;


import cz.vse.java.authentication.TokenContainer;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.ISSConnectionManager;
import cz.vse.java.connections.routerSide.R2CConnection;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.services.serverSide.AService;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.references.EReferenceFor;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.util.FingerPrint;
import cz.vse.java.util.SSLServerConfigManager;
import cz.vse.java.util.Token;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code RouterClientsManagement} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.connections.routerSide
 */
public class RouterClientsManagement extends AConnectionManager implements Runnable, ISSConnectionManager {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private int maxClients;
    private int port;

    private final CopyOnWriteArrayList<IConnection> clients;

    private SSLServerSocket socket;
    private TokenContainer tokens;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link RouterClientsManagement class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public RouterClientsManagement(int maxClients,
                                   int port,
                                   AService service,
                                   String pathToKeystore,
                                   String passwordToKeystore) {

        super(service);

        if(maxClients < 1) {

            throw new IllegalArgumentException("Not possible to create " + maxClients + " connections!");
        } else if(port < 1) {

            throw new IllegalArgumentException("Port not identified: " + port);
        } else {

            this.maxClients = maxClients;
            this.port = port;
            this.tokens = new TokenContainer();

            clients = new CopyOnWriteArrayList<>();

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

        LOG.log(Level.SEVERE, "Starting listening at port " + port + " to new clients!");
        listenToNewClients();
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

    public void listenToNewClients() {

        while (this.clients.size() <= maxClients) {

            LOG.log(Level.INFO, "Listening to new connection. Capacity is filled from "
                    + this.clients.size() + " out of " + maxClients);
            try {
                R2CConnection connection = new R2CConnection(socket.accept(), this);

                for (IHandler handler : super.getHandlers().getHandlers()) {

                    connection.addMessageHandler(handler.copy(connection.getMessageHandlerContainer()));
                }

                LOG.log(Level.FINE, "Someone connected!");

                Thread t = new Thread(connection);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOG.log(Level.INFO, "Max connections reached.");
    }


    public void register(R2CConnection connection) {

        synchronized (this.clients) {

            clients.addIfAbsent(connection);
            LOG.log(Level.INFO, "Connection registered!");
        }
    }

    public void unregister(R2CConnection connection) {

        synchronized (this.clients) {

            clients.remove(connection);
            LOG.log(Level.INFO, "Connection unregistered!");
        }
        listenToNewClients();
    }

    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler handler to be added.
     */
    @Override
    public void addMessageHandler(IHandler handler) {

        super.getHandlers().add(handler.copy(super.getHandlers()));

        for (IConnection connection : this.clients) {

            connection.addMessageHandler(handler.copy(connection.getMessageHandlerContainer()));
        }
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

    public boolean checkToken(IConnection connection, Token token) {

        return this.tokens.validate(connection, token);
    }

    public void close() {

        synchronized (this.clients) {

            for (IConnection connection : this.clients) {

                connection.close();
            }
        }

        this.clients.clear();
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * <p>Builds the {@link ServiceReference} instance.</p>
     *
     * @return the service reference for creating the connection usage
     */
    @Override
    public ServiceReference getServiceReference() {

        return new ServiceReference(
                EReferenceFor.CLIENT,
                this.getIp(),
                this.getPort(),
                EServiceType.ROUTER);
    }

    public String getIp() {

        return new FingerPrint().getIp();
    }

    public int getPort() {

        return socket.getLocalPort();
    }

    /**
     * Getter for {@link TokenContainer} formed {@code tokens}
     * of the instance of {@link RouterClientsManagement}
     *
     * @return the value of {@code tokens}
     * @see TokenContainer
     * @see RouterClientsManagement
     */
    public TokenContainer getTokens() {

        return tokens;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/



}
