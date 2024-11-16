package client;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import helper.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.GuiController;

public class Display {

    private GuiController ctr;

    public Display(GuiController ctr) {
        this.ctr = ctr;
    }

    public void text(Message msg) {
        addLabel(msg.getString1(), ctr.chatBox);
    }

    public void img(Message msg) {
        Platform.runLater(() -> {

            Label label = new Label("  IMAGE  ");
            label.setFont(new Font("Calibri", 18));
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #32aee8; -fx-background-radius: 20px; -fx-cursor: HAND");

            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {

                    // get the bytes from the message
                    byte[] bytes = msg.getBytes();
                    FileOutputStream fos;
                    ByteArrayInputStream bais;
                    try {
                        // safe the bytes to a jpeg file
                        fos = new FileOutputStream(msg.getFilename() + ".jpeg");
                        fos.write(bytes);
                        // view image in sp
                        bais = new ByteArrayInputStream(bytes);
                        Image image = new Image(bais);
                        ImageView imageView = new ImageView(image);
                        imageView.setViewport(
                                new Rectangle2D(0, 0, image.getWidth(), image.getHeight()));
                        imageView.setFitWidth(360);
                        imageView.setFitHeight(100);
                        imageView.setPreserveRatio(true);
                        // add Image to label and add label to chatBox
                        Label labelPic = new Label();
                        labelPic.setStyle("-fx-background-color: #3a3c4a");
                        labelPic.setGraphic(imageView);
                        ctr.chatBox.getChildren().add(labelPic);
                        fos.close();
                        bais.close();
                        label.setDisable(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            ctr.chatBox.getChildren().add(label);

        });

    }

    public void pdf(Message msg) {
        Platform.runLater(() -> {

            Label label = new Label("  PDF  ");
            label.setFont(new Font("Calibri", 18));
            label.setStyle(
                    "-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #d04949; -fx-background-radius: 20px; -fx-cursor: HAND");

            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {

                    // get the bytes from the message
                    byte[] bytes = msg.getBytes();
                    FileOutputStream fos;
                    try {
                        // safe the bytes to a pdf file
                        fos = new FileOutputStream(msg.getFilename() + ".pdf");
                        fos.write(bytes);
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            ctr.chatBox.getChildren().add(label);

        });
    }
    
    // to clear chatBox, if Server removed user from room
    @FXML
    public void clearChat(){
        Platform.runLater(() -> ctr.chatBox.getChildren().clear());
    }

    // disable all gui buttons etc
    @FXML
    public void disable() {
        Platform.runLater(() -> {
            ctr.privateRoomList.getChildren().clear();
            ctr.roomList.getChildren().clear();
            ctr.userList.getChildren().clear();
            ctr.userRoomList.getChildren().clear();
            ctr.sendMsgButton.setDisable(true);
            ctr.sendImageButton.setDisable(true);
            ctr.sendPdfButton.setDisable(true);

        });
    }

    @FXML
    private void addLabel(String s, VBox box) {
        // show text in multiple lines, if text wider than sp
        // we have to use Text bc otherwise everythhing but the first line is cut off, if vBox was already "full" before
        Label label = new Label();
        Text wrappedText = new Text(s);
        Platform.runLater(() -> wrappedText.wrappingWidthProperty().bind(box.widthProperty()));// Set wrapping width to VBox width
        label.setGraphic(wrappedText);

        // add to the box
        Platform.runLater(() -> box.getChildren().add(label));
    }

}
