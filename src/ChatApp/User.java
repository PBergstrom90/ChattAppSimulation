package ChatApp;

public class User {

    private String name;
    private String ipAddress;
    private int number;

    public User(String name, String ipAddress, int number) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
