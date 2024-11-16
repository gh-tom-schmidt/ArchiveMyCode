package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import helper.Profile;

// the database manages the userdata
public class Database {

    // we use a hashtable
    // we use cannot use a record here, so we create a new class Profile and use
    // this instaed
    private Hashtable<String, Profile> userdata;

    private Display display;

    @SuppressWarnings("unchecked")
    public Database(Display display) {

        this.display = display;

        try {
            // we try to load the saved userdata if present
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Userdata.db"));

            this.userdata = (Hashtable<String, Profile>) ois.readObject();

            ois.close();

            this.display.log("[Server] Userdata.db loaded.");

        } catch (IOException | ClassNotFoundException e) {
            // otherwise we create a new database
            this.userdata = new Hashtable<String, Profile>();

            this.display.log("[Server] New Userdata.db File created.");
        }

    }

    // get the profile from the name
    public Profile get(String username) {
        return this.userdata.get(username);
    }

    public Hashtable<String, Profile> getData() {
        return userdata;
    }

    // add a new user (return null if user exists)
    public boolean addUser(String username, String password) {
        // the default values for block and ban are false
        return this.userdata.putIfAbsent(username, new Profile(username, password, null, false)) == null;
    }

    // check is user exists
    public boolean exists(String username) {
        return this.userdata.containsKey(username);
    }

    // check if the password of the user is the same
    public boolean checkPassword(String username, String password) {
        return this.get(username).getPassword().equals(password);
    }

    // remove a user
    public void removeUser(String username) {
        this.userdata.remove(username);
        display.log("[Server] User " + username + " removed.");
    }

    // ban a user
    public void banUser(String username) {
        this.get(username).setBanned(true);
        display.log("[Server] User " + username + " banned.");
    }

    // unban a user
    public void unbanUser(String username) {
        this.get(username).setBanned(false);
        display.log("[Server] User " + username + " unbanned.");
    }

    // block a user
    public void blockUser(String username) {
        this.get(username).setBlocked(1);
        display.log("[Server] User " + username + " blocked.");
    }

    // block a user
    public void unblockUser(String username) {
        this.get(username).clearBlock();
        display.log("[Server] User " + username + " unblocked.");
    }

    // check if user is blocked or banned
    public Boolean isBlockedOrBand(String username) {
        return this.get(username).isBlocked() || this.get(username).isBanned();
    }

    // change the name of the user
    public void changeUsername(String username, String new_username) {
        if (!exists(new_username)) {
            Profile pf = get(username);
            pf.setUsername(new_username);

            this.userdata.remove(username);
            this.userdata.put(new_username, pf);

            display.log("[Server] Username" + username + " changed to " + new_username);
        }

    }

    // save the userdata (overrides old file)
    public boolean saveUserdata() {
        try {
            // try to save the userdata to a file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Userdata.db"));
            oos.writeObject(this.userdata);
            oos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
