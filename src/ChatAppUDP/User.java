package ChatAppUDP;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final String name;
    private boolean active;
    public static List<User> userList = new ArrayList<>();

    public User(String name, boolean active) {
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

    // Following functions provides a "Class Equality Check", to verify that a new User is being added correctly.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
