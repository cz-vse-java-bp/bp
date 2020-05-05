package cz.vse.java.admin;


import cz.vse.java.admin.utils.ProductSealer;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.EProductStamp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code StampsPicker} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 01. 05. 2020
 *
 *
 * @see cz.vse.java.admin
 */
public class StampsPicker implements Initializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    private Button accept;

    @FXML
    private ListView<String> stampsList;

    @FXML
    private Label stampsProductName;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link StampsPicker class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    private static ProductSealer PRODUCT_SEALER;

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        this.stampsList.getItems().clear();

        for (EProductStamp ps : EProductStamp.values()) {

            this.stampsList.getItems().add(ps.getName());
        }

        this.stampsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        if(PRODUCT_SEALER != null) {

            stampsProductName.setText(PRODUCT_SEALER.getProductName());

        } else {

            stampsProductName.setText("");
        }

        this.stampsList.setCellFactory(stampsLV -> {

            ListCell<String> stamp = new ListCell<String>() {

                @Override
                public void updateItem(String item, boolean empty) {

                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            stamp.addEventFilter(
                    MouseEvent.MOUSE_PRESSED,
                    e -> {

                        if (stamp.isEmpty()) {

                            System.out.println("nevybrano");
                            return;
                        }

                        int indexOfItem = stamp.getIndex();

                        if (stampsList.getSelectionModel().getSelectedIndices().contains(indexOfItem)) {

                            stampsList.getSelectionModel().clearSelection(indexOfItem);

                        } else {

                            stampsList.getSelectionModel().select(indexOfItem);
                        }

                        stampsList.requestFocus();

                        e.consume();
                    });

            return stamp;
        });

        for (EProductStamp ps : PRODUCT_SEALER.getProduct().getStamps()) {

            for (int i = 0; i < stampsList.getItems().size(); i++) {

                if(stampsList.getItems().get(i).equals(ps.getName())) {

                    stampsList.getSelectionModel().select(i);
                    break;
                }
            }
        }

        this.accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                List<EProductStamp> stamps = new ArrayList<>();

                for (String s : stampsList.getSelectionModel().getSelectedItems()) {

                    stamps.add(EProductStamp.getStamp(s));
                }

                if(PRODUCT_SEALER.getProduct() != null) {

                    PRODUCT_SEALER.getProduct().setStamps(stamps);
                }

                Stage stage = (Stage) accept.getScene().getWindow();
                stage.close();
            }
        });
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/

    /**
     * Getter for {@link ProductSealer} formed {@code PRODUCT_SEALER}
     * of the instance of {@link StampsPicker}
     *
     * @return the value of {@code PRODUCT_SEALER}
     * @see ProductSealer
     * @see StampsPicker
     */
    public static ProductSealer getProductSealer() {

        return PRODUCT_SEALER;
    }

    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code ProductSealer} formed
     * {@code PRODUCT_SEALER} variable.</p>
     *
     * @param productSealer given ProductSealer value to
     *                       be set to the variable
     * @see ProductSealer
     * @see StampsPicker
     */
    public static void setProductSealer(ProductSealer productSealer) {

        PRODUCT_SEALER = productSealer;
    }
}
