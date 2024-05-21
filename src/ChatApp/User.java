package ChatApp;

import java.util.List;

public class User {

    private final String name;
    private final String ipAddress;
    private final boolean active;
    public List<User> userList;

    public User(String name, String ipAddress, boolean active) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.active = true;
    }

    public String getName() {
        return name;
    }
}
