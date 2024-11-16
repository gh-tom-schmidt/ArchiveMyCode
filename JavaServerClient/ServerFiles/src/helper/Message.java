package helper;

import java.io.Serializable;
import java.util.Hashtable;

public class Message implements Serializable {

    private String is;
    private String string1;
    private String string2;
    private Hashtable<String, Room> room_list;
    private byte[] bytes;
    private String filename;

    public Message(String is, String string1) {
        this.is = is;
        this.string1 = string1;
    }

    public Message(String is, String string1, String string2) {
        this.is = is;
        this.string1 = string1;
        this.string2 = string2;

    }

    public Message(String is, Hashtable<String, Room> room_list) {
        this.is = is;
        this.room_list = room_list;
    }

    public Message(String is, String filename, byte[] bytes) {
        this.is = is;
        this.filename = filename;
        this.bytes = bytes;
    }

    public Boolean is(String code) {
        return is.equals(code);
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Hashtable<String, Room> getRoomList() {
        return room_list;
    }

    public String getString1() {
        return string1;
    }

    public String getString2() {
        return string2;
    }

}
