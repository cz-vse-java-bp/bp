package cz.vse.java.admin;


import cz.vse.java.admin.utils.EOrderState;
import cz.vse.java.admin.utils.OrderSealer;
import cz.vse.java.util.persistance.entities.IEntity;
import cz.vse.java.util.persistance.entities.OrderItem;
import cz.vse.java.util.persistance.entities.orders.Order;
import cz.vse.java.util.persistance.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.mail.*;
import javax.mail.internet.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code OrderEmailForm} is used to abstractly define
 * the type of the instances.</p>
 *
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 03. 05. 2020
 *
 *
 * @see cz.vse.java.admin
 */
public class OrderEmailForm implements Initializable {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    private Button
            x,
            emailSend;

    @FXML
    private TextField
            emailSubject,
            emailAddress;

    @FXML
    private TextArea
            emailText;

    @FXML
    private TableView<OrderSealer> orderTable;


    @FXML
    private TableColumn<OrderSealer, Long> id;
    private TableColumn<OrderSealer, String> state, contact, submitter, note;

    @FXML
    private ProgressIndicator progress;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link OrderEmailForm class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        progress.setVisible(true);

        orderTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        orderTable.getItems().clear();
        orderTable.getColumns().clear();

        id = new TableColumn<>("ID");
        id.setMinWidth(30);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        state = new TableColumn<>("Stav");
        state.setMinWidth(50);
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        note = new TableColumn<>("Poznámka");
        note.setMinWidth(100);
        note.setCellValueFactory(new PropertyValueFactory<>("note"));
        submitter = new TableColumn<>("Zákazník");
        submitter.setMinWidth(50);
        submitter.setCellValueFactory(new PropertyValueFactory<>("submitter"));
        contact = new TableColumn<>("Kontakt");
        contact.setMinWidth(50);
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        orderTable.getColumns().addAll(id, state, submitter, contact, note);

        ObservableList<OrderSealer> orderSealers = FXCollections.observableArrayList();

        try {

            for (IEntity e : new OrderService().getAll()) {

                orderSealers.add(new OrderSealer((Order) e));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        orderTable.getItems().addAll(orderSealers);

        orderTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                OrderSealer os = orderTable.getSelectionModel().getSelectedItem();

                if(os != null) {

                    emailAddress.setText(os.getContact());

                    String s = os.getState();

                    if(s.equals(EOrderState.DONE.getDesc())) {


                        StringBuilder order = new StringBuilder();

                        for (OrderItem oi : os.getOrder().getOrderItems()) {

                            order.append("\n")
                                    .append(oi.getQuantity())
                                    .append("x ")
                                    .append(oi.getProduct().getProductName());
                        }

                        emailSubject.setText("Úspěšné dokončení objednávky " + os.getId());
                        emailText.setText("Milý zákazníku " + os.getSubmitter() +
                                ",\nÚspěšně jsme dokončili objednávku č. "
                                + os.getId() + " a můžete si ji vyzvednout.\n"
                                + order + "\nCelková cena: " + os.getOrder().getPrice());

                    } else if(s.equals(EOrderState.FAILED.getDesc())) {

                        emailSubject.setText("Problém při dokončování objednávky " + os.getId());
                        emailText.setText("Milý zákazníku " + os.getSubmitter() +
                                "\nVyskytl se problém při dokončování objednávky č. " + os.getId() + ".\n"
                                + "O problému Vás budeme dále informovat."
                        );

                    } else {

                        emailSubject.setText("Průběh objednávky " + os.getId());
                    }
                }
            }
        });

        x.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                emailSubject.setText("");
                emailText.setText("");
                emailAddress.setText("");
                orderTable.getSelectionModel().clearSelection();
            }
        });


        emailSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                emailSend.setDisable(true);
                progress.setVisible(true);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String to = emailAddress.getText();
                String from = "93f4eac972b758";
                String password = "1288607f7927f2";

                Properties prop = new Properties();
                prop.put("mail.smtp.auth", true);
                prop.put("mail.smtp.starttls.enable", "true");
                prop.put("mail.smtp.host", "smtp.mailtrap.io");
                prop.put("mail.smtp.port", "25");
                prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");


                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

                Message message = new MimeMessage(session);

                try {

                    message.setFrom(new InternetAddress(from));
                    message.setRecipients(
                            Message.RecipientType.TO, InternetAddress.parse(to));
                    message.setSubject(emailSubject.getText());

                    String msg = emailText.getText();

                    MimeBodyPart mimeBodyPart = new MimeBodyPart();
                    mimeBodyPart.setContent(msg, "text/html");

                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(mimeBodyPart);

                    message.setContent(multipart);

                    Transport.send(message);

                } catch (MessagingException e) {

                    e.printStackTrace();

                } finally {

                    emailSend.setDisable(false);
                    progress.setVisible(false);
                }
            }
        });

        progress.setVisible(false);

    }

    /* *****************************************************************/
    /* Static methods **************************************************/



    /* *****************************************************************/
    /* Getters *********************************************************/



    /* *****************************************************************/
    /* Setters *********************************************************/


}
