package cz.vse.java.connections.serviceSide;


import cz.vse.java.authentication.AuthenticationHandlerContainer;
import cz.vse.java.authentication.EAuthenticationScenarioType;
import cz.vse.java.authentication.TokenValidator;
import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.ISSConnection;
import cz.vse.java.connections.utils.management.AConnectionManager;
import cz.vse.java.connections.utils.management.ServiceClientsManagement;
import cz.vse.java.connections.utils.problemSolvers.*;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.connections.utils.problemSolvers.utils.IProblemSolver;
import cz.vse.java.connections.utils.problemSolvers.utils.ProblemSolverContainer;
import cz.vse.java.handlers.*;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AuthenticationResultContainer;
import cz.vse.java.messages.QuitMessage;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.ConnectionAutoCloser;
import cz.vse.java.util.Token;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code S2CConnection} is used to abstractly define
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
public class S2CConnection implements ISSConnection, IProblemSolver, ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private Token token = new Token("");

    private boolean listening = false;
    private boolean authenticated = false;
    private boolean amIAuthenticated = false;

    private ServiceClientsManagement connectionManager;

    private AuthenticationHandlerContainer authenticationHandlers;
    private final HashMap<EAuthenticationScenarioType, Boolean> authenticationState;

    private HandlerContainer handlers;

    private ConnectionAutoCloser autoCloserWhenNotusing;
    private ConnectionAutoCloser autoCloser;
    private boolean autoClose;

    private ProblemSolverContainer problemSolvers;

    private boolean running = false;
    private final CopyOnWriteArrayList<IObserver> observers;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link S2CConnection class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public S2CConnection(Socket socket,
                         ServiceClientsManagement connectionManager,
                         boolean autoClose) {

        this.socket = socket;
        this.connectionManager = connectionManager;

        this.autoClose = autoClose;
        this.autoCloser = new ConnectionAutoCloser(this, 60, false);
        this.autoCloserWhenNotusing = new ConnectionAutoCloser(this, 300, false);

        this.handlers = new HandlerContainer();
        this.authenticationHandlers = new AuthenticationHandlerContainer();
        this.authenticationState = new HashMap<>();

        init();
        this.observers = new CopyOnWriteArrayList<>();
    }

    /* *****************************************************************/
    /* Instance methods ************************************************/


    private void init() {

        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.authenticationHandlers.add(
                new TokenValidator(
                        authenticationHandlers,
                        this.getConnectionManager().getTokens()
                )
        );

        this.handlers.add(new AuthResultContainerHandler(handlers));
        this.handlers.add(new ErrorMessageHandler(handlers));
        this.handlers.add(new FingerPrintContainerRequestHandler(handlers));
        this.handlers.add(new PasswordRequestHandler(handlers));
        this.handlers.add(new QuitMessageHandler(handlers));
        this.handlers.add(new ServiceReferenceContainerHandler(handlers));
        this.handlers.add(new ServiceTypeContainerHandler(handlers));
        this.handlers.add(new TextMessageHandler(handlers));
        this.handlers.add(new UserNameRequestHandler(handlers));
        this.handlers.add(new UseTokenHandler(handlers));

        this.problemSolvers = new ProblemSolverContainer();
        this.problemSolvers.add(new WrongUsername(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new WrongPassword(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new ConnectionInterrupted(ESolveMethod.CLOSE));
        this.problemSolvers.add(new WrongMessage(ESolveMethod.CLOSE));
        this.problemSolvers.add(new WrongFingerPrint(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new CannotAuthenticate(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new ServiceReferenceNotFound(ESolveMethod.DN));
        this.problemSolvers.add(new CannotSendMessage(ESolveMethod.CLOSE));
        this.problemSolvers.add(new CannotAcceptMessage(ESolveMethod.CLOSE));
    }


    /**
     * <p>Method responsible for problem solving.</p>
     *
     * @param connection the solver of the error
     * @param type       type of error
     * @return if the given type of error is solvable
     */
    @Override
    public boolean solve(IConnection connection, EErrorType type) {

        return problemSolvers.solve(connection, type);
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
            System.out.println("added observer " + observer.getClass().getName());
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

            for (IObserver o : this.observers) {

                o.update();
            }
        }
    }

    /**
     * <p>Closes the connection channel.</p>
     */
    @Override
    public void close() {

        LOG.log(Level.INFO, "Closing the connection with client.");
        listening = false;

        this.running = false;
        notifyObservers();

        this.connectionManager.unregister(this);

        try {

            this.input.close();
            this.output.close();
            this.socket.close();

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Cannot close the connection! " + e.getMessage());

        }
    }

    /**
     * <p>Provides the listening operation.</p>
     */
    @Override
    public void listen() {

        this.listening = true;
        this.running = true;

        notifyObservers();

        while (listening) {

            try {
                if(autoClose) {

                    this.autoCloserWhenNotusing.reset();
                }

                IMessage message = (IMessage) input.readObject();

                this.autoCloserWhenNotusing.stop();

                if(message instanceof QuitMessage) {

                    ((QuitMessage) message).execute(this);
                }

                if(!authenticated) {

                    this.authenticationHandlers.handle(this, message);

                } else {

                    this.handlers.handle(this, message);
                }

            } catch (SocketException | EOFException e) {

                LOG.log(Level.SEVERE, "Connection interrupted!" + e.getMessage());
                this.solve(this, EErrorType.CANNOT_ACCEPT_MESSAGE);

            } catch (IOException | ClassNotFoundException e) {

                LOG.log(Level.SEVERE, "Message was malformed probably! " + e.getMessage());
                this.solve(this, EErrorType.CANNOT_ACCEPT_MESSAGE);
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

                LOG.log(Level.FINE, "Sending message of " + message.getClass().getName());

                this.output.writeObject(message);

            } catch (IOException e) {

                LOG.log(Level.SEVERE, "Cannot send the message " + message.getClass().getName() + ": " + e.getMessage());
                this.solve(this, EErrorType.CANNOT_SEND_MESSAGE);

            } catch (NullPointerException e) {

                LOG.log(Level.SEVERE, "Cannot send the message - connection is closed.");
                this.solve(this, EErrorType.CANNOT_SEND_MESSAGE);
            }
        } else {

            LOG.log(Level.SEVERE, "Message is null!");
        }
    }

    /**
     * <p>Returns the token used for the communication.</p>
     *
     * @return {@link Token} used for the communication.
     */
    @Override
    public Token getToken() {

        return this.token;
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

        listen();
    }

    /**
     * <p>Adds message handler - the connection is gonna
     * be able to use this handler.</p>
     *
     * @param handler handler to be added.
     */
    @Override
    public void addMessageHandler(IHandler handler) {

        this.handlers.add(handler);
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
                notifyObservers();
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
     * Getter for {@link boolean} formed {@code running}
     * of the instance of {@link S2CConnection}
     *
     * @return the value of {@code running}
     * @see boolean
     * @see S2CConnection
     */
    public boolean isRunning() {

        return running;
    }

    /**
     * <p>Gets reference to the message handler container.</p>
     *
     * @return the given message handler container
     */
    @Override
    public HandlerContainer getMessageHandlerContainer() {

        return this.handlers;
    }


    /**
     * <p>Returns the {@link AConnectionManager} instance this
     * connection belongs to.</p>
     *
     * @return the connection manager
     */
    @Override
    public ServiceClientsManagement getConnectionManager() {

        return this.connectionManager;
    }


    /**
     * <p>Returns the service type</p>
     *
     * @return type of the service.
     */
    @Override
    public EServiceType getServiceType() {

        return this.getConnectionManager().getService().getServiceType();
    }



    /**
     * Getter for {@link boolean} formed {@code authenticated}
     * of the instance of {@link S2CConnection}
     *
     * @return the value of {@code authenticated}
     * @see boolean
     * @see S2CConnection
     */
    public boolean isAuthenticated() {

        return authenticated;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/

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
     * <p>Setter for the {@code $field.typeName} formed
     * {@code authenticated} variable.</p>
     *
     * @param authenticated given $field.typeName value to
     *                      be set to the variable
     * @see boolean
     * @see S2CConnection
     */
    public void setAuthenticated(boolean authenticated) {

        if(authenticated) {

            this.connectionManager.register(this);
        }

        this.authenticated = authenticated;
    }

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see S2CConnection
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
    }
}
