package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import helper.Message;
import helper.Profile;
import helper.Room;
import helper.Time;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.GuiController;

public class Display {

    private Database db;
    private RoomManager rm;
    private GuiController gc;
    private ArrayList<String> log;

    @SuppressWarnings("unchecked")
    public Display() {

        // try to load the log file
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Log.db"));
            log = (ArrayList<String>) ois.readObject();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            log = new ArrayList<>();
        }

    }

    public void registerModules(Database db, RoomManager rm, GuiController gc) {
        this.db = db;
        this.rm = rm;
        this.gc = gc;
    }

    public boolean updateGui() {
        Platform.runLater(() -> {
            gc.roomList.getChildren().clear();
            gc.privateRoomList.getChildren().clear();
            gc.activeUserList.getChildren().clear();

            Hashtable<String, Room> temp = new Hashtable<>();

            for (Map.Entry<String, Room> entry : rm.getData().entrySet()) {

                temp.put(entry.getKey(), new Room(entry.getValue()));

                displayRoom(entry);
                displayActiveUser(entry);

            }

            displayRegisteredUsers();

            gc.online.setText(String.valueOf(rm.areOnline()));

            rm.sendToAll(new Message("ur", temp));

        });

        db.saveUserdata();

        return true;

    }

    public void displayRoom(Map.Entry<String, Room> entry) {

        Label label = new Label(entry.getKey() + " (" + entry.getValue().size() + " User)");
        label.setWrapText(true);
        label.setFont(new Font("Calibri", 13));
        // create a context menu
        ContextMenu contextMenu = new ContextMenu();

        // creat menu elements
        MenuItem changeName = new MenuItem("Change Name");
        MenuItem deleteRoom = new MenuItem("Delete Room");

        // set the click action for the menuitem
        changeName.setOnAction(e -> {
            rm.changeRoomname(entry.getKey(), gc.popUp());
            updateGui();
        });

        // delete the clicked room
        deleteRoom.setOnAction(e -> {
            rm.removeRoom(entry.getKey());
            updateGui();
        });

        // add items to menu
        contextMenu.getItems().addAll(changeName, deleteRoom);

        // add the context menu to the label (room)
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(label, event.getScreenX(), event.getScreenY());
            }
        });

        // add the label to the public or private room list
        if (entry.getValue() != null && entry.getValue().hasLimit()) {
            gc.privateRoomList.getChildren().add(label);

        } else {
            gc.roomList.getChildren().add(label);

        }

    }

    public void displayActiveUser(Map.Entry<String, Room> entry) {

        for (String user : entry.getValue()) {

            Label label = new Label(user + " in Room " + entry.getKey());
            label.setWrapText(true);
            label.setFont(new Font("Calibri", 13));

            // create a context menu
            ContextMenu contextMenu = new ContextMenu();

            // create menu items
            MenuItem block = new MenuItem("Block");
            MenuItem ban = new MenuItem("Ban");
            MenuItem msg = new MenuItem("Message");
            MenuItem private_room = new MenuItem("New private Room.");

            // block a user
            private_room.setOnAction(e -> {
                String user2 = gc.popUp();

                if (rm.getRoomWhereUserIsIn(user2) != null) {
                    String roomname = user + "-" + user2;
                    rm.addRoom(roomname, user, user2);
                }

            });

            // block a user
            block.setOnAction(e -> {
                db.blockUser(user);
                updateGui();
                rm.kickClient(user);
            });

            // ban a user
            ban.setOnAction(e -> {
                db.banUser(user);
                updateGui();
                rm.kickClient(user);
            });

            // send a message to a user
            msg.setOnAction(e -> {
                rm.getClientFromUsername(user).send(new Message("msg", "[Admin] " + gc.popUp()));
            });

            // add menu items to the context menu
            contextMenu.getItems().addAll(msg, block, ban, private_room);

            // add the context menu to the label
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(label, event.getScreenX(), event.getScreenY());
                }
            });

            // add the label to the userlist (VBox)
            gc.activeUserList.getChildren().add(label);
        }

    }

    public void displayRegisteredUsers() {

        // first clear the whole VBox
        gc.registeredUserList.getChildren().clear();

        for (Map.Entry<String, Profile> entry : db.getData().entrySet()) {

            Label label = new Label(entry.getValue().toString());
            label.setWrapText(true);
            label.setFont(new Font("Calibri", 13));

            // create a context menu
            ContextMenu contextMenu = new ContextMenu();

            // creat menu elements
            MenuItem changeName = new MenuItem("Change Name");
            MenuItem removeUser = new MenuItem("Remove User");
            MenuItem block = new MenuItem("Block");
            MenuItem unblock = new MenuItem("Unblock");
            MenuItem ban = new MenuItem("Ban");
            MenuItem unban = new MenuItem("Unban");

            // set the click action for the menuitem
            changeName.setOnAction(e -> {
                db.changeUsername(entry.getValue().getUsername(), gc.popUp());
                updateGui();
            });

            // delete the clicked user
            removeUser.setOnAction(e -> {
                rm.kickClient(entry.getValue().getUsername());
                db.removeUser(entry.getValue().getUsername());
                updateGui();
            });

            // block a user
            block.setOnAction(e -> {
                db.blockUser(entry.getValue().getUsername());
                rm.kickClient(entry.getValue().getUsername());
                updateGui();
            });

            // unblock a user
            unblock.setOnAction(e -> {
                db.unblockUser(entry.getValue().getUsername());
                updateGui();
            });

            // ban a user
            ban.setOnAction(e -> {
                db.banUser(entry.getValue().getUsername());
                updateGui();
            });

            // ban a user
            unban.setOnAction(e -> {
                db.unbanUser(entry.getValue().getUsername());
                updateGui();
            });

            // add items to menu
            contextMenu.getItems().addAll(changeName, removeUser, block, ban, unblock, unban);

            // add the context menu to the label (room)
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(label, event.getScreenX(), event.getScreenY());
                }
            });

            // add the label to the roomlist (VBox)
            gc.registeredUserList.getChildren().add(label);
        }

    }

    public boolean saveLog() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Log.db"));
            oos.writeObject(log);
            oos.close();

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    public void log(String msg) {
        Time time = new Time();
        String fullMsg = "[" + time + "] " + msg;
        addLabel(fullMsg);
        log.add(fullMsg);
        saveLog();
    }

    private void addLabel(String s) {
        // show text in multiple lines, if text wider than sp
        // we have to use Text bc otherwise everythhing but the first line is cut off,
        // if vBox was already "full" before
        Label label = new Label();
        Text wrappedText = new Text(s);
        Platform.runLater(() -> wrappedText.wrappingWidthProperty().bind(gc.logBox.widthProperty()));// Set wrapping
                                                                                                     // width to VBox
                                                                                                     // width
        label.setGraphic(wrappedText);

        // add to the box
        Platform.runLater(() -> gc.logBox.getChildren().add(label));
    }

    public void viewAll() {
        gc.logBox.getChildren().clear();

        for (String s : log) {
            addLabel(s);
        }
    }

}
