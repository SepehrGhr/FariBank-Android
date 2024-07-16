package ir.ac.kntu;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
    private User user;
    private String name;
    private String lastName;

    private String phoneNumber;

    public Contact(User user, String name, String lastName, String phoneNumber) {
        this.user = user;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "name : " + name + '\n' +
                "lastName : " + lastName + '\n' +
                "phoneNumber : " + phoneNumber + '\n';
    }

    public void edit(String newName, String newLastName, String newPhoneNum) {
        name = newName;
        lastName = newLastName;
        phoneNumber = newPhoneNum;
        user = Main.getUsers().findUserByPhoneNumber(newPhoneNum);
    }

    private void updatePhoneNumber(String selection) {
        user = Main.getUsers().findUserByPhoneNumber(selection);
        phoneNumber = selection;
        System.out.println(Color.GREEN + "Selected contact has been successfully edited!!" + Color.RESET);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        return Objects.equals(user, contact.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
