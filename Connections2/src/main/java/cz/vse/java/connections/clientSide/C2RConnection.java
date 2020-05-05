package cz.vse.java.connections.clientSide;


import cz.vse.java.connections.utils.*;
import cz.vse.java.connections.utils.management.*;
import cz.vse.java.connections.utils.problemSolvers.*;
import cz.vse.java.connections.utils.problemSolvers.utils.*;
import cz.vse.java.handlers.*;
import cz.vse.java.handlers.utils.*;
import cz.vse.java.messages.AuthMeMessage;
import cz.vse.java.messages.utils.*;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.SSLClientConfigManager;
import cz.vse.java.util.Token;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.observerDP.ISubject;

import javax.net.ssl.SSLSocket;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.*;


/*********************************************************************
 * <p>The class of {@code C2RConnection} is used to abstractly define
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
public class C2RConnection extends AConnectionWithRouter implements ICSConnection, ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    private boolean listening = false;
    private boolean amIAuthenticated = false;
    private final ArrayList<IObserver> observers = new ArrayList<>();
    private AConnectionManager connectionManager;

    private String ip;
    private int port;

    private EServiceType otherSideServiceType = EServiceType.ROUTER;

    private SSLSocket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private HandlerContainer handlers;

    private ProblemSolverContainer problemSolvers;

    private Token token = new Token("");


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link C2RConnection class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public C2RConnection(String ip,
                         int port,
                         String pathToTrustStore,
                         String passwordToTrustStore,
                         AConnectionManager connectionManager) {

        LOG.log(Level.INFO, "Creating the connection with ROUTER at " + ip + " at port " + port);


        this.ip = ip;
        this.port = port;
        this.connectionManager = connectionManager;

        try {
            this.socket = initSocket(pathToTrustStore, passwordToTrustStore);
            initConnection();
        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Cannot create the socket: " + e.getMessage());
        }

        this.handlers = new HandlerContainer();
        this.handlers.add(new AuthResultContainerHandler(handlers));
        this.handlers.add(new ErrorMessageHandler(handlers));
        this.handlers.add(new FingerPrintContainerRequestHandler(handlers));
        this.handlers.add(new PasswordRequestHandler(handlers));
        this.handlers.add(new QuitMessageHandler(handlers));
        this.handlers.add(new RolesContainerMessageHandler(handlers));
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

        this.addObserver(Client.getInstance());
    }


    /* *****************************************************************/
    /* Instance methods ************************************************/


    private void initConnection() throws IOException {

        this.socket.startHandshake();

        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(this.socket.getInputStream());
    }


    private SSLSocket initSocket(String pathToTruststore, String passwordToTrustStore) throws IOException {

        SSLClientConfigManager config = SSLClientConfigManager.getInstance();
        config.init(pathToTruststore, passwordToTrustStore);

        SSLSocket socket = (SSLSocket) config.getSSLSocketFactory().createSocket(ip, port);

        socket.setEnabledProtocols(config.getEnabledProtocols());
        socket.setEnabledCipherSuites(config.getEnabledCiphers());

        return socket;
    }

    /**
     * <p>Adds the {@link IObserver} to the field.</p>
     *
     * @param observer the listener to any change.
     */
    @Override
    public void addObserver(IObserver observer) {

        synchronized (this.observers) {

            this.observers.add(observer);
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
     * <p>Closes the connection channel.</p>
     */
    @Override
    public void close() {

        LOG.log(Level.INFO, "Closing the connection with ROUTER");
        listening = false;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot close the connection!");
        }
        LOG.log(Level.INFO, "Connection with ROUTER closed");
        notifyObservers();
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
     * <p>Provides the listening operation.</p>
     */
    @Override
    public void listen() {

        listening = true;

        this.send(new AuthMeMessage());

        while(listening) {

            try {
                IMessage message = (IMessage) input.readObject();

                Client.getInstance().addReceivedMessage(message);

                this.handlers.handle(this, message);

            } catch (SocketException | EOFException e) {

                LOG.log(Level.SEVERE, "Connection interrupted: " + e.getMessage());
                this.solve(EErrorType.CONNECTION_INTERRUPTED);

            } catch (IOException | ClassNotFoundException e) {

                LOG.log(Level.SEVERE, "Message malformed: " + e.getMessage());
                this.solve(EErrorType.WRONG_MESSAGE);
            }
        }
    }

    /**
     * <p>Method responsible for problem solving.</p>
     *
     * @param connection    the problem solves
     * @param type          type of error
     * @return if the given type of error is solvable
     */
    @Override
    public boolean solve(IConnection connection, EErrorType type) {

        if(connection.equals(this)) {

            return this.problemSolvers.solve(connection, type);

        } else {

            throw new IllegalArgumentException("Connection does not correspond!");

        }
    }


    /**
     * <p>Local and easier access to method of
     * {@link C2RConnection#solve(IConnection, EErrorType)}, but does the same.</p>
     *
     * @param type  of the error
     */
    private void solve(EErrorType type) {

        if(!this.solve(this, type)) {

            this.close();
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
                LOG.log(Level.INFO, "Sending " + message.getClass().getName());
            } catch (IOException e) {

                LOG.log(Level.SEVERE, "Cannot send the message " + message.getClass().getName() + ": " + e.getMessage());
                close();

            } catch (NullPointerException e) {

                LOG.log(Level.SEVERE, "Cannot send the message - connection is closed.");
                close();
            }
        } else {

            LOG.log(Level.SEVERE, "Message is null!");
        }
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
     * <p>Returns {@link EServiceType} of the service this connection
     * is communicating with.</p>
     *
     * @return  {@link EServiceType}
     */
    @Override
    public EServiceType getOtherSideServiceType() {

        return otherSideServiceType;
    }

    /**
     * <p>Returns the token used for the communication.</p>
     *
     * @return token used for communication.
     */
    @Override
    public Token getToken() {

        return this.token;
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

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see C2RConnection
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
    }

    /**
     * <p>Sets the authentication result.</p>
     *
     * @param result result of the authentication
     *               process.
     */
    @Override
    public void setAmIAuthenticated(boolean result) {

        this.amIAuthenticated = result;
        this.notifyObservers();
    }
}
