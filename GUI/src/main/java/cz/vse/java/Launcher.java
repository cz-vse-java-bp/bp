package cz.vse.java;


import cz.vse.java.sign.SignGuiController;
import cz.vse.java.util.Token;
import cz.vse.java.utils.random.RandomNumberGenerator;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Launcher} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 01. 04. 2020
 *
 *
 * @see cz.vse.java
 */
public class Launcher extends Application {


    /* *****************************************************************/
    /* Instance variables **********************************************/



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Launcher class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void start(Stage stage) throws Exception {

        SignGuiController.setSTAGE(stage);
        Parent root = FXMLLoader.load(getClass().getResource("/sign.fxml"));
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Přihlášení");
        stage.show();
    }


    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of Launcher.
     *
     */
   public static void main(String[] args){
        
        System.err.println(">>> QuickTest: Launcher class");
        System.err.println(">>> Creating Launcher instance...");

        launch(args);
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
}
