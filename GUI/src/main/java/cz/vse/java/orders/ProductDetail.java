package cz.vse.java.orders;


import cz.vse.java.messages.AddPreOrderItem;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.persistance.entities.EProductStamp;
import cz.vse.java.util.persistance.entities.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code ProductDetail} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 15. 04. 2020
 *
 *
 * @see cz.vse.java.orders
 */
public class ProductDetail implements Initializable, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    private Button
            addQ,
            subQ,
            addToCart;

    @FXML
    private Label
            productName,
            description,
            price,
            question,
            max;


    @FXML
    private ListView<String> categories;

    @FXML
    private TextField currentQuant;

    private int currentNum = 0;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link ProductDetail class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    private static Product PRODUCT;

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Client.getInstance().addObserver(this);

        this.currentQuant.setText(Integer.toString(0));
        this.currentQuant.setDisable(true);
        this.subQ.setDisable(true);
        this.addToCart.setDisable(true);

        if(PRODUCT.getQuantity() < 1) {

            addQ.setDisable(true);
        }

        this.addToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String ident = Client.getInstance().getPreOrder().getIdentificator();

                Client.getInstance().addMessageTask(new MessageTask(

                        new AddPreOrderItem(

                                PRODUCT,
                                currentNum,
                                ident),

                        EServiceType.ORDER_MANAGEMENT)
                );

                Stage stage = (Stage) addToCart.getScene().getWindow();
                stage.close();
            }
        });

        this.categories.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                categories.getSelectionModel().clearSelection();
            }
        });

        this.addQ.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(currentNum < (PRODUCT.getQuantity()-1)) {

                    currentNum++;
                    currentQuant.setText(Integer.toString(currentNum));
                    updateQuestion();
                    subQ.setDisable(false);
                    addToCart.setDisable(false);

                } else if(currentNum == (PRODUCT.getQuantity()-1)) {

                    currentNum++;
                    currentQuant.setText(Integer.toString(currentNum));
                    updateQuestion();
                    addQ.setDisable(true);
                    subQ.setDisable(false);
                    addToCart.setDisable(false);
                }
            }
        });

        this.subQ.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(currentNum > 1) {

                    currentNum--;
                    currentQuant.setText(Integer.toString(currentNum));
                    addQ.setDisable(false);
                    updateQuestion();
                    addToCart.setDisable(false);

                } else if(currentNum == 1) {

                    currentNum--;
                    currentQuant.setText(Integer.toString(currentNum));
                    addQ.setDisable(false);
                    subQ.setDisable(true);
                    addToCart.setDisable(true);
                    updateQuestion();
                }
            }
        });

        update();
    }

    @Override
    public void update() {

        List<Product> products = Client.getInstance().getProducts().getProducts();

        for (Product product : products) {

            if(product.getId().equals(PRODUCT.getId())) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        productName.setText(product.getProductName());
                        description.setText(product.getShortDesc());
                        description.setTooltip(new Tooltip(product.getLongDesc()));
                        price.setText(product.getPrice().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
                        max.setText(String.format("Max: %d jednotek", product.getQuantity()));

                        if(currentNum > 0) {

                            addToCart.setDisable(false);

                        } else {

                            addToCart.setDisable(true);
                        }

                        updateStamps();
                        updateQuestion();

                        if(currentNum > product.getQuantity()) {

                            currentNum = product.getQuantity();
                            addQ.setDisable(true);
                        }
                    }
                });
            }
        }
    }


    public void updateQuestion() {

        this.question.setText(String.format(
                "Přejete si přidat %d kusů '%s' do košíku?",
                currentNum,
                PRODUCT.getProductName())
        );
    }

    public void updateStamps() {

        this.categories.getItems().clear();

        ObservableList<String> stamps = FXCollections.observableArrayList();

        for (EProductStamp ps : PRODUCT.getStamps()) {

            stamps.add(ps.getName());
        }

        this.categories.getItems().addAll(stamps);
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/

    /**
     * <p>Setter for the {@code Product} formed
     * {@code PRODUCT} variable.</p>
     *
     * @param PRODUCT given Product value to
     *                be set to the variable
     * @see Product
     * @see ProductDetail
     */
    public static void setPRODUCT(Product PRODUCT) {

        ProductDetail.PRODUCT = PRODUCT;
    }

    /* *****************************************************************/
    /* Main method *****************************************************/


    /**
     * The main method of the class of ProductDetail.
     *
     */
  /*  public static void main(String[] args){
        
        System.err.println(">>> QuickTest: ProductDetail class");
        System.err.println(">>> Creating ProductDetail instance...");
        ProductDetail instance = new ProductDetail();
        
        System.out.println(instance.toString());
        
        
        //code
        
        
        System.err.println(">>> Creation successfull...");
    }
    
    */
}
