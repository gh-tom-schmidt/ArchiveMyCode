package server;

import java.util.Hashtable;
import java.util.Map;

import helper.Message;
import helper.Room;

// the RoomManger keeps track of all rooms and the clients in the rooms
public class RoomManager {

    // using a Hashtable to store a list of clients under a given room name
    private Hashtable<String, Room> rooms = new Hashtable<>();
    private Hashtable<String, Client> clients = new Hashtable<>();

    private String default_room;
    private Display display;

    public RoomManager(Display display) {
        this.display = display;
    }

    public boolean setDefault(String default_room) {
        this.default_room = default_room;
        return addRoom(default_room);
    }

    public String getDefaultRoom() {
        return default_room;
    }

    public Hashtable<String, Room> getData() {
        return rooms;
    }

    public Client getClientFromUsername(String username) {
        return clients.get(username);
    }

    public String getRoomWhereUserIsIn(String username) {

        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            if (entry.getValue().contains(username))
                return entry.getKey();
        }
        return null;
    }

    public boolean addClientToServer(Client client) {
        return clients.putIfAbsent(client.getName(), client) == null
                && addClientToRoom(default_room, client.getName());
    }

    public boolean removeClientFromServer(String username) {
        return removeClientFromRoom(username)
                && clients.remove(username) != null;
    }

    public boolean addRoom(String roomname) {
        return rooms.putIfAbsent(roomname, new Room()) == null
                && display.updateGui();
    }

    public boolean addRoom(String roomname, String user1, String user2) {
        return rooms.putIfAbsent(roomname, new Room(user1, user2)) == null
                && display.updateGui();
    }

    public boolean removeRoom(String roomname) {
        if (!roomname.equals(this.default_room)) {
            // clear the chatBoxes, bc all user in removed room are moved to default room
            for (String username : rooms.get(roomname)) {
                getClientFromUsername(username).send(new Message("clear", ""));
            }
            if (!rooms.get(roomname).isEmpty()) // check if empty first!
                return rooms.get(default_room).addAll(rooms.get(roomname))
                        && (rooms.remove(roomname) != null)
                        && display.updateGui();

            else
                return (rooms.remove(roomname) != null)
                        && display.updateGui();
        }
        return false;

    }

    public boolean removeClientFromRoom(String username) {
        String room = getRoomWhereUserIsIn(username);

        return (room != null)
                && rooms.get(room).remove(username)
                && display.updateGui();
    }

    public boolean addClientToRoom(String roomname, String username) {
        removeClientFromRoom(username);

        Room room = rooms.get(roomname);

        if (room.hasLimit())
            return (room.getUser1().equals(username) || room.getUser2().equals(username))
                    && room.add(username)
                    && display.updateGui();

        return room.add(username) && display.updateGui();
    }

    public void kickClient(String username) {
        if (getClientFromUsername(username) != null)
            getClientFromUsername(username).close();
        display.log("[Server] Client " + username + " kicked.");
        display.updateGui();
    }

    public boolean changeRoomname(String old_roomname, String new_roomname) {
        // check if roomname is ok
        if (new_roomname == null || new_roomname.length() < 1 || old_roomname.equals(this.default_room)
                || old_roomname.equals(new_roomname))
            return false;

        // no cases needed
        return rooms.putIfAbsent(new_roomname, rooms.get(old_roomname)) == null
                && rooms.remove(old_roomname) != null
                && display.updateGui();
    }

    public String areOnline() {
        return String.valueOf(clients.size());
    }

    public void sendToRoom(String fromUser, Message msg) {

        String room_send_to = getRoomWhereUserIsIn(fromUser);

        if (msg.is("msg")) {
            display.log("In room " + room_send_to + ": " + msg.getString1());
        }

        for (String username : rooms.get(room_send_to)) {
            getClientFromUsername(username).send(msg);
        }

    }

    public void sendToAll(Message msg) {

        for (Map.Entry<String, Client> entry : clients.entrySet())
            entry.getValue().send(msg);

    }

}
