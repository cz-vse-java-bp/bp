package cz.vse.java.sign;


import cz.vse.java.mode.ModePicker;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.userData.UserProperties;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code SignGuiController} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 14. 04. 2020
 *
 *
 * @see cz.vse.java
 */
public class SignGuiController implements Initializable, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    TextField userName, routerIP, routerPort;

    @FXML
    PasswordField password, trustStorePassword;

    @FXML
    ProgressBar progressBar;

    @FXML
    Button signBtn, pickTrustStore;

    @FXML
    Label connState;

    private String tsPath;
    private String tsPassword;

    private String rIP;
    private int rP;

    private Thread clientThread;
    private Task task;
    private boolean connected;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link SignGuiController class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Stage STAGE;

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Starts the initialization of the application by clicking at the
     * {@code sign} {@link Button}.</p>
     */
    @FXML
    public void sign() {

        LOG.log(Level.FINE, "User tries to sign");

        UserProperties.getInstance().setUserName(userName.getText());
        UserProperties.getInstance().setPassword(password.getText());


        connState.setText("");
        connState.setStyle("-fx-text-fill: black");

        startProgress("blue");

        if(this.tsPath != null) {

            if(     (routerIP.getText() != null) &&
                    (!routerIP.getText().equals("")) &&
                    (routerPort.getText() != null) &&
                    (!routerPort.getText().equals(""))) {

                try {

                    this.rP = Integer.parseInt(routerPort.getText());
                    this.rIP = routerIP.getText();

                } catch (Exception e) {

                    this.connState.setText("Chybně zadaný port!");
                    this.connState.setStyle("-fx-text-fill: red");

                } finally {

                    this.tsPassword = trustStorePassword.getText();

                    task = new Task<Void>() {

                        @Override
                        protected Void call() {

                            Client client = Client.getInstance();
                            Client.setTrustStore(tsPath, tsPassword);
                            client.prepareConnections(rIP,rP);

                            clientThread = new Thread(client);
                            clientThread.setDaemon(true);
                            clientThread.start();

                            return null;
                        }
                    };

                    Client.getInstance().addObserver(this);

                    Thread t = new Thread(task);
                    t.setDaemon(true);
                    t.start();
                }
                startProgress("orange");
                connState.setText("Připojuji");
            }


        } else {

            this.connState.setText("Nebyl vybrán TrustStore!");
            this.connState.setStyle("-fx-text-fill: red;");
            startProgress("red");
        }
    }


    /**
     * <p>Sets the path of the TrustStore used in future.</p>
     */
    @FXML
    public void pickTrustStore() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TrustStore files", "*.jts")
        );

        File dfDir = new File(
                "C:\\Users\\user\\Desktop\\skola\\BP\\Projects\\GUI\\src\\main\\Resources");

        if(dfDir.exists() && dfDir.isDirectory()) {

            fileChooser.setInitialDirectory(dfDir);
        }

        File selectedFile = fileChooser.showOpenDialog(STAGE);

        try {

            this.tsPath = selectedFile.getAbsolutePath();

        } catch (NullPointerException e) {

            LOG.log(Level.FINE, "User changed his mind about setting truststore");
        }
    }


    /**
     * <p>Updates the progress bar with given color.</p>
     * @param color to be set as default for the progress bar
     */
    public void startProgress(String color) {

        progressBar.setStyle("-fx-accent: " + color + ";");

        Task task = new Task<Void>() {

            @Override
            public Void call() {

                updateProgress(-1F, -1F);
                return null;
            }
        };

        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());
    }


    /**
     * <p>Stops the progress bar.</p>
     */
    public void stopProgress(String color) {

        progressBar.setStyle("-fx-accent: " + color + ";");

        Task task = new Task<Void>() {

            @Override
            public Void call() {

                updateProgress(1, 1);
                return null;
            }
        };

        new Thread(task).start();
        progressBar.progressProperty().bind(task.progressProperty());
    }


    /**
     * <p>Initializes the controller.</p>
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.password.setText("password");
        this.trustStorePassword.setText("changeit");
        this.userName.setText("jira00");
        this.routerPort.setText("888");
        this.routerIP.setText("localhost");
        this.connState.setText("");

        this.startProgress("blue");

        this.connected = false;
    }


    @Override
    public void update() {

        try {
            Task t = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException {

                    if (Client.getInstance().getRouter().getAuthenticationState()) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                connState.setText("Připojeno!");
                            }
                        });
                        connState.setStyle("-fx-text-fill: green");
                        progressBar.setStyle("-fx-accent: green");
                        connected = true;
                        for (int i = 0; i < 80; i++) {

                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                                break;
                            }

                            updateProgress(i + 1, 10);
                        }

                    } else {

                        connState.setStyle("-fx-text-fill: red");
                        progressBar.setStyle("-fx-accent: red");
                    }

                    return null;
                }
            };


            progressBar.progressProperty().bind(t.progressProperty());
            new Thread(t).start();

        } catch (Exception e) {

            System.err.println("Problem while animating...");
        }

        if(connected) {

            Client.getInstance().removeObserver(this);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    try {

                        ModePicker.setPreviousStage(STAGE);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/modePick.fxml"));
                        Parent parent = loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.setResizable(false);
                        stage.setTitle("Výběr role");
                        stage.show();

                        STAGE.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Stage} formed
     * {@code STAGE} variable.</p>
     *
     * @param STAGE given Stage value to
     *              be set to the variable
     * @see Stage
     * @see SignGuiController
     */
    public static void setSTAGE(Stage STAGE) {

        SignGuiController.STAGE = STAGE;
    }
}
