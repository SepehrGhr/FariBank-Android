package ir.ac.kntu;

import java.util.HashSet;
import java.util.Set;

public class Admin {
    private String name;
    private String username;
    private String password;
    private Set<Type> allowedTypes;

    private boolean blocked = false;

    public Admin(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        setAllowedTypes();
    }

    private void setAllowedTypes() {
        allowedTypes = new HashSet<>();
        allowedTypes.add(Type.SETTINGS);
        allowedTypes.add(Type.REPORT);
        allowedTypes.add(Type.CONTACTS);
        allowedTypes.add(Type.TRANSFER);
        allowedTypes.add(Type.AUTHENTICATION);
        allowedTypes.add(Type.CARD);
        allowedTypes.add(Type.FUNDS);
        allowedTypes.add(Type.CHARGE);
    }

    public void addAllowed(Type type){
        allowedTypes.add(type);
    }

    public void removeAllowed(Type type){
        allowedTypes.remove(type);
    }

    public boolean isAllowed(Type type){
        return allowedTypes.contains(type);
    }

    public String getName() {
        return name;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return Color.CYAN + "*".repeat(35) + '\n' +
                Color.YELLOW + "Admin: " + '\n' +
                Color.WHITE + "Name: " + Color.BLUE + name + '\n' +
                Color.WHITE + "Username: " + Color.BLUE + username + '\n' +
                Color.WHITE + "Password: " + Color.BLUE + password + '\n' +
                Color.CYAN + "*".repeat(35) + Color.RESET;
    }
}
