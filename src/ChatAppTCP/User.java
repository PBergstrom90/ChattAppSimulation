package ChatAppTCP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

    private final String name;
    private boolean active;
    // Use synchronized list to ensure thread safety, when multiple instances access the userList.
    public static final List<User> userList = Collections.synchronizedList(new ArrayList<>());

    public User(String name) {
        this.name = name;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
