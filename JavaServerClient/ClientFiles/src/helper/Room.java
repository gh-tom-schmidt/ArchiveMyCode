package helper;

import java.util.ArrayList;

public class Room extends ArrayList<String> {

    private String user1;
    private String user2;

    public Room() {
        super();
    }

    public Room(String user1, String user2) {
        super();
        this.user1 = user1;
        this.user2 = user2;
    }

    public Room(Room oldRoom) {
        super(oldRoom);
        this.user1 = oldRoom.getUser1();
        this.user2 = oldRoom.getUser2();
    }

    public Boolean hasLimit() {
        return user1 != null && user2 != null;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }
}
