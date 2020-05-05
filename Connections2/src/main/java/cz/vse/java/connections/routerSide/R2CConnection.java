package cz.vse.java.connections.routerSide;


import cz.vse.java.authentication.AuthenticationHandlerContainer;
import cz.vse.java.authentication.EAuthenticationScenarioType;
import cz.vse.java.authentication.IAuthenticationScenario;
import cz.vse.java.connections.utils.ISSConnection;
import cz.vse.java.connections.utils.management.AConnectionManager;
import cz.vse.java.handlers.GiveMeRolesHandler;
import cz.vse.java.handlers.ServiceReferenceRequestHandler;
import cz.vse.java.handlers.TextMessageHandler;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.authentication.UserAuthenticator;
import cz.vse.java.handlers.QuitMessageHandler;
import cz.vse.java.messages.AuthenticationResultContainer;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.ConnectionAutoCloser;
import cz.vse.java.util.Token;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;

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
 * <p>The class of {@code R2CConnection} is used to abstractly define
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
public class R2CConnection implements ISSConnection {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Token token = new Token("");

    private boolean listening = false;
    private boolean authenticated = false;
    private boolean amIAuthenticated = false;

    private AConnectionManager connectionManager;

    private AuthenticationHandlerContainer authenticationHandlers;
    private final HashMap<EAuthenticationScenarioType, Boolean> authenticationState;

    private HandlerContainer messageHandlers;

    private ConnectionAutoCloser autoCloser;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link R2CConnection class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public R2CConnection(Socket socket, AConnectionManager connectionManager) {

        this.socket = socket;
        this.authenticationState = new HashMap<>();
        this.connectionManager = connectionManager;

        try {
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        autoCloser = new ConnectionAutoCloser(this, 30, false);

        prepareAuthentication();
        prepareMessageHandlers();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/

    private void prepareMessageHandlers() {

        this.messageHandlers = new HandlerContainer();
        this.messageHandlers.add(new QuitMessageHandler(messageHandlers));
        this.messageHandlers.add(new TextMessageHandler(messageHandlers));
        this.messageHandlers.add(new ServiceReferenceRequestHandler(messageHandlers));
        this.messageHandlers.add(new GiveMeRolesHandler(messageHandlers));
    }

    private void prepareAuthentication() {

        this.authenticationHandlers = new AuthenticationHandlerContainer();
        this.authenticationHandlers.add(new UserAuthenticator(
                authenticationHandlers,
                DatabaseConnectionContainer.getInstance().get(EDBUse.USER_AUTHENTICATION)
        ));

        for (IHandler scenario : authenticationHandlers.getHandlers()) {

            EAuthenticationScenarioType type =
                    ((IAuthenticationScenario) scenario).getAuthScenarioType();
            authenticationState.putIfAbsent(type, false);
        }
    }


    /**
     * <p>Closes the connection channel.</p>
     */
    @Override
    public void close() {

        LOG.log(Level.INFO, "Closing the connection with client.");
        listening = false;
        try {
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

        listening = true;

        while (listening) {

            try {
                IMessage message = (IMessage) input.readObject();

                if(message instanceof QuitMessage) {

                    ((QuitMessage) message).execute(this);
                }

                if(!authenticated) {

                    this.authenticationHandlers.handle(this, message);

                } else {

                    this.messageHandlers.handle(this, message);
                }
            } catch (SocketException | EOFException e) {

                LOG.log(Level.SEVERE, "Connection interrupted!");
                close();

            } catch (IOException | ClassNotFoundException e) {

                LOG.log(Level.SEVERE, "Message malformed!");
                close();

            }
        }
    }

    /**
     * <p>Returns the service type</p>
     *
     * @return type of the service.
     */
    @Override
    public EServiceType getServiceType() {

        return EServiceType.ROUTER;
    }

    /**
     * <p>Sends the message to the channel.</p>
     *
     * @param message to be sent
     */
    @Override
    public void send(IMessage message) {

        message.setToken(this.token);

        try {
            this.output.writeObject(message);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot send the message!");
        }
        try {
            this.output.flush();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot flush the stream!");
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


    /**
     * <p>Updates the authentication state.</p>
     *
     * @param type      type of the auth scenario to be updated
     * @param value     the result of the auth scenario test
     */
    public void updateAuthState(EAuthenticationScenarioType type, Boolean value) {

        synchronized (this.authenticationState) {

            if(this.authenticationState.containsKey(type)) {

                this.authenticationState.put(type, value);
            }

            if(!this.authenticationState.containsValue(false)) {

                this.setAuthenticated(true);
                this.send(new AuthenticationResultContainer(true));
                this.autoCloser.stop();
            }
        }
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

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
     * of the instance of {@link R2CConnection}
     *
     * @return the value of {@code token}
     * @see Token
     * @see R2CConnection
     */
    @Override
    public Token getToken() {

        return token;
    }

    /**
     * Getter for {@link AConnectionManager} formed {@code connectionManager}
     * of the instance of {@link R2CConnection}
     *
     * @return the value of {@code connectionManager}
     * @see AConnectionManager
     * @see R2CConnection
     */
    public AConnectionManager getConnectionManager() {

        return connectionManager;
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


    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the boolean-formed
     * {@code authenticated} variable.</p>
     *
     * @param authenticated given $field.typeName value to
     *                      be set to the variable
     * @see boolean
     * @see R2CConnection
     */
    public void setAuthenticated(boolean authenticated) {

        this.authenticated = authenticated;
    }

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see R2CConnection
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
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
}
