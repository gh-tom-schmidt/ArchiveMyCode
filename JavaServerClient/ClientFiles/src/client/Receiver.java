package client;

import java.io.IOException;
import java.io.ObjectInputStream;

import helper.Message;
import main.GuiController;

public class Receiver extends Thread {

    private ObjectInputStream inFromServer;
    private Display display;
    private Update update;

    public Receiver(GuiController ctr, ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
        display = new Display(ctr);
        update = new Update(ctr);
    }

    @Override
    public void run() {
        update.status("online");

        while (true) {
            try {
                // wait for message from sever
                Message msg = (Message) inFromServer.readObject();

                // text message
                if (msg.is("msg"))
                    display.text(msg);

                // update roomlist
                else if (msg.is("ur"))
                    update.updateGui(msg);

                // PDF
                else if (msg.is("pdf"))
                    display.pdf(msg);

                else if (msg.is("img"))
                    display.img(msg);

                // clear the useres chatBox, if Server removed user from room    
                else if (msg.is("clear"))
                    display.clearChat();
                    
            } catch (ClassNotFoundException | IOException e) {
                update.status("offline");
                display.disable();
                break;
            }

        }
    }
}
