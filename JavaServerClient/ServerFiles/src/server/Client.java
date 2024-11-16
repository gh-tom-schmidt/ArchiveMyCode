package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import helper.Message;

public class Client extends Thread {

    private RoomManager rm;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private Database db;
    private Socket socket;

    public Client(Socket socket, RoomManager rm, Database db) throws IOException {

        // create an input and output stream to send and receive from client
        outToClient = new ObjectOutputStream(socket.getOutputStream());
        inFromClient = new ObjectInputStream(socket.getInputStream());

        this.rm = rm;
        this.db = db;
        this.socket = socket;
    }

    public void send(Message msg) {
        try {
            outToClient.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        send(new Message("msg", "The server closed your connection."));
        try {
            inFromClient.close();
            outToClient.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = (Message) inFromClient.readObject();

                if (msg.is("l")) {
                    String username = msg.getString1();

                    if (!db.exists(username))
                        send(new Message("e", "User doesn't exist."));

                    else if (db.isBlockedOrBand(username))
                        send(new Message("e", "You are banned or blocked."));

                    else if (!db.checkPassword(username, msg.getString2()))
                        send(new Message("e", "Wrong Password."));

                    else if (rm.getClientFromUsername(username) != null)
                        send(new Message("e", "User is already on the server."));

                    else {
                        setName(username);
                        send(new Message("s", "You are now logged in."));
                        rm.addClientToServer(this);
                    }

                } else if (msg.is("r")) {
                    if (!db.addUser(msg.getString1(), msg.getString2())) {
                        send(new Message("e", "User already exists."));

                    } else {
                        send(new Message("s", "You are now registered."));
                        db.saveUserdata();
                    }

                } else if (msg.is("changeRoom")) {
                    rm.addClientToRoom(msg.getString1(), this.getName());

                } else if (msg.is("createPrivateRoom")) {
                    rm.addRoom(msg.getString1() + "-" + getName(), getName(), msg.getString1());

                } else if (msg.is("renamePrivateRoom")) {
                    rm.changeRoomname(msg.getString1(), msg.getString2());

                } else if (msg.is("deletePrivateRoom")) {
                    rm.removeRoom(msg.getString1());
                }

                else if (msg.is("msg")) {
                    rm.sendToRoom(this.getName(), new Message("msg", "[" + getName() + "] " + msg.getString1()));

                } else if (msg.is("pdf") || msg.is("img")) {
                    rm.sendToRoom(this.getName(), msg);

                }

            } catch (IOException | ClassNotFoundException e) {
                rm.removeClientFromServer(getName());
                break;
            }
        }
    }

}
