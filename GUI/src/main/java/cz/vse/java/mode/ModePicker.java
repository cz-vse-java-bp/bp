package cz.vse.java.mode;


import cz.vse.java.messages.GiveMeRoles;
import cz.vse.java.messages.RolesContainerMessage;
import cz.vse.java.messages.ServiceReferenceRequest;
import cz.vse.java.messages.UniqueOrderIdentRequest;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.userData.ERole;
import cz.vse.java.util.userData.UserProperties;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ModePicker} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 *
 * @see cz.vse.java.mode
 */
public class ModePicker implements Initializable, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    ComboBox<String> modeCB;

    @FXML
    Button chooseBtn;

    @FXML
    ProgressIndicator progressInd;

    private String mode;

    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ModePicker class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Stage PREVIOUS_STAGE;

    private static Stage CURRENT_STAGE;

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Client.getInstance().addObserver(this);

        progressInd.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        this.chooseBtn.setDisable(true);
        this.modeCB.setDisable(true);
        this.modeCB.valueProperty().addListener(new ChangeListener<String>() {

            @Override public void changed(ObservableValue ov, String t, String t1) {

                mode = t1;
            }
        });


        Client.getInstance().addMessageTask(
                new MessageTask(
                    new GiveMeRoles(UserProperties.getInstance().getUserName()),
                    EServiceType.ROUTER
                )
        );
    }


    @Override
    public void update() {

        List<ERole> roles = new ArrayList<>();

        List<IMessage> messageList = Client.getInstance()
                .getReceivedByClass(RolesContainerMessage.class.getName());

        for (IMessage message : messageList) {

            RolesContainerMessage rcm = (RolesContainerMessage) message;

            for (ERole role : rcm.getContent()) {

                if(!roles.contains(role)) {

                    roles.add(role);
                }
            }
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                modeCB.setDisable(false);
                chooseBtn.setDisable(false);
                modeCB.getItems().clear();

                for (ERole role : roles) {

                    modeCB.getItems().add(role.getRoleName());
                }
                progressInd.setProgress(1);
            }
        });

        this.chooseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(mode != null) {

                    if (!mode.equals("")) {

                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                        switch (mode) {

                            case "ZÁKAZNÍK": {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {

                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/orders.fxml"));
                                            Parent parent = loader.load();
                                            stage.setScene(new Scene(parent));
                                            stage.setResizable(false);
                                            stage.setTitle("Title");

                                            PREVIOUS_STAGE.close();

                                            stage.show();


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                break;
                            }

                            case "ADMIN": {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {

                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin2.fxml"));
                                            Parent parent = loader.load();
                                            stage.setScene(new Scene(parent));
                                            stage.setResizable(false);
                                            stage.setTitle("Administrativa");

                                            PREVIOUS_STAGE.close();

                                            stage.show();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                break;
                            }

                            case "SKLADNIK": {

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {

                                            Client.getInstance().addMessageTask(new MessageTask(
                                                    new ServiceReferenceRequest(EServiceType.TASK_SERVICE),
                                                    EServiceType.ROUTER)
                                            );

                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tasks.fxml"));
                                            Parent parent = loader.load();
                                            stage.setScene(new Scene(parent));
                                            stage.setResizable(false);
                                            stage.setTitle("Aplikace skladník");

                                            PREVIOUS_STAGE.close();

                                            stage.show();


                                        } catch (IOException e) {

                                            e.printStackTrace();
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            }
        });
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Stage} formed
     * {@code PREVIOUS_STAGE} variable.</p>
     *
     * @param previousStage given Stage value to
     *                       be set to the variable
     * @see Stage
     * @see ModePicker
     */
    public static void setPreviousStage(Stage previousStage) {

        PREVIOUS_STAGE = previousStage;
    }

    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of ModePicker.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: ModePicker class");
        System.err.println(">>> Creating ModePicker instance...");
        ModePicker instance = new ModePicker();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
