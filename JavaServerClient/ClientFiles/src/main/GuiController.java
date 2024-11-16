package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.Receiver;
import javafx.beans.value.ChangeListener;
import helper.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GuiController extends Application {

    @FXML
    public Label status;
    @FXML
    public TextField username;
    @FXML
    public Label usernameLabel;
    @FXML
    public PasswordField password;
    @FXML
    public Button proccessLogin;
    @FXML
    public Button proccessRegistration;
    @FXML
    public Button changeToLogin;
    @FXML
    public Button sendMsgButton;
    @FXML
    public Button sendPdfButton;
    @FXML
    public Button sendImageButton;
    @FXML
    public VBox chatBox;
    @FXML
    public TextField msgBox;
    @FXML
    public VBox roomList;
    @FXML
    public VBox userList;
    @FXML
    public VBox userRoomList;
    @FXML
    public VBox privateRoomList;
    @FXML
    public ScrollPane spChat;

    public ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket server;
    private String name;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException, InterruptedException {

        // try to create a connection to a server
        // (if server is online)
        try {
            server = new Socket("localhost", 55555);
        } catch (IOException e) {
            System.out.println("The Server is offline");
            System.exit(0);
        }

        // create file streams for receive and send messages
        outToServer = new ObjectOutputStream(server.getOutputStream());
        inFromServer = new ObjectInputStream(server.getInputStream());

        // first load the login view as the start of the client program
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        loader.setController(this);

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        primaryStage.setResizable(false);
        primaryStage.setTitle("Client");
        // show this scene
        primaryStage.show();

    }

    public String getName() {
        return name;
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

    // switch the scene to another fxml file, e.g. to register
    public void switchSceneTo(String fxml_file, ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_file));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loader.setController(this);

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // for Button
    @FXML
    private void switchToRegistration(ActionEvent event) throws IOException {
        switchSceneTo("/fxml/register.fxml", event);
    }

    // for Button
    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        switchSceneTo("/fxml/login.fxml", event);
    }

    // for Button
    @FXML
    private void sendMsg(ActionEvent event) throws IOException {

        // send the string from the Textfield to the server
        outToServer.writeObject(new Message("msg", msgBox.getText()));

        // clear the Textfield"
        msgBox.clear();

    }

    // for height of Scrollpanes and to move view down, when new label is added
    @FXML
    private void controlSp(ScrollPane sp, VBox vBox) {
        Platform.runLater(() -> {
            vBox.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    sp.setVvalue((Double) newValue);
                }
            });
        });
    }

    // before we can recive and send messages via the other thread above
    // we first have to login (or register) on the server
    @FXML
    private void handleLogin(ActionEvent event) throws ClassNotFoundException, IOException {

        // disable the login button to stop spam
        proccessLogin.setVisible(false);

        // send the login data to the server
        outToServer.writeObject(new Message("l", username.getText(), password.getText()));

        // wait for the server response
        Message msg = (Message) inFromServer.readObject();

        // if the login was successfully
        if (msg.is("s")) {
            Platform.runLater(() -> controlSp(spChat, chatBox));
            // if login succesfull, go to messeger
            switchSceneTo("/fxml/messenger.fxml", event);

            // run the receiver logic (MUST come after switch to messenger layout !!! )
            Receiver receiver = new Receiver(this, inFromServer);
            name = username.getText();
            usernameLabel.setText(name);
            receiver.setDaemon(true);
            receiver.start();

        } else if (msg.is("e")) {

            // if login is unsuccessfull, go back to login
            switchSceneTo("/fxml/login.fxml", event);

            // display the error message (after we switched the scene, because the
            // controller is the same)
            status.setText(msg.getString1());

        }
    }

    @FXML
    private void handleRegisteration(ActionEvent event) throws ClassNotFoundException, IOException {

        // disable the registration button to stop spam
        proccessRegistration.setVisible(false);

        // check whether password or username is empty
        if (username.getText().length() > 0 && password.getText().length() > 0) {
            // send the login data to the server
            outToServer.writeObject(
                    new Message("r", username.getText(), password.getText()));

            // wait for server response
            Message msg = (Message) inFromServer.readObject();

            if (msg.is("s")) {

                // if registered, go to login page
                switchSceneTo("/fxml/login.fxml", event);

            } else if (msg.is("e")) {

                // if register is unsuccessfull, go back to login
                switchSceneTo("/fxml/register.fxml", event);

                // display the error message (after we switched the scene, because the
                // controller is the same)
                status.setText(msg.getString1());

            }
        } else {
            proccessRegistration.setVisible(true);
        }
    }

    @FXML
    public void sendImage(ActionEvent event) throws IOException {
        // choose the pdf file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg"));
        // in showOpenDialog we have to slect the stage we are currently in
        File img = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

        if (img != null) {
            // read the file into a byte array
            FileInputStream fis = new FileInputStream(img);
            byte[] fileData = new byte[(int) img.length()];
            fis.read(fileData);
            fis.close();

            // create a new popup to enter text
            Stage popupStage = new Stage();

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Image");

            TextField textField = addTextField("");
            textField.setPromptText("Enter Name of the Image");
            Button okButton = addButton("OK");

            okButton.setOnAction(s -> {

                try {
                    outToServer.writeObject(new Message("img", textField.getText(), fileData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                popupStage.close();
            });

            VBox popupLayout = new VBox(10);
            popupLayout.setStyle("-fx-background-color: #2b2d36");
            popupLayout.getChildren().addAll(textField, okButton);

            Scene popupScene = new Scene(popupLayout, 250, 150);
            popupStage.setScene(popupScene);
            popupStage.setResizable(false);
            popupStage.showAndWait();
        }
    }

    @FXML
    public void sendPDF(ActionEvent event) throws IOException {

        // choose the pdf file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        // in showOpenDialog we have to slect the stage we are currently in
        File pdf = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

        if (pdf != null) {
            // read the file into a byte array
            FileInputStream fis = new FileInputStream(pdf);
            byte[] fileData = new byte[(int) pdf.length()];
            fis.read(fileData);
            fis.close();

            // create a new popup to enter text
            Stage popupStage = new Stage();

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("PDF");

            TextField textField = addTextField("");
            textField.setPromptText("Enter Name of PDF");
            Button okButton = addButton("OK");

            okButton.setOnAction(s -> {

                try {
                    outToServer.writeObject(new Message("pdf", textField.getText(), fileData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                popupStage.close();
            });

            VBox popupLayout = new VBox(10);
            popupLayout.setStyle("-fx-background-color: #2b2d36");
            popupLayout.getChildren().addAll(textField, okButton);

            Scene popupScene = new Scene(popupLayout, 250, 150);
            popupStage.setScene(popupScene);
            popupStage.setResizable(false);
            popupStage.showAndWait();
        }

    }

    @FXML
    private void addLabel(String s, VBox box) {
        // show text in multiple lines, if text wider than sp
        Label label = new Label(s);
        label.setWrapText(true);
        label.setFont(new Font("Calibri", 13));

        // add to the box
        Platform.runLater(() -> box.getChildren().add(label));
    }

    @FXML
    private Button addButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Calibri", 14));
        button.setStyle("-fx-background-color: #b8cdd7");
        button.setCursor(Cursor.HAND);
        return button;
    }

    @FXML
    private TextField addTextField(String text) {
        TextField textField = new TextField();
        textField.setFont(new Font("Calibri", 14));
        textField.setStyle("-fx-background-color: #3a3c4a; -fx-text-fill: #b8cdd7; -fx-prompt-text-fill: #b8cdd7");
        return textField;
    }

}
