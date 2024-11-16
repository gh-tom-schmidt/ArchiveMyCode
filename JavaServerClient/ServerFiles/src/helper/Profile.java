package helper;

import java.io.Serializable;

public class Profile implements Serializable {
    private String username;
    private String password;
    private Time blocked;
    private Boolean banned;

    public Profile(String username, String password, Time blocked, Boolean banned) {
        this.username = username;
        this.password = password;
        this.blocked = blocked;
        this.banned = banned;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        username = newUsername;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isBlocked() {
        if(blocked != null && blocked.isOver()) blocked = null;
        return blocked != null;
    }

    public void setBlocked(int minutes) {
        blocked = new Time(minutes);
    }

    public void clearBlock() {
        blocked = null;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean isBanned) {
        banned = isBanned;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Banned: " + banned + ", Blocked: " + blocked;
    }
}
