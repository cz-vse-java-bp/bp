package cz.vse.java.orders;


import cz.vse.java.admin.utils.OrderItemSealer;
import cz.vse.java.connections.clientSide.C2SConnection;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.utils.emailServices.*;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.persistance.entities.EUnit;
import cz.vse.java.util.persistance.entities.Product;
import cz.vse.java.util.persistance.entities.orders.PreOrderItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.text.Element;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Orders} is used to abstractly define
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
public class Orders implements Initializable, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    private Button
            custNumSave,
            lookupBtn,
            resetFilterBtn,
            approveOrder;

    @FXML
    private TextField
            custNum,
            productLookUpField,
            emailField;

    @FXML
    private TextArea orderNote;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private TableView<Product> storageTab;

    @FXML
    private TableColumn<Product, String> productName;
    private TableColumn<Product, BigDecimal> price;
    private TableColumn<Product, Integer> storageQuantity;
    private TableColumn<Product, String>  unit;


    @FXML
    private TableView<PreOrderItem> cartTab;

    @FXML
    private TableColumn<PreOrderItem, String> cartProductName;
    private TableColumn<PreOrderItem, BigDecimal> cartPrice;
    private TableColumn<PreOrderItem, Integer> cartQuantity;

    @FXML
    private ContextMenu storageCM;

    @FXML
    private MenuItem addToCartMI, showDetailsMI, removeOrderItem;

    @FXML
    private Label priceLabel;


    private boolean update = true;
    private Task progressTask;
    private boolean isRunning = false;

    private boolean filtered = false;



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Orders class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Client.getInstance().addObserver(this);

        this.setProgress(false);

        this.priceLabel.setText("Košík prázdný");

        this.orderNote.setText("");
        this.emailField.setText("");
        this.custNum.setText("");

        this.removeOrderItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                PreOrderItem poi = cartTab.getSelectionModel().getSelectedItem();

                if(poi != null) {

                    String identificator = Client.getInstance().getPreOrder().getIdentificator();

                    IMessage remover = new RemovePreOrderItem(
                            poi.getProduct().getId(),
                            identificator
                    );

                    Client.getInstance().addMessageTask(new MessageTask(remover, EServiceType.ORDER_MANAGEMENT));

                    Client.getInstance().getPreOrder().getPreOrderItems().remove(poi);

                    fillCart();
                }
            }
        });


        this.orderNote.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                if(!orderNote.getText().equals("")) {

                    String ident = Client.getInstance().getPreOrder().getIdentificator();
                    Client.getInstance().addMessageTask(new MessageTask(new SetNoteToOrderMessage(orderNote.getText(), ident),
                            EServiceType.ORDER_MANAGEMENT));
                }
            }
        });

        this.emailField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                if(!emailField.getText().equals("")) {

                    String ident = Client.getInstance().getPreOrder().getIdentificator();
                    Client.getInstance().addMessageTask(new MessageTask(new SetContactToOrderMessage(emailField.getText(), ident),
                            EServiceType.ORDER_MANAGEMENT));
                }
            }
        });

        this.custNumSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(!custNum.getText().equals("")) {

                    Client.getInstance().addMessageTask(new MessageTask(

                            new SetSubmitterToOrder(

                                    custNum.getText(),
                                    Client.getInstance().getPreOrder().getIdentificator()),

                            EServiceType.ORDER_MANAGEMENT)
                    );
                }
            }
        });


        approveOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(!emailField.getText().equals("")) {

                    if(EmailAddress.validate(emailField.getText())) {

                        Client.getInstance().addMessageTask(new MessageTask(

                                new SetContactToOrderMessage(

                                        emailField.getText(),
                                        Client.getInstance().getPreOrder().getIdentificator()),

                                EServiceType.ORDER_MANAGEMENT)
                        );


                        Client.getInstance().addMessageTask(new MessageTask(

                                new TryToGenerateOrder(

                                        Client.getInstance().getPreOrder().getIdentificator()),

                                EServiceType.ORDER_MANAGEMENT)
                        );

                        emailField.setText("");
                        orderNote.setText("");
                        custNum.setText("");

                        cartTab.getItems().clear();

                        Client.getInstance().clearReceivedMessages(15L);

                    } else {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle("Chyba!");
                        alert.setHeaderText("Vyplňte prosím pole pro kontakt validní emailovou adresou!");
                        alert.setContentText("Nelze odeslat objednávku bez zadání validní emailové adresy. " +
                                "Nebylo by pak možné Vás kontaktovat o splnění objednávky.");

                        alert.setWidth(600);

                        alert.showAndWait();
                    }

                } else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Chyba!");
                    alert.setHeaderText("Vyplňte prosím pole pro kontakt.");
                    alert.setContentText("Nelze odeslat objednávku bez zadání emailové adresy. " +
                            "Nebylo by pak možné Vás kontaktovat o splnění objednávky.");

                    alert.setWidth(600);
                    alert.showAndWait();
                }
            }
        });


        progressTask = new Task<Void>() {

            @Override
            public Void call() {

                while (update) {

                    if (isRunning) {

                        updateProgress(-1F, 1F);

                    } else {

                        updateProgress(1F, 1F);
                    }

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        Task t = new Task<Void>() {

            @Override
            public Void call() {

                while (update) {

                    setProgress(true);

                    Client.getInstance().addMessageTask(
                            new MessageTask(
                                    new ProductAllRequest(),
                                    EServiceType.STORAGE_MANAGEMENT, 10L));

                    if(Client.getInstance().getPreOrder() != null) {

                        String ident = Client.getInstance().getPreOrder().getIdentificator();

                        if (ident != null) {

                            Client.getInstance().addMessageTask(
                                    new MessageTask(
                                            new GiveMeMyOrder(ident),
                                            EServiceType.ORDER_MANAGEMENT, 10L));
                        }
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        lookupBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                filtered = true;
                productLookUpField.setDisable(true);
                resetFilterBtn.setDisable(false);
                lookupBtn.setDisable(true);

                update();
            }
        });

        resetFilterBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                filtered = false;
                productLookUpField.setDisable(false);
                resetFilterBtn.setDisable(true);
                lookupBtn.setDisable(false);

                update();
            }
        });

        progress.progressProperty().unbind();
        progress.progressProperty().bind(progressTask.progressProperty());

        productName = new TableColumn<>("Název");
        productName.setMinWidth(200);
        productName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        price = new TableColumn<>("Cena za kus");
        price.setMinWidth(100);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        storageQuantity = new TableColumn<>("Skladem");
        storageQuantity.setMinWidth(70);
        storageQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        unit = new TableColumn<>("Jednotka");
        unit.setMinWidth(70);
        unit.setCellValueFactory(new PropertyValueFactory<>("unitString"));

        storageTab.getColumns().clear();
        storageTab.getColumns().addAll(productName, price, storageQuantity, unit);

        cartProductName = new TableColumn<>("Název");
        cartProductName.setMinWidth(200);
        cartProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        cartPrice = new TableColumn<>("Cena/kus");
        cartPrice.setMinWidth(70);
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        cartQuantity = new TableColumn<>("Množství");
        cartQuantity.setMinWidth(80);
        cartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        this.cartTab.getColumns().clear();
        this.cartTab.getColumns().addAll(cartProductName, cartPrice, cartQuantity);

        Client.getInstance().addMessageTask(
                new MessageTask(new ServiceReferenceRequest(EServiceType.STORAGE_MANAGEMENT), EServiceType.ROUTER)
        );

        Client.getInstance().addMessageTask(
                new MessageTask(new ServiceReferenceRequest(EServiceType.ORDER_MANAGEMENT), EServiceType.ROUTER)
        );

        Client.getInstance().addMessageTask(
                new MessageTask(new ProductAllRequest(), EServiceType.STORAGE_MANAGEMENT)
        );

        Client.getInstance().addMessageTask(
                new MessageTask(
                        new UniqueOrderIdentRequest(), EServiceType.ORDER_MANAGEMENT));

        Thread updateTask = new Thread(t);
        Thread progressTaskThread = new Thread(progressTask);
        progressTaskThread.setDaemon(true);
        updateTask.setDaemon(true);
        progressTaskThread.start();
        updateTask.start();
    }



    @Override
    public void update() {


        Product selected = storageTab.getSelectionModel().getSelectedItem();
        int sIndex = 0;

        if(selected != null) {

            sIndex = storageTab.getItems().indexOf(selected);
        }

        C2SConnection conn = (C2SConnection) Client.getInstance()
                .getConnectionWithService(EServiceType.STORAGE_MANAGEMENT);

        if(conn != null && conn.getAmIAuthenticated()){

            fillStorageTable();
        }

        if(selected != null) {

            storageTab.getSelectionModel().select(sIndex);
        }

        conn = (C2SConnection) Client.getInstance()
                .getConnectionWithService(EServiceType.ORDER_MANAGEMENT);

        if(conn != null && conn.getAmIAuthenticated()) {

            fillCart();
        }

        List<IMessage> messages = Client.getInstance().getReceivedByClass(
                OrderTransformationResult.class.getName());

        if(messages.size() > 0) {

            acceptDial(messages);
        }
    }


    public void acceptDial(List<IMessage> messages) {

        Task<Void> task = new Task<Void>() {

            @Override
            public Void call() {

                for (IMessage message : messages) {

                    if(((OrderTransformationResult) message).getContent()) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Objednávka přijata!");
                        alert.setHeaderText(null);
                        alert.setContentText("O jejím průběhu Vás budeme kontaktovat.");

                        alert.showAndWait();

                        break;
                    }
                }

                priceLabel.setText("");
                Client.getInstance().clearReceivedMessages(20L);

                return null;
            }
        };

        Thread t = new Thread(task);
        t.start();
    }


    public void fillStorageTable() {

        List<Product> products = new ArrayList<>();

        if(filtered) {

            products.addAll(
                    Client.getInstance()
                            .getProducts().findProducts(
                            this.productLookUpField.getText()
                    )
            );

        } else {

            products.addAll(
                    Client.getInstance()
                            .getProducts().getProducts()
            );
        }


        ObservableList<Product> olp = FXCollections.observableArrayList();
        olp.addAll(products);

        this.storageTab.getItems().clear();
        this.storageTab.setItems(olp);

        this.storageTab.setRowFactory(tv -> new TableRow<Product>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (product == null) {

                    setTooltip(null);

                } else {
                    tooltip.setText(
                            String.format("Název: %s%n", product.getProductName().replaceAll(".{60}(?=.)", "$0\n")) +
                            String.format("Krátký popis: %s%n", product.getShortDesc()).replaceAll(".{60}(?=.)", "$0\n") +
                            String.format("Popis: %s\n%n", product.getLongDesc()).replaceAll(".{60}(?=.)", "$0\n") +
                            String.format("Cena: %s kč%n", product.getPrice()
                                    .setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
                            )
                    );
                    setTooltip(tooltip);
                }
            }
        });

        setProgress(false);
    }


    public void fillCart() {

        List<PreOrderItem> items = Client.getInstance().getPreOrder().getPreOrderItems();

        ObservableList<PreOrderItem> oPoi = FXCollections.observableArrayList();

        oPoi.addAll(items);

        this.cartTab.getItems().clear();
        this.cartTab.getItems().addAll(oPoi);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                BigDecimal price = new BigDecimal(0);

                if(cartTab.getItems().size() > 0) {


                    for (PreOrderItem poi : items) {

                        price = price.add(poi.getPrice().multiply(BigDecimal.valueOf(poi.getQuantity())));
                    }

                    priceLabel.setText(NumberFormat.getCurrencyInstance().format(price));
                }

                if(price.equals(BigDecimal.ZERO)) {

                    priceLabel.setText("Košík je prázdný");
                }
            }
        });
    }



    public void handleShowDetails(ActionEvent actionEvent) {

        Product p = storageTab.getSelectionModel().getSelectedItem();

        if(p != null) {

            showDetailPick(p);
        }
    }


    public void handleAddToCart(ActionEvent actionEvent) {

        Product p = storageTab.getSelectionModel().getSelectedItem();

        if(p != null) {

            showDetailPick(p);
        }
    }


    public void showDetailPick(Product product) {

        ProductDetail.setPRODUCT(product);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/productDetail.fxml"));
        Parent parent = null;

        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setResizable(false);
        stage.setTitle("Detail produktu");
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


    public void setProgress(boolean run) {

        this.isRunning = run;
    }
}
