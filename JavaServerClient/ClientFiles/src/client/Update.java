package client;

import java.io.IOException;
import java.util.Map;

import helper.Message;
import helper.Room;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.GuiController;

public class Update {

    private GuiController ctr;

    public Update(GuiController ctr) {
        this.ctr = ctr;
    }

    public void status(String status) {
        Platform.runLater(() -> {
            ctr.status.setText(status);
        });
    }

    public void updateGui(Message msg) {
        Platform.runLater(() -> {
            ctr.roomList.getChildren().clear();
            ctr.privateRoomList.getChildren().clear();
            ctr.userList.getChildren().clear();
            ctr.userRoomList.getChildren().clear();

            String currentRoom = new String();
            for (Map.Entry<String, Room> entry : msg.getRoomList().entrySet()) {
                if (entry.getValue().contains(ctr.getName()))
                    currentRoom = entry.getKey();
            }

            Label label = new Label("  Room:  " + currentRoom + "  ");
            label.setFont(new Font("Calibri", 14));
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #262732; -fx-background-color: #b8cdd7; -fx-background-radius: 20px");
            ctr.userRoomList.getChildren().add(label);

            for (Map.Entry<String, Room> entry : msg.getRoomList().entrySet()) {

                displayRoom(entry);
                displayActiveUser(entry);
                // show user in current room
                if(currentRoom != null && entry.getKey().equals(currentRoom)){
                    displayUserRoom(entry);
                }

            }

        });

    }

    public void displayRoom(Map.Entry<String, Room> entry) { 
        Room room = entry.getValue();

        // check if allowed to view thhe room (in case it's a private connection)
        if(!(room.hasLimit() && !(room.getUser1().equals(ctr.getName()) || room.getUser2().equals(ctr.getName())))){

        Label label = new Label(entry.getKey() + " (" + entry.getValue().size() + " User)");
        label.setWrapText(true);
        label.setFont(new Font("Calibri", 13));

        // Create a context menu
        ContextMenu contextMenu = new ContextMenu();

        if (entry.getValue().hasLimit()) {

            MenuItem changeRoom = new MenuItem("Change to this Room");
            MenuItem renameRoom = new MenuItem("Rename this Room");
            MenuItem deleteRoom = new MenuItem("Delete this Room");

            changeRoom.setOnAction(e -> {
                // send a request to the server
                try {
                    ctr.outToServer.writeObject(new Message("changeRoom", entry.getKey()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                ctr.chatBox.getChildren().clear();

            });

            renameRoom.setOnAction(e -> {
                // send a request to the server
                try {
                    ctr.outToServer.writeObject(new Message("renamePrivateRoom", entry.getKey(), ctr.popUp()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            });

            deleteRoom.setOnAction(e -> {
                // send a request to the server
                try {
                    ctr.outToServer.writeObject(new Message("deletePrivateRoom", entry.getKey()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            });

            // add menu items to the context menu
            contextMenu.getItems().addAll(changeRoom, renameRoom, deleteRoom);

            // add the context menu on the label
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(label, event.getScreenX(), event.getScreenY());
                }
            });

            ctr.privateRoomList.getChildren().add(label);

        } else {

            // creat a new menu item
            MenuItem changeRoom = new MenuItem("Change to this Room");

            changeRoom.setOnAction(e -> {
                // send a request to the server
                try {
                    ctr.outToServer.writeObject(new Message("changeRoom", entry.getKey()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                ctr.chatBox.getChildren().clear();

            });

            // add menu items to the context menu
            contextMenu.getItems().addAll(changeRoom);

            // add the context menu on the label
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(label, event.getScreenX(), event.getScreenY());
                }
            });

            // add the label to the roomlist (VBox)
            ctr.roomList.getChildren().add(label);
        }
    }

    }

    public void displayActiveUser(Map.Entry<String, Room> entry) {
        addUserLabel(entry, ctr.userList, false);
    }

    public void displayUserRoom(Map.Entry<String, Room> entry){
        addUserLabel(entry, ctr.userRoomList, true);
    }

    @FXML
    public void addUserLabel(Map.Entry<String, Room> entry, VBox vBox, boolean thisRoom) {
        for (String user : entry.getValue()) {
            Label label;
            // show text in multiple lines, if text wider than sp
            if (!thisRoom) {
                if (entry.getValue().hasLimit()) {
                    label = new Label(user + " in a private room ");
                } else {
                    label = new Label(user + " in room " + entry.getKey());
                }
            }
            else{
                label = new Label(user);
            }

            label.setWrapText(true);
            label.setFont(new Font("Calibri", 13));

            // Create a context menu
            ContextMenu contextMenu = new ContextMenu();

            // creat a new menu item
            MenuItem privateRoom = new MenuItem("Create a private Chat.");

            privateRoom.setOnAction(e -> {
                // send a request to the server
                try {
                    ctr.outToServer.writeObject(new Message("createPrivateRoom", user));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ctr.chatBox.getChildren().clear();

            });

            // add menu items to the context menu
            contextMenu.getItems().addAll(privateRoom);

            // add the context menu on the label
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(label, event.getScreenX(), event.getScreenY());
                }
            });

            vBox.getChildren().add(label);
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
