package ir.ac.kntu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {

    public static <T extends Enum<T>> void printMenu(T[] options, Runnable handleInput) {
        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
        System.out.println(Color.WHITE + "Please select the option you want" + Color.RESET);
        for (int i = 0; i < options.length; i++) {
            String optionName = formatEnumName(options[i]);
            System.out.println(Color.WHITE + (i + 1) + "-" + Color.BLUE + optionName + Color.RESET);
        }
        System.out.println(Color.YELLOW + "<>".repeat(20) + Color.RESET);
        handleInput.run();
    }

    private static <T extends Enum<T>> String formatEnumName(T option) {
        String optionName = option.toString().replace('_', ' ').toLowerCase();
        return Character.toUpperCase(optionName.charAt(0)) + optionName.substring(1);
    }

    public static void printAdminLoginMenu() {
        System.out.println(Color.WHITE + "Please enter your username");
        String input = InputManager.getInput();
        Admin loggingIn = Main.getAdminData().findAdminByUsername(input);
        if (loggingIn == null) {
            System.out.println(Color.RED + "There is no admin with this username" + Color.RESET);
            printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
        } else {
            System.out.println(Color.WHITE + "Please enter your password" + Color.RESET);
            String password = InputManager.getInput();
            while (!password.equals(loggingIn.getPassword())) {
                System.out.println(Color.RED + "Entered password is incorrect , please try again" + Color.RESET);
                password = InputManager.getInput();
            }
            if(loggingIn.isBlocked()){
                System.out.println(Color.RED + "You cant access the bank now because you have been blocked by a manager" + Color.RESET);
                printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
                return;
            }
            Main.getAdminData().setCurrentAdmin(loggingIn);
            printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
        }
    }

    public static void printSignUpMenu() {
        System.out.println(Color.WHITE + "-Please enter your name-" + Color.RESET);
        String name = InputManager.setUserName();
        System.out.println(Color.WHITE + "-Please enter your last name-" + Color.RESET);
        String lastName = InputManager.setUserName();
        System.out.println(Color.WHITE + "-Please enter your phone number-" + Color.RESET);
        String phoneNumber = setPhoneNumber();
        if ("".equals(phoneNumber)) {
            System.out.println(Color.WHITE + "Please login instead" + Color.RESET);
            printMenu(OptionEnums.SignOrLogin.values(), InputManager::handleSignOrLogin);
            return;
        }
        System.out.println(Color.WHITE + "-Please enter your security number-" + Color.RESET);
        String securityNumber = setSecurityNumber();
        if ("".equals(securityNumber)) {
            System.out.println(Color.WHITE + "Please login instead" + Color.RESET);
            printMenu(OptionEnums.SignOrLogin.values(), InputManager::handleSignOrLogin);
            return;
        }
        System.out.println(Color.WHITE + "-Please enter your password-" + Color.WHITE + " (it must contain at least " +
                "one lowercase,uppercase,number and character)" + Color.RESET);
        String password = setPassword();
        System.out.println(Color.GREEN + "Your information has been successfully registered and will be checked soon" + Color.RESET);
        User newUser = Main.getUsers().unregisteredAlreadyExists(phoneNumber) ?
                new User(name, lastName, Main.getUsers().getUnregistered(phoneNumber), securityNumber, password) :
                new User(name, lastName, new PhoneNumber(phoneNumber, 0), securityNumber, password);
        Main.getUsers().addNewUserToDatabase(newUser);
    }

    public static String setPassword() {
        String password = InputManager.getInput();
        while (!checkPasswordValidity(password)) {
            System.out.println(Color.RED + "Your password must contain at least one lowercase,uppercase,number and" +
                    " character, please try again" + Color.RESET);
            password = InputManager.getInput();
        }
        return password;
    }

    public static boolean checkPasswordValidity(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=?])[^\\s]{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }

    public static String setSecurityNumber() {
        String securityNumber = InputManager.getInput();
        while (!checkSecurityNumberValidity(securityNumber)) {
            System.out.println(Color.RED + "Please enter your security number correctly (10 digits)" + Color.RESET);
            securityNumber = InputManager.getInput();
        }
        if (Main.getUsers().findUserBySecurityNumber(securityNumber) != null) {
            System.out.println(Color.RED + "There is already an account with this security number in our bank" + Color.RESET);
            return "";
        }
        return securityNumber;
    }

    private static boolean checkSecurityNumberValidity(String securityNumber) {
        String ssnRegex = "\\d{10}";
        Pattern ssnPattern = Pattern.compile(ssnRegex);
        Matcher ssnMatcher = ssnPattern.matcher(securityNumber);
        return ssnMatcher.matches();
    }

    public static String setPhoneNumber() {
        String phoneNumber = InputManager.getInput();
        while (!checkPhoneNumberValidity(phoneNumber)) {
            System.out.println(Color.RED + "Please enter your phone number correctly" + Color.RESET);
            phoneNumber = InputManager.getInput();
        }
        if (Main.getUsers().findUserByPhoneNumber(phoneNumber) != null) {
            System.out.println(Color.RED + "There is already an user registered with this phone number" + Color.RESET);
            return "";
        }
        return phoneNumber;
    }

    public static boolean checkPhoneNumberValidity(String phoneNumber) {
        String numberRegex = "\\d{11}";
        Pattern numberPattern = Pattern.compile(numberRegex);
        Matcher numberMatcher = numberPattern.matcher(phoneNumber);
        return numberMatcher.matches() && "09".equals(phoneNumber.substring(0, 2));
    }

    public static void printUserLoginMenu() {
        System.out.println(Color.WHITE + "-Please enter your username (Phone number)-" + Color.RESET);
        User loggingIn = getUsername();
        System.out.println(Color.WHITE + "-Please enter your password-" + Color.RESET);
        getPasswordInput(loggingIn);
        if(!loggingIn.isBlocked()) {
            System.out.println(Color.GREEN + "You have successfully logged in!" + Color.RESET);
        }
        Main.getUsers().setCurrentUser(loggingIn);
        showMenuAfterLogin(loggingIn);
    }

    private static void showMenuAfterLogin(User loggingIn) {
        if (loggingIn.isBlocked()){
            System.out.println(Color.RED + "You access to bank has been blocked by a manager!" + Color.RESET);
            System.out.println(Color.WHITE + "enter any key to log out" + Color.RESET);
            InputManager.getInput();
            userLogout();
        }
        if (loggingIn.isAuthenticated()) {
            printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        } else {
            if (Main.getAdminData().getRequests().get(loggingIn).isChecked() && !Main.getAdminData().getRequests().get(loggingIn).isApproved()) {
                Main.getAdminData().getRequests().get(loggingIn).showErrorMassage();
                Main.getAdminData().getRequests().get(loggingIn).editInformation();
                Menu.userLogout();
            } else {
                System.out.println(Color.RED + "We are sorry but your authentication request has not been checked yet, please come back later");
                System.out.println(Color.WHITE + "enter any key to log out" + Color.RESET);
                InputManager.getInput();
                userLogout();
            }
        }
    }

    public static void printContactsMenu() {
        if (!Main.getUsers().getCurrentUser().isContactsActivated()) {
            System.out.println(Color.RED + "You have deactivated contacts option! change it from settings and try again" + Color.RESET);
            return;
        }
        printMenu(OptionEnums.ContactsMenuOption.values(), InputManager::handleContactsInput);
    }

    public static void endProgram() {
        System.out.println(Color.PURPLE + "Thanks for choosing our bank!" + Color.RESET);
        System.exit(0);
    }

    public static void userLogout() {
        Main.getUsers().setCurrentUser(null);
        Menu.printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
    }

    private static void getPasswordInput(User loggingIn) {
        String password = InputManager.getInput();
        while (!(password.equals(loggingIn.getPassword()))) {
            System.out.println(Color.RED + "entered password is incorrect , please try again" + Color.RESET);
            password = InputManager.getInput();
        }
    }

    private static User getUsername() {
        String input = InputManager.getInput();
        User toLoginUser = Main.getUsers().findUserByPhoneNumber(input);
        while (toLoginUser == null) {
            System.out.println(Color.RED + "no user with this phone number exists" + Color.RESET);
            input = InputManager.getInput();
            toLoginUser = Main.getUsers().findUserByPhoneNumber(input);
        }
        return toLoginUser;
    }

    public static void printAccountDetails() {
        User curUser = Main.getUsers().getCurrentUser();
        System.out.println(Color.CYAN + "*".repeat(30) + Color.RESET);
        System.out.println(Color.WHITE + "Full Name : " + Color.BLUE + curUser.getName() + " " + curUser.getLastName() + Color.RESET);
        System.out.println(Color.WHITE + "Phone Number: " + Color.BLUE + curUser.getPhoneNumber() + Color.RESET);
        System.out.println(Color.WHITE + "Security Number: " + Color.BLUE + curUser.getSecurityNumber() + Color.RESET);
        System.out.println(Color.WHITE + "Account ID: " + Color.BLUE + curUser.getAccount().getAccountID() + Color.RESET);
        System.out.println(Color.WHITE + "CreditCard Number: " + Color.BLUE + curUser.getAccount().getCreditCard().getCardNumber() + Color.RESET);
        System.out.println(Color.CYAN + "*".repeat(30) + Color.RESET);
    }

    public static void printManagerLogin() {
        System.out.println(Color.WHITE + "Please enter your username");
        String username = InputManager.getInput();
        Manager loggingIn = Main.getManagerData().findManagerByUsername(username);
        if (loggingIn == null) {
            System.out.println(Color.RED + "There is no manager with this username" + Color.RESET);
        } else {
            System.out.println(Color.WHITE + "Please enter your password" + Color.RESET);
            String password = InputManager.getInput();
            while (!password.equals(loggingIn.getPassword())) {
                System.out.println(Color.RED + "Entered password is incorrect , please try again" + Color.RESET);
                password = InputManager.getInput();
            }
            if(loggingIn.isBlocked()){
                System.out.println(Color.RED + "You cant access the bank cause you have been blocked by another manager." + Color.RESET);
                return;
            }
            Main.getManagerData().setCurrentManager(loggingIn);
            printMenu(OptionEnums.ManagerMenu.values(), InputManager::handleManagerMenuInput);
        }
    }
}
