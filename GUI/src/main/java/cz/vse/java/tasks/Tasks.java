package cz.vse.java.tasks;


import cz.vse.java.connections.utils.IConnection;
import cz.vse.java.messages.*;
import cz.vse.java.messages.utils.IMessage;
import cz.vse.java.messages.utils.future.MessageTask;
import cz.vse.java.services.clientSide.Client;
import cz.vse.java.services.serverSide.EServiceType;
import cz.vse.java.util.observerDP.IObserver;
import cz.vse.java.util.persistance.entities.tasks.ETaskState;
import cz.vse.java.util.userData.UserProperties;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;


/*********************************************************************
 * <p>The class of {@code Tasks} is used to abstractly define
 * the type of the instances.</p>
 *
 * <p>This window is responsible for creating a control panel for
 * managing the tasks.</p>
 *
 * <i>Written for project "GUI".</i>
 * @author Vojtěch Pavlů
 * @version 17. 04. 2020
 *
 *
 * @see cz.vse.java.tasks
 */
public class Tasks implements Initializable, IObserver {


    /* *****************************************************************/
    /* Instance variables **********************************************/

    @FXML
    private Button
            taskDone,
            taskNotDone,
            fastUpdate,
            refuseTask;

    @FXML
    private CheckBox listen;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label mainLabel;

    @FXML
    private TableView<TaskFormat> taskTable;

    @FXML
    private TableColumn<TaskFormat, Long> id;
    private TableColumn<TaskFormat, String> description;
    private TableColumn<TaskFormat, String> state;
    private TableColumn<TaskFormat, String> note;


    private final CopyOnWriteArrayList<TaskFormat> tasks = new CopyOnWriteArrayList<>();


    private Task progressTask;
    private boolean progressUpdate;
    private boolean isProgress;

    private Task taskFinished;
    private boolean updateFinished;
    private boolean listening;

    private Task updateTask;
    private boolean updateTasks;


    /* *****************************************************************/
    /* Static variables ************************************************/

    /**
     * <p>Private static instance of the {@link Logger}
     * - the logger of the {@link Tasks class</p>
     */
    private static final Logger LOG =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /* *****************************************************************/
    /* Constructors ****************************************************/



    /* *****************************************************************/
    /* Instance methods ************************************************/


    /**
     * <p>Method for initializing the whole window and it's elements.</p>
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.isProgress = true;

        this.taskTable.setDisable(true);
        this.taskDone.setDisable(true);
        this.refuseTask.setDisable(true);
        this.refuseTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                TaskFormat tf = taskTable.getSelectionModel().getSelectedItem();

                if(tf != null) {

                    Client.getInstance().addMessageTask(new MessageTask(
                            new RefuseTask(
                                    UserProperties.getInstance().getUserName(),
                                    tf.getId()
                            ),
                            EServiceType.TASK_SERVICE
                    ));

                    Client.getInstance().getTasks().remove(tf.getId());

                    fastUpdate();
                }
            }
        });

        this.taskDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                TaskFormat tf = taskTable.getSelectionModel().getSelectedItem();

                if(tf != null) {

                    Client.getInstance().addMessageTask(new MessageTask(
                             new TaskStateChange(
                                    tf.getId(),
                                    ETaskState.DONE,
                                    UserProperties.getInstance().getUserName()),
                            EServiceType.TASK_SERVICE)
                    );

                    Client.getInstance().getTasks().remove(tf.getId());
                }
            }
        });


        this.taskNotDone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                TaskFormat tf = taskTable.getSelectionModel().getSelectedItem();

                if(tf != null) {

                    Client.getInstance().addMessageTask(new MessageTask(
                            new TaskStateChange(
                                    tf.getId(),
                                    ETaskState.NOT_DONE,
                                    UserProperties.getInstance().getUserName()),
                            EServiceType.TASK_SERVICE)
                    );

                    Client.getInstance().getTasks().remove(tf.getId());
                }
            }
        });

        this.taskNotDone.setDisable(true);

        this.listen.setStyle("-fx-text-fill: green");

        this.listen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(listen.isSelected()) {

                    listening = true;

                    mainLabel.setText("Poslouchám přidělení úkolů");
                    mainLabel.setStyle("-fx-text-fill: green");
                    listen.setStyle("-fx-text-fill: green");
                    progressBar.setStyle("-fx-accent: green");

                    taskTable.setDisable(false);

                } else {

                    taskTable.setDisable(true);

                    mainLabel.setText("Aktuálně neposlouchám novým úkolům");
                    mainLabel.setStyle("-fx-text-fill: red");
                    listen.setStyle("-fx-text-fill: red");
                    listening = false;
                    progressBar.setStyle("-fx-accent: red");
                }


                Client.getInstance().setListeningToTasks(listening);

                Client.getInstance().addMessageTask(new MessageTask(
                        new ListeningForTasksContainer(
                                UserProperties.getInstance().getUserName(),
                                listening
                        ),
                        EServiceType.TASK_SERVICE
                ));

                Client.getInstance().requestAllMyTasks();

                taskTable.setDisable(false);
            }
        });

        initUpdateThreads();
        initTable();
    }


    /**
     * <p>Prepares the table for the data storage.</p>
     */
    public void initTable() {

        taskTable.setDisable(true);
        taskTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        taskTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                TaskFormat tf = taskTable.getSelectionModel().getSelectedItem();

                if(tf != null) {

                    taskDone.setDisable(false);
                    taskNotDone.setDisable(false);
                    refuseTask.setDisable(false);
                }
            }
        });


        taskTable.getColumns().clear();

        id = new TableColumn<>("ID");
        id.setMinWidth(50);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        description = new TableColumn<>("Popis");
        description.setMinWidth(200);
        description.setCellValueFactory(new PropertyValueFactory<>("taskDesc"));

        state = new TableColumn<>("Stav");
        state.setMinWidth(80);
        state.setCellValueFactory(new PropertyValueFactory<>("taskState"));

        note = new TableColumn<>("Poznámka");
        note.setMinWidth(100);
        note.setCellValueFactory(new PropertyValueFactory<>("taskNote"));

        this.taskTable.getColumns().addAll(id, description, state, note);

        isProgress = false;
    }


    /**
     * <p>Initializes the threads responsible for fluent
     * app work</p>
     */
    public void initUpdateThreads() {

        progressUpdate = true;
        updateFinished = true;
        isProgress = true;
        updateTasks = true;

        updateTask = new Task<Void>() {

            @Override
            public Void call() {

                while(updateTasks) {

                    IConnection conn =
                            Client.getInstance()
                                    .getConnectionWithService(
                                            EServiceType.TASK_SERVICE);
                    if(conn != null) {

                        if (conn.getAmIAuthenticated()) {

                            fastUpdate();

                            try {

                                fastUpdate.setDisable(false);

                                Thread.sleep(5000);

                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }

                            update();

                        } else {

                            try {

                                Thread.sleep(4000);

                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }
                        }

                    } else {

                        Client.getInstance().addMessageTask(new MessageTask(
                                new ServiceReferenceRequest(
                                        EServiceType.TASK_SERVICE),
                                EServiceType.ROUTER)
                        );

                        try {

                            Thread.sleep(5000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        };


        progressTask = new Task<Void>() {

            @Override
            public Void call() {

                while (progressUpdate) {

                    if(isProgress) {

                        updateProgress(-1F, 1F);

                    } else {

                        updateProgress(1F, 1F);
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }
        };

        this.progress.progressProperty().bind(progressTask.progressProperty());


        taskFinished = new Task() {
            @Override
            protected Void call() throws Exception {

                while(updateFinished) {

                    if (!listening) {

                        updateProgress(-1F, 1F);

                    } else {

                        List<cz.vse.java.util.persistance.entities.tasks.Task> tasks =
                                Client.getInstance().getTasks().getTasks();


                        int defaultVal = tasks.size();
                        int finished = defaultVal;

                        for (cz.vse.java.util.persistance.entities.tasks.Task t : tasks) {

                            if(t.getState().equals(ETaskState.CONFIRMED)) {

                                finished--;

                            } else if(t.getState().equals(ETaskState.ASSIGNED)) {

                                finished--;
                            }
                        }
                        float result = ( ((float) finished) / defaultVal);
                        updateProgress(result, 1F);
                    }

                    Thread.sleep(100);
                }

                return null;
            }
        };

        progressBar.progressProperty().bind(taskFinished.progressProperty());

        Thread ut = new Thread(updateTask);
        ut.setDaemon(true);
        ut.start();

        Thread pt = new Thread(progressTask);
        pt.setDaemon(true);
        pt.start();

        Thread pf = new Thread(taskFinished);
        pf.setDaemon(true);
        pf.start();
    }


    /**
     * <p>Responsible for updating the data of the table</p>
     */
    public void updateTaskTable() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                TaskFormat selected = taskTable.getSelectionModel().getSelectedItem();

                ObservableList<TaskFormat> fxTasks = FXCollections.observableArrayList();
                fxTasks.addAll(tasks);

                taskTable.getItems().clear();
                taskTable.setItems(fxTasks);

                if(selected != null && fxTasks.contains(selected)) {

                    int index = fxTasks.indexOf(selected);

                    taskTable.getSelectionModel().select(index);
                }

                isProgress = false;
            }
        });
    }


    /**
     * <p>Overriden method of {@link IObserver} interface.</p>
     */
    @Override
    public void update() {

        IConnection c = Client.getInstance().getConnectionWithService(EServiceType.TASK_SERVICE);

        if(c != null && c.getAmIAuthenticated()) {

            isProgress = true;

            if(listen.isSelected()) {

                taskTable.setDisable(false);
            }

            CopyOnWriteArrayList<cz.vse.java.util.persistance.entities.tasks.Task> tasks =
                    Client.getInstance().getTasks().getTasks();

            System.out.println("Num of tasks: " + tasks.size());

            if (tasks.size() > 0) {

                synchronized (this.tasks) {

                    this.tasks.clear();

                    for (cz.vse.java.util.persistance.entities.tasks.Task t : tasks) {

                        if(t.getState().equals(ETaskState.CONFIRMED)) {

                            this.tasks.add(new TaskFormat(t));

                        } else if(t.getState().equals(ETaskState.ASSIGNED)) {

                            this.tasks.add(new TaskFormat(t));
                        }
                    }
                }

                updateTaskTable();
            }
        } else {

            System.out.println("Not authenticated yet....");
        }
    }


    /**
     * <p>Begins fast update of the table of tasks.</p>
     */
    public void fastUpdate() {

        Client.getInstance().send(
                new GiveMeMyTasks(UserProperties.getInstance().getUserName()),
                EServiceType.TASK_SERVICE
        );

        update();
    }
}
