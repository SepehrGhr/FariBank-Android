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

    public static void getNewContactInfo() {
        System.out.println(Color.WHITE + "Please enter the name of your new contact" + Color.RESET);
        String name = InputManager.getInput();
        System.out.println(Color.WHITE + "Please enter the lastname of your new contact" + Color.RESET);
        String lastName = InputManager.getInput();
        System.out.println(Color.WHITE + "Please enter the phone number of your new contact");
        String phoneNumber = InputManager.getInput();
        while (!Menu.checkPhoneNumberValidity(phoneNumber)) {
            System.out.println(Color.RED + "Please enter your phone number correctly" + Color.RESET);
            phoneNumber = InputManager.getInput();
        }
        if (Main.getUsers().getCurrentUser().contactAlreadyExists(phoneNumber)) {
            System.out.println(Color.RED + "A contact with this phone number already exists" + Color.RESET);
            return;
        }
        User contactUser = Main.getUsers().findUserByPhoneNumber(phoneNumber);
        if (contactUser == null) {
            System.out.println(Color.RED + "There is no user with this phone number in our bank" + Color.RESET);
            return;
        }
        Main.getUsers().getCurrentUser().addNewContact(new Contact(contactUser, name, lastName, phoneNumber));
        System.out.println(Color.GREEN + "Your new contact has been successfully added!!" + Color.RESET);
    }

    @Override
    public String toString() {
        return "name : " + name + '\n' +
                "lastName : " +  lastName + '\n' +
                "phoneNumber : " + phoneNumber + '\n';
    }

    public void edit(String newName, String newLastName, String newPhoneNum) {
        name = newName;
        lastName = newLastName;
        phoneNumber = newPhoneNum;
        user = Main.getUsers().findUserByPhoneNumber(newPhoneNum);
//            if (Main.getUsers().getCurrentUser().contactAlreadyExists(selection)) {
//                System.out.println(Color.RED + "A contact with this phone number already exists" + Color.RESET);
//                return;
//            }
//            if (Main.getUsers().findUserByPhoneNumber(selection) == null) {
//                System.out.println(Color.RED + "There is no user with this phone number in our bank" + Color.RESET);
//                return;
//            }
//            updatePhoneNumber(selection);
//        }
    }

    private void updatePhoneNumber(String selection) {
        user = Main.getUsers().findUserByPhoneNumber(selection);
        phoneNumber = selection;
        System.out.println(Color.GREEN + "Selected contact has been successfully edited!!" + Color.RESET);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(user, contact.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
