package cz.vse.java.connections.routerSide;


import cz.vse.java.authentication.AuthenticationHandlerContainer;
import cz.vse.java.authentication.EAuthenticationScenarioType;
import cz.vse.java.authentication.FingerPrintDBAuthenticator;
import cz.vse.java.connections.utils.ISSConnection;
import cz.vse.java.connections.utils.management.AConnectionManager;
import cz.vse.java.connections.utils.management.ServicesManagement;
import cz.vse.java.handlers.*;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.references.ServiceReference;
import cz.vse.java.util.ConnectionAutoCloser;
import cz.vse.java.util.Token;
import cz.vse.java.util.database.DBConnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code R2SConnection} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "Connections2".</i>
 * @author Vojtěch Pavlů
 * @version 02. 04. 2020
 *
 *
 * @see cz.vse.java.connections
 */
public class R2SConnection implements ISSConnection {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private EServiceType serviceType;
    private ServiceReference srForClients;
    private boolean amIAuthenticated = false;

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private ServicesManagement connectionManager;

    private Token token = new Token("");

    private boolean listening = false;
    private boolean authenticated = false;

    private AuthenticationHandlerContainer authenticationHandlers;
    private final HashMap<EAuthenticationScenarioType, Boolean> authenticationState;

    private HandlerContainer messageHandlers;

    private ConnectionAutoCloser autoCloser;

    private Long start;
    private Long end;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link R2SConnection class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public R2SConnection(Socket socket, ServicesManagement connectionManager) {

        LOG.log(Level.INFO, "Creating " + this.getClass().getName() + " instance.");
        this.socket = socket;
        this.connectionManager = connectionManager;

        this.authenticationHandlers = new AuthenticationHandlerContainer();
        this.authenticationState = new HashMap<>();
        this.messageHandlers = new HandlerContainer();

        this.init();

        autoCloser = new ConnectionAutoCloser(this, 60, false);
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    private void init() {

        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot initialize the inputs and outputs! " + e.getMessage());
        }

        this.authenticationHandlers.add(new FingerPrintDBAuthenticator(
                authenticationHandlers,
                EServiceType.ROUTER,
                new DBConnection("jdbc:h2:tcp://localhost/~/test", "sa", "")
                        .getConnection()
        ));

        this.messageHandlers.add(new QuitMessageHandler(messageHandlers));
        this.messageHandlers.add(new TextMessageHandler(messageHandlers));
        this.messageHandlers.add(new ServiceReferenceContainerHandler(messageHandlers));
        this.messageHandlers.add(new ServiceTypeContainerHandler(messageHandlers));

    }

    /**
     * <p>Returns the {@link AConnectionManager} instance this
     * connection belongs to.</p>
     *
     * @return the connection manager
     */
    @Override
    public AConnectionManager getConnectionManager() {

        return this.connectionManager;
    }


    /**
     * <p>Closes the connection channel.</p>
     */
    @Override
    public void close() {

        LOG.log(Level.INFO, "Closing the connection with service.");
        listening = false;

        this.connectionManager.unregister(this);

        try {
            autoCloser.stop();
            this.input.close();
            this.output.close();
            this.socket.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot close the connection! " + e.getMessage());
        }

        LOG.log(Level.INFO, "Connection closed.");
    }

    /**
     * <p>Provides the listening operation.</p>
     */
    @Override
    public void listen() {

        start = System.currentTimeMillis();
        listening = true;

        while (listening) {

            try {
                IMessage message = (IMessage) input.readObject();

                if(message instanceof QuitMessage) {

                    messageHandlers.handle(this, message);
                }

                if(!authenticated) {

                    this.authenticationHandlers.handle(this, message);

                } else {

                    this.messageHandlers.handle(this, message);
                }
            } catch (SocketException | EOFException e) {

                e.printStackTrace();
                close();

            } catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();
                close();
            }
        }
    }

    /**
     * <p>Sends the message to the channel.</p>
     *
     * @param message to be sent
     */
    @Override
    public void send(IMessage message) {

        if(message != null) {
            message.setToken(this.token);

            try {
                this.output.writeObject(message);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Cannot send the message: " + e.getMessage());
            }
        } else {

            LOG.log(Level.SEVERE, "Message is null - cannot be sent!");
        }
    }

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

        this.autoCloser.reset();
        listen();
    }


    public void updateAuthState(EAuthenticationScenarioType type, Boolean value) {

        synchronized (this.authenticationState) {

            if(this.authenticationState.containsKey(type)) {

                this.authenticationState.put(type, value);
            }

            if(!this.authenticationState.containsValue(false)) {

                autoCloser.stop();
                this.setAuthenticated(true);
                this.send(new AuthenticationResultContainer(true));
                end = System.currentTimeMillis();
                LOG.log(Level.INFO, "Authentication lasted " + (end - start) + " ms.");
                this.send(new ServiceTypeRequest());
                this.send(new ServiceReferenceRequest(this.serviceType));
                this.connectionManager.register(this);
            }
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link EServiceType} formed {@code serviceType}
     * of the instance of {@link R2SConnection}
     *
     * @return the value of {@code serviceType}
     * @see EServiceType
     * @see R2SConnection
     */
    public EServiceType getServiceType() {

        return serviceType;
    }

    /**
     * <p>gets the authentication result</p>
     *
     * @return result of the authentication process
     * on the other side of the connection
     */
    @Override
    public boolean getAmIAuthenticated() {

        return this.amIAuthenticated;
    }

    /**
     * Getter for {@link Token} formed {@code token}
     * of the instance of {@link R2SConnection}
     *
     * @return the value of {@code token}
     * @see Token
     * @see R2SConnection
     */
    @Override
    public Token getToken() {

        return token;
    }


    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler handler to be added.
     */
    @Override
    public void addMessageHandler(IHandler handler) {

        this.messageHandlers.add(handler);
    }


    /**
     * Getter for {@link ServiceReference} formed {@code srForClients}
     * of the instance of {@link R2SConnection}
     *
     * @return the value of {@code srForClients}
     * @see ServiceReference
     * @see R2SConnection
     */
    public ServiceReference getSrForClients() {

        return srForClients;
    }

    /**
     * <p>Gets reference to the message handler container.</p>
     *
     * @return the given message handler container
     */
    @Override
    public HandlerContainer getMessageHandlerContainer() {

        return this.messageHandlers;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code EServiceType} formed
     * {@code serviceType} variable.</p>
     *
     * @param serviceType given EServiceType value to
     *                    be set to the variable
     * @see EServiceType
     * @see R2SConnection
     */
    public void setServiceType(EServiceType serviceType) {

        this.serviceType = serviceType;
    }

    /**
     * <p>Setter for the {@code ServiceReference} formed
     * {@code srForClients} variable.</p>
     *
     * @param srForClients given ServiceReference value to
     *                     be set to the variable
     * @see ServiceReference
     * @see R2SConnection
     */
    public void setSrForClients(ServiceReference srForClients) {

        this.srForClients = srForClients;
    }

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code authenticated} variable.</p>
     *
     * @param authenticated given $field.typeName value to
     *                      be set to the variable
     * @see boolean
     * @see R2SConnection
     */
    public void setAuthenticated(boolean authenticated) {

        this.authenticated = authenticated;
    }

    /**
     * <p>Sets the authentication result</p>
     *
     * @param result of the authentication process
     *               on the other side of the connection
     */
    @Override
    public void setAmIAuthenticated(boolean result) {

        this.amIAuthenticated = result;
    }

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see R2SConnection
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
    }
}
