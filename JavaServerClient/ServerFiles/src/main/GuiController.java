package main;

import java.io.IOException;
import java.net.ServerSocket;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import server.Client;
import server.Database;
import server.Display;
import server.RoomManager;

public class GuiController extends Application {

    @FXML
    public VBox roomList;
    @FXML
    public VBox privateRoomList;
    @FXML
    public VBox activeUserList;
    @FXML
    public VBox registeredUserList;
    @FXML
    public VBox logBox;
    @FXML
    public ScrollPane spLog;
    @FXML
    public ScrollPane spActiveUser;
    @FXML
    public ScrollPane spRooms;
    @FXML
    public ScrollPane spPrivateRooms;
    @FXML
    public ScrollPane spRegisteredUser;
    @FXML
    public Label online;

    private RoomManager rm;
    private ServerSocket serversocket;
    private Database db;
    private Display display;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {

        // load the fxml file of the server gui
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server_gui.fxml"));

        display = new Display();
        rm = new RoomManager(display);
        db = new Database(display);
        display.registerModules(db, rm, this);
        rm.setDefault("Lobby");

        loader.setController(this);

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        primaryStage.setResizable(false);
        primaryStage.setTitle("Server");

        VBox.setVgrow(logBox, Priority.ALWAYS);
        controlSp(spLog, logBox);
        controlSp(spRegisteredUser, registeredUserList);
        controlSp(spRooms, roomList);
        controlSp(spActiveUser, activeUserList);
        controlSp(spPrivateRooms, privateRoomList);

        // show this scene
        primaryStage.show();

        // update the gui
        display.updateGui();

        // create a new server socket
        serversocket = new ServerSocket(55555);

        // create a new thread that waits for incoming clients
        Thread thread = new Thread(() -> {
            while (true) {

                // create for every client a new (client)thread
                Client client;
                try {
                    client = new Client(serversocket.accept(), rm, db);
                    // close client when server closes
                    client.setDaemon(true);
                    display.log("[Server] A new client connected.");
                    client.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // close the client thread when the close button (top right) is pressed
        thread.setDaemon(true);
        thread.start();

    }

    public String popUp() {
        // create a new popup to enter text
        Stage popupStage = new Stage();

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Enter Text");

        TextField textField = new TextField();
        textField.setFont(new Font("Calibri", 14));
        textField.setStyle("-fx-background-color: #3a3c4a; -fx-text-fill: #b8cdd7; -fx-prompt-text-fill: #b8cdd7");

        Button button = new Button("OK");
        button.setFont(new Font("Calibri", 14));
        button.setStyle("-fx-background-color: #b8cdd7");
        button.setCursor(Cursor.HAND);

        button.setOnAction(s -> {
            popupStage.close();
        });

        VBox popupLayout = new VBox(10);
        popupLayout.setStyle("-fx-background-color: #2b2d36");
        popupLayout.getChildren().addAll(textField, button);

        Scene popupScene = new Scene(popupLayout, 250, 150);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);
        popupStage.showAndWait();
        return textField.getText();
    }

    // for height of Scrollpanes
    public void controlSp(ScrollPane sp, VBox vBox) {
        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp.setVvalue((Double) newValue);
            }
        });
    }

    @FXML
    public void newRoom() {
        display.log(rm.addRoom(popUp()) ? "[Server] New room created." : "[Server] Room already exits");
        display.updateGui();
    }

    @FXML
    public void viewAll() {
        display.viewAll();
    }

}