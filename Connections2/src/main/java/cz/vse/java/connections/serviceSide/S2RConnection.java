package cz.vse.java.connections.serviceSide;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.connections.utils.ISSConnection;
import cz.vse.java.connections.utils.management.AConnectionManager;
import cz.vse.java.connections.utils.management.AConnectionWithRouter;
import cz.vse.java.connections.utils.problemSolvers.*;
import cz.vse.java.connections.utils.problemSolvers.utils.ESolveMethod;
import cz.vse.java.connections.utils.problemSolvers.utils.ProblemSolverContainer;
import cz.vse.java.handlers.*;
import cz.vse.java.handlers.utils.HandlerContainer;
import cz.vse.java.handlers.utils.IHandler;
import cz.vse.java.messages.AuthMeMessage;
import cz.vse.java.messages.utils.EErrorType;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.services.serverSide.AGeneralService;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.services.references.EReferenceFor;
import cz.vse.java.services.references.ServiceReference;
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
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code S2RConnection} is used to abstractly define
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
public class S2RConnection extends AConnectionWithRouter implements ISSConnection, ISubject {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    private boolean listening = false;
    private boolean amIAuthenticated = false;

    private final ArrayList<IObserver> observers;

    private String ip;
    private int port;

    private EServiceType otherSideServiceType = EServiceType.ROUTER;

    private SSLSocket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final HandlerContainer handlers;

    private ProblemSolverContainer problemSolvers;

    private Token token = new Token("");

    private AConnectionManager connectionManager;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link S2RConnection class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/

    public S2RConnection(String ip,
                         int port,
                         AConnectionManager connectionManager,
                         String pathToTrustStore,
                         String passwordToTrustStore) throws IOException {

        LOG.log(Level.INFO, "Creating the connection with ROUTER at " + ip + " at port " + port);

        this.ip = ip;
        this.port = port;
        this.connectionManager = connectionManager;
        this.observers = new ArrayList<>();
        this.addObserver((AGeneralService) this.getConnectionManager().getService());

        this.socket = initSocket(pathToTrustStore, passwordToTrustStore);
        initConnection();


        this.handlers = new HandlerContainer();
        this.handlers.add(new AuthResultContainerHandler(handlers));
        this.handlers.add(new ErrorMessageHandler(handlers));
        this.handlers.add(new FingerPrintContainerRequestHandler(handlers));
        this.handlers.add(new QuitMessageHandler(handlers));
        this.handlers.add(new ServiceReferenceContainerHandler(handlers));
        this.handlers.add(new ServiceReferenceRequestHandler(handlers));
        this.handlers.add(new ServiceTypeContainerHandler(handlers));
        this.handlers.add(new ServiceTypeRequestHandler(handlers));
        this.handlers.add(new TextMessageHandler(handlers));
        this.handlers.add(new UseTokenHandler(handlers));

        this.problemSolvers = new ProblemSolverContainer();
        this.problemSolvers.add(new ConnectionInterrupted(ESolveMethod.CLOSE));
        this.problemSolvers.add(new WrongMessage(ESolveMethod.CLOSE));
        this.problemSolvers.add(new WrongFingerPrint(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new CannotAuthenticate(ESolveMethod.POLITE_CLOSE));
        this.problemSolvers.add(new WrongToken(ESolveMethod.CLOSE));
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

            if(!observers.contains(observer)) {

                this.observers.add(observer);
            }
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

        LOG.log(Level.INFO, "Closing the connection with ROUTER");
        listening = false;
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Cannot close the connection!");

        } finally {

            this.notifyObservers();
        }
        LOG.log(Level.INFO, "Connection with ROUTER closed");
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
                e.printStackTrace();
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

    private void solve(EErrorType type) {

        if(!this.solve(this, type)) {

            this.close();
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
     * Getter for {@link EServiceType} formed {@code otherSideServiceType}
     * of the instance of {@link S2RConnection}
     *
     * @return the value of {@code otherSideServiceType}
     * @see EServiceType
     * @see S2RConnection
     */
    public EServiceType getOtherSideServiceType() {

        return otherSideServiceType;
    }

    /**
     * Getter for {@link boolean} formed {@code listening}
     * of the instance of {@link S2RConnection}
     *
     * @return the value of {@code listening}
     * @see boolean
     * @see S2RConnection
     */
    public boolean isListening() {

        return listening;
    }


    /**
     * Getter for {@link String} formed {@code ip}
     * of the instance of {@link S2RConnection}
     *
     * @return the value of {@code ip}
     * @see String
     * @see S2RConnection
     */
    public String getIp() {

        return ip;
    }

    /**
     * Getter for {@link int} formed {@code port}
     * of the instance of {@link S2RConnection}
     *
     * @return the value of {@code port}
     * @see int
     * @see S2RConnection
     */
    public int getPort() {

        return port;
    }

    /**
     * <p>Returns the service type</p>
     *
     * @return type of the service.
     */
    @Override
    public EServiceType getServiceType() {

        return this.connectionManager.getService().getServiceType();
    }

    public ServiceReference getServiceReference() {

        AGeneralService gs = (AGeneralService) this.connectionManager.getService();
        return new ServiceReference(
                EReferenceFor.CLIENT,
                gs.getClientsIP(),
                gs.getClientsPort(),
                gs.getServiceType()
        );
    }

    /**
     * Getter for {@link Token} formed {@code token}
     * of the instance of {@link S2RConnection}
     *
     * @return the value of {@code token}
     * @see Token
     * @see S2RConnection
     */
    @Override
    public Token getToken() {

        return token;
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
     * <p>gets the authentication result</p>
     *
     * @return result of the authentication process
     * on the other side of the connection
     */
    @Override
    public boolean getAmIAuthenticated() {

        return this.amIAuthenticated;
    }


    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code listening} variable.</p>
     *
     * @param listening given $field.typeName value to
     *                  be set to the variable
     * @see boolean
     * @see S2RConnection
     */
    public void setListening(boolean listening) {

        this.listening = listening;
    }

    /**
     * <p>Setter for the {@code $field.typeName} formed
     * {@code amIAuthenticated} variable.</p>
     *
     * @param amIAuthenticated given $field.typeName value to
     *                         be set to the variable
     * @see boolean
     * @see S2RConnection
     */
    public void setAmIAuthenticated(boolean amIAuthenticated) {

        this.amIAuthenticated = amIAuthenticated;
    }

    /**
     * <p>Setter for the {@code Token} formed
     * {@code token} variable.</p>
     *
     * @param token given Token value to
     *              be set to the variable
     * @see Token
     * @see S2RConnection
     */
    @Override
    public void setToken(Token token) {

        this.token = token;
    }

    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of S2RConnection.
     *
     */
    public static void main(String[] args){

        System.err.println(">>> QuickTest: S2RConnection class");
        System.err.println(">>> Creating S2RConnection instance...");

        //S2RConnection connection = new S2RConnection("localhost", 888, "C:\\Users\\user\\Desktop\\skola\\BP\\Projects\\Connections2\\src\\main\\Resources\\trustStore.jts", "changeit");

        //new Thread(connection).start();
        //code


        System.err.println(">>> Creation successfull...");
    }
}
