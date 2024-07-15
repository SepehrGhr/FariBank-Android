package ir.ac.kntu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Manager {
    private String name;
    private String password;
    private int level;
    private boolean blocked = false;

    public Manager(String name, String password, int level) {
        this.name = name;
        this.password = password;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return Color.CYAN + "*".repeat(35) + '\n' +
                Color.RED + "Manager: " + '\n' +
                Color.WHITE + "Name: " + Color.BLUE + name + '\n' +
                Color.WHITE + "Password: " + Color.BLUE + password + '\n' +
                Color.CYAN + "*".repeat(35) + Color.RESET;
    }
}
