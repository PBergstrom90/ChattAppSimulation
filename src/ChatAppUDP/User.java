package ChatAppUDP;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String name;
    private boolean active;
    public static List<User> userList = new ArrayList<>();

    public User(String name) {
        this.name = name;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
