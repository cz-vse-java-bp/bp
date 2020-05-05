package cz.vse.java.admin;


import cz.vse.java.admin.utils.*;
import cz.vse.java.orders.ProductDetail;
import cz.vse.java.util.persistance.entities.*;
import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.persistance.service.*;
import cz.vse.java.utils.cryptography.hashing.EHashAlgorithm;
import cz.vse.java.utils.cryptography.hashing.Hasher;
import cz.vse.java.util.database.DBConnection;
import cz.vse.java.util.database.DatabaseConnectionContainer;
import cz.vse.java.util.database.EDBUse;
import cz.vse.java.util.persistance.entities.tasks.Task;
import cz.vse.java.utils.random.Charsets;
import cz.vse.java.utils.random.RandomStringGenerator;
import cz.vse.java.util.userData.ERole;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Admin} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 17. 04. 2020
 *
 *
 * @see cz.vse.java.admin
 */
public class Admin implements Initializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/


    @FXML
    private Tab
            DBTab,
            taskTab,
            empTab,
            storageTab,
            ordersTab;

    @FXML
    private Button
            addEmployeeBtn,
            updateBtn,
            addProduct,
            userClearBtn,
            dbLogin,
            clearSelectedProduct,
            taskClear,
            taskNewAccept,
            ordersShowAll,
            ordersSendNotification,
            ordersShowDone,
            stamps;

    @FXML
    private ProgressIndicator
            progress;

    @FXML
    private ComboBox<String>
            prodUnitCB,
            taskEmplNew,
            taskStateNew;

    @FXML
    private TextField
            firstname,
            lastname,
            username,
            dbUser,
            prodBarcodeTF,
            prodNameTF,
            prodShortDescTF,
            prodLongDescTF,
            prodPriceTF,
            prodQuantityTF,
            prodLocationTF,
            dbAdd;

    @FXML
    private TextArea taskDescNew;

    @FXML
    private PasswordField
            dbPass,
            password;

    @FXML
    private MenuItem
            removeProduct,
            deleteOrder,
            deleteOrderItem;

    @FXML
    private ListView<String>
            roles,
            taskOrderItemNew;

    @FXML
    private TreeView<String> orderItemTree;

    @FXML
    private TableView<UserSealer>
            employeesTable;

    @FXML
    private TableView<TaskSealer>
            taskTable;

    @FXML
    private TableView<OrderSealer>
            orderTable;

    @FXML
    private TableColumn<OrderSealer, Long>          orderIDCol;
    private TableColumn<OrderSealer, String>        orderContactCol, orderSubmitterCol, orderNoteCol, orderStateCol;

    @FXML
    private TableView<ProductSealer>
            productsTable;

    @FXML
    private TableColumn<UserSealer, Long>           userID;
    private TableColumn<UserSealer, String>         userFirstname,
                                                    userLastname,
                                                    userUsername;
    private TableColumn<UserSealer, LocalDate>      dateOfCreation;

    @FXML
    private TableColumn<ProductSealer, Long>        prodID;
    private TableColumn<ProductSealer, String>      prodName,
                                                    prodBarcode,
                                                    prodShortDesc,
                                                    prodLongDesc,
                                                    prodUnit,
                                                    prodLocation;
    private TableColumn<ProductSealer, Integer>     prodQuant;
    private TableColumn<ProductSealer, BigDecimal>  prodPrice;



    @FXML
    private TableColumn<TaskSealer, Long>           taskID,
                                                    taskOIID;
    private TableColumn<TaskSealer, String>         taskUsername,
                                                    taskState,
                                                    taskDesc;


    @FXML
    private TableView<OrderItemSealer>              orderItemTable;

    @FXML
    private TableColumn<OrderItemSealer, Long>      orderItemID;
    private TableColumn<OrderItemSealer, String>    orderItemProductName;
    private TableColumn<OrderItemSealer, Integer>   orderItemQuantity;
    private TableColumn<OrderItemSealer, BigDecimal> orderItemPrice, orderItemUnitPrice;


 /*
    @FXML
    private TableColumn<ProductSealer, >
    private TableColumn<ProductSealer, >
    private TableColumn<ProductSealer, >
    private TableColumn<ProductSealer, >
*/


  //  private Task

    private boolean running = true;
    private Thread updateThread;

    private javafx.concurrent.Task progressTask;
    private boolean isInProgress = false;

    private UserSealer userSealer = null;

    private boolean dbReady = false;
    private boolean passwordChange = false;

    private ProductSealer ps;
    private TaskSealer ts;



    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Admin class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    /* *****************************************************************/
    /* Instance methods ************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/



    /* *****************************************************************/
    /* Initialization **************************************************/


    /**
     * <p>Initializes the main pane of the {@link Admin} GUI.</p>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        running = true;
        isInProgress = true;

        dbAdd.setText("jdbc:h2:tcp://localhost/~/test");
        dbUser.setText("sa");
        dbPass.setText("");

        deleteOrderItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                OrderItemSealer ois = orderItemTable.getSelectionModel().getSelectedItem();

                if(ois != null) {

                    if(ois.getOi() != null) {

                        for (Task t : ois.getOi().getTasks()) {

                            try {

                                new TaskService().delete(t);

                            } catch (SQLException e) {

                                LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                            }
                        }

                        try {

                            new OrderItemService().delete(ois.getOi());

                        } catch (SQLException e) {

                            LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                        }
                    }

                    OrderSealer os = orderTable.getSelectionModel().getSelectedItem();

                    if(os != null) {

                        updateOrderItems(os);
                    }

                    updateData();
                }
            }
        });

        deleteOrder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                OrderSealer os = orderTable.getSelectionModel().getSelectedItem();

                if(os != null) {

                    Order o = os.getOrder();

                    for (OrderItem oi : o.getOrderItems()) {

                        for (Task t : oi.getTasks()) {

                            try {

                                new TaskService().delete(t);

                            } catch (SQLException e) {

                                LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                            }
                        }

                        try {

                            new OrderItemService().delete(oi);

                        } catch (SQLException e) {

                            LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                        }
                    }

                    try {

                        new OrderService().delete(o);

                    } catch (SQLException e) {

                        LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
                    }

                    updateData();
                }
            }
        });

        orderTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                OrderSealer os = orderTable.getSelectionModel().getSelectedItem();

                if(os != null) {

                    updateOrderItems(os);
                }
            }
        });


        ordersSendNotification.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderEmailForm.fxml"));
                Parent parent = null;

                try {
                    parent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.setTitle("Email");
                stage.setAlwaysOnTop(true);
                stage.show();
            }
        });


        ordersShowAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                updateOrders();
            }
        });


        ordersShowDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                updateOrders();

                List<OrderSealer> removables = new ArrayList<>();

                for (OrderSealer os : orderTable.getItems()) {

                    if(!os.getState().equals(EOrderState.DONE.getDesc())) {

                        removables.add(os);
                    }
                }

                for (OrderSealer os : removables) {

                    orderTable.getItems().remove(os);
                }
            }
        });


        taskClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                taskEmplNew.getSelectionModel().select(0);
                taskStateNew.getSelectionModel().select(0);
                taskOrderItemNew.getSelectionModel().clearSelection();
                taskDescNew.setText("");

                ts = null;
            }
        });

        taskNewAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                TaskService taskService = new TaskService();
                UserService us = new UserService();
                OrderItemService ois = new OrderItemService();

                Task t = new Task();

                if(ts != null) {

                    t = ts.getTask();

                } else {

                    try {

                        t.setId(taskService.getUniqueId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    ts = new TaskSealer(t);
                }

                for (ETaskState state : ETaskState.values()) {

                    if(state.getDesc().equals(taskStateNew.getSelectionModel().getSelectedItem())) {

                        t.setState(state);
                        break;
                    }
                }

                t.setCreated(LocalDateTime.now());
                t.setDescription(taskDescNew.getText());

                try {

                    User u = (User) us.findByUserName(taskEmplNew.getSelectionModel().getSelectedItem());
                    t.setUser(u);

                    t.setOrderItem((OrderItem) ois.get(
                            Long.valueOf(taskOrderItemNew.getSelectionModel().getSelectedItem()
                                    .split(",")[0]))
                    );

                    if(taskService.exists(t)) {

                        taskService.update(t);

                    } else {

                        taskService.persist(t);
                    }

                } catch (SQLException e) {

                    e.printStackTrace();
                }

                ts = null;
                taskTable.getSelectionModel().clearSelection();
                updateData();
            }
        });


        this.taskOrderItemNew.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        this.taskTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                TaskSealer taskSealer = taskTable.getSelectionModel().getSelectedItem();

                if(taskSealer != null) {

                    ts = taskSealer;

                    if(!ts.getUsername().isEmpty()) {

                        for(int i = 0; i < taskEmplNew.getItems().size(); i++) {

                            if(taskEmplNew.getItems().get(i).equals(ts.getUsername())) {

                                taskEmplNew.getSelectionModel().select(i);
                                break;
                            }
                        }

                    } else {

                        taskEmplNew.getSelectionModel().select(0);
                    }

                    for(int i = 0; i < taskStateNew.getItems().size(); i++) {

                        if(taskStateNew.getItems().get(i).equals(ts.getState())) {

                            taskStateNew.getSelectionModel().select(i);
                            break;
                        }
                    }

                    taskDescNew.setText(ts.getDesc());

                    for (int i = 0; i < taskOrderItemNew.getItems().size(); i++) {

                        String result = ts.getTask().getOrderItem().getId() + ", "
                                + ts.getTask().getOrderItem().getOrder().getContact();

                        if(taskOrderItemNew.getItems().get(i)
                                .equals(result)) {

                            taskOrderItemNew.getSelectionModel().select(i);
                            taskOrderItemNew.scrollTo(i);
                            taskOrderItemNew.getFocusModel().focus(i);

                            break;
                        }
                    }
                }
            }
        });


        clearSelectedProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                productsTable.getSelectionModel().clearSelection();

                prodBarcodeTF.setText("");
                prodNameTF.setText("");
                prodShortDescTF.setText("");
                prodLongDescTF.setText("");
                prodPriceTF.setText("");
                prodQuantityTF.setText("");
                prodLocationTF.setText("");
                prodQuantityTF.setText("");

                ps = null;

                StampsPicker.setProductSealer(null);
            }
        });

        updateBtn.setDisable(true);

        removeProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ProductSealer ps = productsTable.getSelectionModel().getSelectedItem();

                if(ps != null) {

                    Product p = ps.getProduct();

                    if(p != null) {

                        try {
                            new ProductService().delete(p);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    updateData();
                }
            }
        });

        prodUnitCB.getItems().clear();

        stamps.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ProductSealer ps = productsTable.getSelectionModel().getSelectedItem();

                if(ps == null) {

                    Product p = new Product();
                    p.setBarcode(prodBarcode.getText());
                    p.setProductName(prodNameTF.getText());

                    try {

                        p.setPrice(new BigDecimal(prodPriceTF.getText()));
                        p.setQuantity(Integer.parseInt(prodQuantityTF.getText()));

                    } catch (Exception e) {

                        p.setPrice(null);
                        p.setQuantity(null);
                        e.printStackTrace();
                    }

                    p.setLocation(prodLocationTF.getText());
                    p.setShortDesc(prodShortDescTF.getText());
                    p.setLongDesc(prodLongDescTF.getText());
                    p.setUnit(EUnit.getUnit(prodUnitCB.getSelectionModel().getSelectedItem()));
                    ps = new ProductSealer(p);

                    StampsPicker.setProductSealer(ps);
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/productStamps.fxml"));
                Parent parent = null;

                try {
                    parent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.setTitle("Kategorie");
                stage.setAlwaysOnTop(true);
                stage.show();
            }
        });

        for (EUnit unit : EUnit.values()) {

            prodUnitCB.getItems().add(unit.getDesc());
        }

        addProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(!(prodBarcodeTF.getText().equals("") ||
                        prodNameTF.getText().equals("") ||
                        prodShortDescTF.getText().equals("") ||
                        prodLongDescTF.getText().equals("") ||
                        prodPriceTF.getText().equals("") ||
                        prodQuantityTF.getText().equals("") ||
                        prodLocationTF.getText().equals(""))) {

                    BigDecimal price = null;
                    Integer quantity = null;

                    try {

                        price = new BigDecimal(prodPriceTF.getText());
                        quantity = Integer.parseInt(prodQuantityTF.getText());

                        ProductService productService = new ProductService();
                        Product product = new Product();

                        if(ps != null) {

                            product.setId(ps.getId());

                        } else {

                            product.setId(productService.getUniqueID());
                        }

                        product.setBarcode(prodBarcodeTF.getText());
                        product.setProductName(prodNameTF.getText());
                        product.setShortDesc(prodShortDescTF.getText());
                        product.setLongDesc(prodLongDescTF.getText());
                        product.setPrice(price);
                        product.setQuantity(quantity);
                        product.setUnit(EUnit.getUnit(prodUnitCB.getSelectionModel().getSelectedItem()));
                        product.setLocation(prodLocationTF.getText());

                        if(StampsPicker.getProductSealer() != null && StampsPicker.getProductSealer().getProduct() != null) {

                            product.setStamps(StampsPicker.getProductSealer().getProduct().getStamps());
                        }

                        productService.update(product);

                    } catch (NumberFormatException nfe) {


                    } catch (SQLException e) {

                        e.printStackTrace();

                    } finally {

                        updateData();
                    }
                }

                productsTable.getSelectionModel().clearSelection();
                ps = null;
            }
        });


        password.textProperty().addListener((observable, oldValue, newValue) -> {

            if(userSealer != null) {

                passwordChange = true;

            } else {

                passwordChange = false;
            }
        });

        userClearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                employeesTable.getSelectionModel().clearSelection();
                firstname.setText("");
                lastname.setText("");
                username.setText("");
                password.setText("");
                roles.getSelectionModel().clearSelection();

                addEmployeeBtn.setText("Přidat zaměstnance");

                userSealer = null;

                passwordChange = false;
            }
        });


        roles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roles.getItems().clear();

        for (ERole r : ERole.values()) {

            roles.getItems().add(r.getRoleName());
        }

        roles.setCellFactory(rolesLV -> {

            ListCell<String> roleCell = new ListCell<String>() {

                @Override
                public void updateItem(String item, boolean empty) {

                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            roleCell.addEventFilter(
                    MouseEvent.MOUSE_PRESSED,
                    e -> {

                if (roleCell.isEmpty()) {

                    return ;
                }

                int indexOfItem = roleCell.getIndex();

                if (roles.getSelectionModel().getSelectedIndices().contains(indexOfItem)) {

                    roles.getSelectionModel().clearSelection(indexOfItem);

                } else {

                    roles.getSelectionModel().select(indexOfItem);
                }

                roles.requestFocus();

                e.consume();
            });

            return roleCell ;
        });





        threadInitialization();
        tablesInit();

        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                isInProgress = true;

                updateData();

                isInProgress = false;
            }
        });

        addEmployeeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                boolean done = false;

                if(username.getText().equals("") ||
                        password.getText().equals("") ||
                        firstname.getText().equals("") ||
                        lastname.getText().equals("")) {

                    done = true;

                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Chyba při přidávání");
                    a.setHeaderText("Vyplňte prosím všechna pole!");
                    a.setHeaderText(null);

                    a.showAndWait();
                }

                while (!done) {
                    UserService us = new UserService();

                    if (userSealer == null) {

                        try {
                            String pass = password.getText();
                            String salt = new RandomStringGenerator(Charsets.LETTERS, Charsets.NUMBERS).generateRandomString(15, 30);

                            Hasher h = new Hasher(EHashAlgorithm.SHA512);
                            String hashedPass = h.generateHashWithSalt(pass, salt);

                            User user = new User();
                            user.setId(us.getUniqueID());
                            user.setFirstName(firstname.getText());
                            user.setLastName(lastname.getText());
                            user.setUserName(username.getText());
                            user.setPassword(password.getText());
                            user.setDateOfCreation(LocalDate.now());
                            user.setPasswordSalt(salt);
                            user.setPassword(hashedPass);

                            if (roles.getSelectionModel().getSelectedItems().size() > 0) {

                                for (String role : roles.getSelectionModel().getSelectedItems()) {

                                    user.addRole(ERole.getByName(role));
                                }
                            }

                            us.persist(user);

                            done = true;
                            userClearBtn.fire();

                        } catch (SQLException e) {

                            done = true;

                        } catch (NoSuchAlgorithmException e) {

                            done = true;
                        }

                    } else {

                        try {

                            User user = userSealer.getUser();

                            if (passwordChange) {

                                String salt = new RandomStringGenerator(Charsets.LETTERS, Charsets.NUMBERS).generateRandomString(15, 30);

                                Hasher h = new Hasher(EHashAlgorithm.SHA512);

                                String pass = h.generateHashWithSalt(password.getText(), salt);

                                user.setPassword(pass);
                                user.setPasswordSalt(salt);
                            }

                            user.setUserName(username.getText());
                            user.setFirstName(firstname.getText());
                            user.setLastName(lastname.getText());

                            if (roles.getSelectionModel().getSelectedItems().size() > 0) {

                                for (String role : roles.getSelectionModel().getSelectedItems()) {

                                    user.addRole(ERole.getByName(role));
                                }
                            }

                            us.update(user);

                            done = true;
                            userClearBtn.fire();
                        } catch (SQLException e) {

                            done = true;

                        } catch (NoSuchAlgorithmException e) {

                            done = true;
                        }
                    }
                }
            }
        });


        dbLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                isInProgress = true;

                String address = dbAdd.getText();
                String userName = dbUser.getText();
                String password = dbPass.getText();

                DBConnection dbc = new DBConnection(address, userName, password);

                Connection conn = dbc.getConnection();
                String query = "SELECT TEST_CONN_SEQ.NEXTVAL";

                try {
                    PreparedStatement ps = conn.prepareStatement(query);
                    ResultSet resultSet = ps.executeQuery();

                    if(resultSet.next()) {

                        dbReady = true;

                        DatabaseConnectionContainer.getInstance().add(
                                EDBUse.TASK_MANAGEMENT, dbc);

                        DatabaseConnectionContainer.getInstance().add(
                                EDBUse.EMPLOYEE_MANAGEMENT, dbc);

                        DatabaseConnectionContainer.getInstance().add(
                                EDBUse.ORDERS_MANAGEMENT,dbc);

                        DatabaseConnectionContainer.getInstance().add(
                                EDBUse.STORAGE_MANAGEMENT, dbc);

                        storageTab.setDisable(false);
                        taskTab.setDisable(false);
                        empTab.setDisable(false);
                        ordersTab.setDisable(false);
                        updateBtn.setDisable(false);
                    }

                } catch (SQLException e) {

                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Chyba spojení s databází!");
                    a.setHeaderText("Chybé přihlašovací údaje!");
                    a.setContentText("Zkuste to prosím znovu.");

                    a.showAndWait();

                } finally {

                    isInProgress = false;
                }
            }
        });

        isInProgress = false;
    }


    public void updateData() {

        isInProgress = true;

        this.employeesTable.getItems().clear();

        this.taskStateNew.getItems().clear();
        this.taskEmplNew.getItems().clear();
        this.taskOrderItemNew.getItems().clear();
        this.orderTable.getItems().clear();

        ps = null;
        ts = null;




        try {

            for (IEntity e : new OrderItemService().getAll()) {

                //TODO

                OrderItem oi = (OrderItem) e;

                this.taskOrderItemNew.getItems().add(oi.getId() + ", " + oi.getOrder().getContact());
            }

            this.taskEmplNew.getItems().add("*Nepřiděleno*");

            for (IEntity e : new UserService().getAll()) {

                this.taskEmplNew.getItems().add(((User) e).getUserName());
            }

        } catch (SQLException e) {

            LOG.log(Level.SEVERE, "Problem while connecting to DB: " + e.getMessage());
        }

        for (ETaskState s : ETaskState.values()) {

            this.taskStateNew.getItems().add(s.getDesc());
        }

        UserService us = new UserService();

        try {

            ObservableList<UserSealer> users = FXCollections.observableArrayList();

            for (IEntity e : us.getAll()) {

                users.add(new UserSealer((User) e));
            }

            employeesTable.getItems().addAll(users);

        } catch (SQLException e) {

            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Chyba při komunikaci s DB");
            a.setHeaderText("Chybé údaje či spojení přerušeno.");
            a.setContentText("Zkuste to opět později.");

            a.showAndWait();
        }

        updateProducts();
        updateTasks();
        updateOrders();

        isInProgress = false;
    }

    public void updateProducts() {

        this.productsTable.getItems().clear();

        try {

            ObservableList<ProductSealer> productSealers = FXCollections.observableArrayList();

            for (IEntity e : new ProductService().getAll()) {

                productSealers.add(new ProductSealer((Product) e));
            }

            productsTable.getItems().clear();
            productsTable.getItems().addAll(productSealers);

        } catch (SQLException e) {

            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Chyba při komunikaci s DB");
            a.setHeaderText("Chybé údaje či spojení přerušeno.");
            a.setContentText("Zkuste to opět později.");

            a.showAndWait();
        }
    }


    public void updateTasks() {

        this.taskTable.getItems().clear();

        try {

            ObservableList<TaskSealer> taskSealers = FXCollections.observableArrayList();

            for (IEntity e : new TaskService().getAll()) {

                taskSealers.add(new TaskSealer((Task) e));
            }

            taskTable.getItems().clear();
            taskTable.getItems().addAll(taskSealers);

        } catch (SQLException e) {

            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Chyba při komunikaci s DB");
            a.setHeaderText("Chybé údaje či spojení přerušeno.");
            a.setContentText("Zkuste to opět později.");

            a.showAndWait();
        }
    }

    public void updateOrders() {

        this.orderTable.getItems().clear();

        try {

            ObservableList<OrderSealer> orders = FXCollections.observableArrayList();

            for (IEntity e : new OrderService().getAll()) {

                orders.add(new OrderSealer((Order) e));
            }

            this.orderTable.getItems().addAll(orders);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


    public void updateOrderItems(OrderSealer os) {

        if(os != null) {

            orderItemTable.getItems().clear();

            Order o = os.getOrder();

            TreeItem<String> root = new TreeItem<>(o.getId() + " " + o.getContact());

            TreeItem<String> id = new TreeItem<>("ID: " + o.getId().toString());
            TreeItem<String> submitter = new TreeItem<>("Zákazník: " + o.getSubmitter());
            TreeItem<String> contact = new TreeItem<>("Kontakt: " + o.getContact());
            TreeItem<String> note = new TreeItem<>("Poznámka: " + o.getNote());
            TreeItem<String> price = new TreeItem<>("Celková cena: " + o.getPrice());
            TreeItem<String> orderItems = new TreeItem<>("Položky");

            for (OrderItem oi : o.getOrderItems()) {

                TreeItem<String> orderItem = new TreeItem<>(oi.getId() + " " + oi.getProduct().getProductName());

                TreeItem<String> oiId = new TreeItem<>("ID: " + oi.getId());
                TreeItem<String> oiProd = new TreeItem<>("Název produktu: " + oi.getProduct().getProductName());
                TreeItem<String> oiQ = new TreeItem<>("Množství: " + oi.getQuantity());
                TreeItem<String> tasks = new TreeItem<>("Úkoly");

                orderItem.getChildren().addAll(oiId, oiProd, oiQ, tasks);
                orderItems.getChildren().add(orderItem);

                for (Task t : oi.getTasks()) {

                    TreeItem<String> task = new TreeItem<>(t.getId().toString());

                    TreeItem<String> tId = new TreeItem<>("ID: " + t.getId());
                    TreeItem<String> state = new TreeItem<>("Stav: " + t.getState());
                    TreeItem<String> desc = new TreeItem<>("Popis: " + t.getDescription());

                    task.getChildren().addAll(tId, state, desc);
                    tasks.getChildren().add(task);
                }
            }

            root.getChildren().addAll(id, submitter, contact, note, price, orderItems);

            orderItemTree.setRoot(root);

            ObservableList<OrderItemSealer> ois = FXCollections.observableArrayList();

            try {

                for (OrderItem oi : new OrderItemService().getByOrdersId(os.getId())) {

                    if(oi != null) {

                        ois.add(new OrderItemSealer(oi));
                    }



                }

            } catch (SQLException e) {

                LOG.log(Level.SEVERE, "Problem while connecting to DB! " + e.getMessage());
            }

            orderItemTable.getItems().addAll(ois);
        }
    }



    public void tablesInit() {

        this.employeesTable.getColumns().clear();

        userID = new TableColumn<>("ID");
        userID.setMinWidth(20);
        userID.setCellValueFactory(new PropertyValueFactory<>("id"));
        userFirstname = new TableColumn<>("Křestní");
        userFirstname.setMinWidth(40);
        userFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        userLastname = new TableColumn<>("Příjmení");
        userLastname.setMinWidth(50);
        userLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        userUsername = new TableColumn<>("Přezdívka");
        userUsername.setMinWidth(50);
        userUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        dateOfCreation = new TableColumn<>("Vytvořeno");
        dateOfCreation.setMinWidth(50);
        dateOfCreation.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));
        this.employeesTable.getColumns().addAll(userID, userFirstname, userLastname, userUsername, dateOfCreation);

        this.employeesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                UserSealer us = employeesTable.getSelectionModel().getSelectedItem();

                if(us != null) {

                    userSealer = us;

                    firstname.setText(us.getFirstname());
                    lastname.setText(us.getLastname());
                    username.setText(us.getUsername());
                    password.setText(us.getUser().getPassword());

                    roles.getSelectionModel().clearSelection();

                    for (ERole r : us.getUser().getRoles()) {

                        for (String pr : roles.getItems()) {

                            if(pr.equals(r.getRoleName())) {

                                int index = roles.getItems().indexOf(pr);
                                roles.getSelectionModel().select(index);
                            }
                        }
                    }

                    roles.requestFocus();

                    passwordChange = false;

                    addEmployeeBtn.setText("Upravit");

                } else {

                    addEmployeeBtn.setText("Vytvořit");
                }
            }
        });

        this.productsTable.getColumns().clear();
        prodID = new TableColumn<>("ID");
        prodID.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodName = new TableColumn<>("Název");
        prodName.setMinWidth(50);
        prodName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        prodBarcode = new TableColumn<>("QR");
        prodBarcode.setMinWidth(50);
        prodBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        prodShortDesc = new TableColumn<>("Krátký popis");
        prodShortDesc.setMinWidth(100);
        prodShortDesc.setCellValueFactory(new PropertyValueFactory<>("shortDesc"));
        prodLongDesc = new TableColumn<>("Dlouhý popis");
        prodLongDesc.setCellValueFactory(new PropertyValueFactory<>("longDesc"));
        prodUnit = new TableColumn<>("Jednotka");
        prodUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        prodLocation = new TableColumn<>("Umístění");
        prodLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        prodQuant = new TableColumn<>("Množství");
        prodQuant.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        prodPrice = new TableColumn<>("Cena");
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.getColumns().addAll(prodID, prodName, prodPrice, prodBarcode, prodQuant, prodLocation, prodUnit, prodShortDesc, prodLongDesc);

        productsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                ps = productsTable.getSelectionModel().getSelectedItem();

                if(ps != null) {

                    StampsPicker.setProductSealer(ps);

                    prodBarcodeTF.setText(ps.getBarcode());
                    prodLocationTF.setText(ps.getLocation());
                    prodLongDescTF.setText(ps.getLongDesc());
                    prodPriceTF.setText(ps.getPrice().toString());
                    prodNameTF.setText(ps.getProductName());
                    prodQuantityTF.setText(Integer.toString(ps.getQuantity()));
                    prodShortDescTF.setText(ps.getShortDesc());

                    int index = 0;

                    for(int i = 0; i < prodUnitCB.getItems().size(); i++) {

                        if(ps.getUnit().equals(prodUnitCB.getItems().get(i))) {

                            index = i;
                            break;
                        }
                    }

                    prodUnitCB.getSelectionModel().select(index);
                }
            }
        });

        this.taskTable.getColumns().clear();

        taskID = new TableColumn<>("ID");
        taskID.setMinWidth(50);
        taskID.setCellValueFactory(new PropertyValueFactory<>("id"));

        taskUsername = new TableColumn<>("Skladník");
        taskUsername.setMinWidth(80);
        taskUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        taskState = new TableColumn<>("Stav");
        taskState.setMinWidth(80);
        taskState.setCellValueFactory(new PropertyValueFactory<>("state"));

        taskDesc = new TableColumn<>("Popis");
        taskDesc.setMinWidth(150);
        taskDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));

        taskTable.getColumns().addAll(taskID, taskUsername, taskState, taskDesc);


        this.orderTable.getColumns().clear();

        this.orderIDCol = new TableColumn<>("ID");
        this.orderIDCol.setMinWidth(20);
        this.orderIDCol.setMaxWidth(50);
        this.orderIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        this.orderContactCol = new TableColumn<>("Kontakt");
        this.orderContactCol.setMinWidth(80);
        this.orderContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        this.orderNoteCol = new TableColumn<>("Poznámka");
        this.orderNoteCol.setMinWidth(100);
        this.orderNoteCol.setCellValueFactory(new PropertyValueFactory<>("note"));

        this.orderSubmitterCol = new TableColumn<>("Zákazník");
        this.orderSubmitterCol.setMinWidth(100);
        this.orderSubmitterCol.setCellValueFactory(new PropertyValueFactory<>("submitter"));

        this.orderStateCol = new TableColumn<>("Stádium");
        this.orderStateCol.setMinWidth(100);
        this.orderStateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        this.orderTable.getColumns().addAll(orderIDCol, orderStateCol, orderContactCol, orderSubmitterCol, orderNoteCol);


        this.orderItemTable.getItems().clear();
        this.orderItemTable.getColumns().clear();

        this.orderItemID = new TableColumn<>("ID");
        this.orderItemID.setMinWidth(20);
        this.orderItemID.setCellValueFactory(new PropertyValueFactory<>("id"));

        this.orderItemProductName = new TableColumn<>("Název produktu");
        this.orderItemProductName.setMinWidth(50);
        this.orderItemProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        this.orderItemPrice = new TableColumn<>("Cena");
        this.orderItemPrice.setMinWidth(50);
        this.orderItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.orderItemQuantity = new TableColumn<>("Množství");
        this.orderItemQuantity.setMinWidth(30);
        this.orderItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        this.orderItemUnitPrice = new TableColumn<>("Jednotková cena");
        this.orderItemUnitPrice.setMinWidth(40);
        this.orderItemUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        this.orderItemTable.getColumns().addAll(orderItemID,
                orderItemProductName, orderItemQuantity, orderItemPrice, orderItemUnitPrice);

    }


    public void threadInitialization() {

        progressTask = new javafx.concurrent.Task() {
            @Override
            protected Void call() throws Exception {

                while(running) {

                    if (isInProgress) {

                        updateProgress(-1F, 1F);

                    } else {

                        updateProgress(1F, 1F);
                    }

                    Thread.sleep(100);
                }
                return null;
            }
        };
        progress.progressProperty().bind(progressTask.progressProperty());


        Thread t = new Thread(progressTask);
        t.setDaemon(true);
        t.start();
    }

}
