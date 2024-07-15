package ir.ac.kntu;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {
    private static Scanner input = new Scanner(System.in);

    public static String getInput() {
        return input.nextLine().trim();
    }

    public static boolean isInputValid(String input, int max) {
        String checkNumeric = "\\d+";
        Pattern pattern = Pattern.compile(checkNumeric);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches() && input.length() < 5 && Integer.parseInt(input) <= max && Integer.parseInt(input) > 0;
    }

    public static void handleSelectRuleInput() {
        String selection = getSelection(4);
        if ("1".equals(selection)) {
            Menu.printMenu(OptionEnums.SignOrLogin.values(), InputManager::handleSignOrLogin);
            return;
        } else if ("2".equals(selection)) {
            Menu.printAdminLoginMenu();
            return;
        } else if ("3".equals(selection)) {
            Menu.printManagerLogin();
            Menu.printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
        }
        Menu.endProgram();
    }

    public static String getSelection(int options) {
        String selection = getInput();
        while (!isInputValid(selection, options)) {
            System.out.println(Color.RED + "Please enter a number between 1 and " + options + Color.RESET);
            selection = getInput();
        }
        return selection;
    }

    public static void handleAdminInput() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                Main.getAdminData().displayTicketsMenu();
                Menu.printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
            }
            case "2" -> Menu.printMenu(OptionEnums.AdminUserMenu.values(), Main.getUsers()::handleAdminUserInput);
            default -> {
                Main.getAdminData().setCurrentAdmin(null);
                Menu.printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
            }
        }
    }

    public static void handleManagerMenuInput() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> {
                Menu.printMenu(OptionEnums.ManagerSettingMenu.values(), InputManager::handleManagerSetting);
                Menu.printMenu(OptionEnums.ManagerMenu.values(), InputManager::handleManagerMenuInput);
            }
            case "2" -> {
                Menu.printMenu(OptionEnums.ManagerUserManageMenu.values(), InputManager::handleManagerUserManage);
                Menu.printMenu(OptionEnums.ManagerMenu.values(), InputManager::handleManagerMenuInput);
            }
            case "3" -> {
                Menu.printMenu(OptionEnums.ManagerAutoTransMenu.values(), InputManager::handleAutoTransInput);
                Menu.printMenu(OptionEnums.ManagerMenu.values(), InputManager::handleManagerMenuInput);
            }
            default -> managerLogout();
        }
    }

    private static void handleManagerUserManage() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> Menu.printMenu(OptionEnums.ManagerViewUserMenu.values(), InputManager::handleManagerViewUser);
            case "2" -> Menu.printMenu(OptionEnums.ManagerAddMenu.values(), Main.getManagerData()::handleManagerAdd);
            default -> {
            }
        }
    }

    private static void handleManagerViewUser() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                Main.getManagerData().showListOfEveryone();
                Menu.printMenu(OptionEnums.ManagerViewUserMenu.values(), InputManager::handleManagerViewUser);
            }
            case "2" -> {
                Main.getManagerData().showListWithFilter();
            }
            default -> {
            }
        }
    }

    private static void handleAutoTransInput() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> Main.getManagerData().doAllPayas();
            case "2" -> {
                Main.getManagerData().depositMonthlyInterest();
                System.out.println(Color.GREEN + "a monthly interest has been successfully deposited to all users with active interest fund" + Color.RESET);
            }
            default -> {
            }
        }
    }

    private static void handleManagerSetting() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                Main.getManagerData().getCurrentManager().changeFeeMenu();
                Menu.printMenu(OptionEnums.ManagerSettingMenu.values(), InputManager::handleManagerSetting);
            }
            case "2" -> {
                Main.getManagerData().getCurrentManager().changeInterestRate();
                Menu.printMenu(OptionEnums.ManagerSettingMenu.values(), InputManager::handleManagerSetting);
            }
            default -> {
            }
        }
    }

    private static void managerLogout() {
        Main.getManagerData().setCurrentManager(null);
        Menu.printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
    }

    public static void handleSignOrLogin() {
        String selection = getSelection(3);
        if ("1".equals(selection)) {
            Menu.printUserLoginMenu();
            return;
        }
        if ("2".equals(selection)) {
            Menu.printSignUpMenu();
        }
        Menu.printMenu(OptionEnums.SelectRuleOption.values(), InputManager::handleSelectRuleInput);
    }

    public static void handleUserMainMenuInput() {
        String selection = getSelection(10);
        OptionEnums.UserMainMenuOption[] options = OptionEnums.UserMainMenuOption.values();
        String selected = options[Integer.parseInt(selection) - 1].toString();
        switch (selected) {
            case "ACCOUNT_MANAGEMENT" -> {
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
                Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
            }
            case "FUNDS_MANAGEMENT" -> {
                Menu.printMenu(OptionEnums.FundManagementOptions.values(), InputManager::handleFundMenuInput);
            }
            case "CONTACTS" -> {
                Menu.printContactsMenu();
                Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
            }
            case "TRANSFER_MONEY" ->
                    Menu.printMenu(OptionEnums.TransferMenuOption.values(), InputManager::handleTransferMethod);
            case "CHARGE_SIM" ->
                    Menu.printMenu(OptionEnums.ChargeSimOptions.values(), InputManager::handleChargeMethod);
            case "SUPPORT" -> Menu.printMenu(OptionEnums.SupportMenuOption.values(), InputManager::handleSupportInput);
            case "SETTINGS" ->
                    Menu.printMenu(OptionEnums.SettingsMenuOption.values(), InputManager::handleSettingsInput);
            case "ACCOUNT_DETAILS" -> {
                Menu.printAccountDetails();
                Main.getUsers().getCurrentUser().generateReport();
                Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
            }
            case "LOG_OUT" -> Menu.userLogout();
            default -> Menu.endProgram();
        }
    }

    private static void handleFundMenuInput() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                Main.getUsers().getCurrentUser().showAndSelectFunds();
                Menu.printMenu(OptionEnums.FundManagementOptions.values(), InputManager::handleFundMenuInput);
            }
            case "2" -> {
                printAddFundMenu();
                Menu.printMenu(OptionEnums.FundManagementOptions.values(), InputManager::handleFundMenuInput);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    private static void printAddFundMenu() {
        System.out.println(Color.WHITE + "Please select the type of your new fund" + Color.RESET);
        System.out.println(Color.WHITE + "1-" + Color.BLUE + "Saving Fund" + Color.RESET);
        System.out.println(Color.WHITE + "2-" + Color.BLUE + "Interest Fund" + Color.RESET);
        System.out.println(Color.WHITE + "3-" + Color.BLUE + "Remaining Fund" + Color.RESET);
        System.out.println(Color.WHITE + "4-" + Color.BLUE + "Return" + Color.RESET);
        handleAddFundMenuInput();
    }

    private static void handleAddFundMenuInput() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> createNewSavingFund();
            case "2" -> createNewInterestFund();
            case "3" -> createNewRemainderFund();
            default -> Menu.printMenu(OptionEnums.FundManagementOptions.values(), InputManager::handleFundMenuInput);
        }
    }

    private static void createNewSavingFund() {
        SavingFund newFund = new SavingFund(Main.getUsers().getCurrentUser());
        Main.getUsers().getCurrentUser().addFund(newFund);
        System.out.println(Color.GREEN + "Your new fund has been successfully created!" + Color.RESET);
    }

    private static void createNewRemainderFund() {
        if (Main.getUsers().getCurrentUser().isHasRemainderFund()) {
            System.out.println(Color.RED + "You cant have more than one remainder fund at once!" + Color.RESET);
            return;
        }
        RemainderFund newFund = new RemainderFund(Main.getUsers().getCurrentUser());
        Main.getUsers().getCurrentUser().addFund(newFund);
        Main.getUsers().getCurrentUser().setHasRemainderFund(true);
        Main.getUsers().getCurrentUser().setRemainderFund(newFund);
        System.out.println(Color.GREEN + "Your new fund has been successfully created!" + Color.RESET);
    }

    private static void createNewInterestFund() {
        int monthCount = Integer.parseInt(getInterestMonthCount());
        long amount = getInterestStartAmount();
        if (amount == -1) {
            return;
        }
        InterestFund newFund = new InterestFund(Main.getUsers().getCurrentUser(), monthCount, amount);
        Main.getUsers().getCurrentUser().addFund(newFund);
        Main.getManagerData().addNewInterestFund(newFund);
        System.out.println(Color.GREEN + "Your new fund has been successfully created!" + Color.RESET);
    }

    private static long getInterestStartAmount() {
        System.out.println(Color.WHITE + "Please enter the amount you want to invest (Maximum 12 digits)" + Color.RESET);
        String amount = InputManager.getInput();
        while (!InputManager.chargeAmountValidity(amount)) {
            System.out.println(Color.RED + "Please enter a valid number (Maximum 12 digits , Minimum 1)" + Color.RESET);
            amount = InputManager.getInput();
        }
        if (Long.parseLong(amount) <= Main.getUsers().getCurrentUser().getAccount().getBalance()) {
            Main.getUsers().getCurrentUser().getAccount().setBalance(Main.getUsers().getCurrentUser().getAccount().getBalance() - Long.parseLong(amount));
            return Long.parseLong(amount);
        } else {
            System.out.println(Color.RED + "Your account balance is not enough for this amount!!" + Color.RESET);
            return -1;
        }
    }

    private static String getInterestMonthCount() {
        System.out.println(Color.WHITE + "Please enter the number of months you want to let your money rest(Maximum 24)" + Color.RESET);
        System.out.println(Color.WHITE + "You cant deposit or withdraw from your fund in this time span" + Color.RESET);
        return getSelection(24);
    }

    private static void handleChargeMethod() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> {
                Main.getUsers().getCurrentUser().getSimCard().printChargeSimCard();
                Menu.printMenu(OptionEnums.ChargeSimOptions.values(), InputManager::handleChargeMethod);
            }
            case "2" -> {
                Contact selected = Main.getUsers().getCurrentUser().selectContactFromList();
                if (selected != null) {
                    selected.getUser().getSimCard().printChargeSimCard();
                }
                Menu.printMenu(OptionEnums.ChargeSimOptions.values(), InputManager::handleChargeMethod);
            }
            case "3" -> {
                Main.getUsers().chargeSimByNumber();
                Menu.printMenu(OptionEnums.ChargeSimOptions.values(), InputManager::handleChargeMethod);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleSupportInput() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                //Ticket.submitNewTicket();
                Menu.printMenu(OptionEnums.SupportMenuOption.values(), InputManager::handleSupportInput);
            }
            case "2" -> {
                Main.getUsers().getCurrentUser().displayTickets();
                Menu.printMenu(OptionEnums.SupportMenuOption.values(), InputManager::handleSupportInput);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleSettingsInput() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> {
                Main.getUsers().getCurrentUser().changePassword();
                Menu.printMenu(OptionEnums.SettingsMenuOption.values(), InputManager::handleSettingsInput);
            }
            case "2" -> {
                Main.getUsers().getCurrentUser().changeCreditPassword();
                Menu.printMenu(OptionEnums.SettingsMenuOption.values(), InputManager::handleSettingsInput);
            }
            case "3" -> {
                Main.getUsers().getCurrentUser().changeContactStatus();
                Menu.printMenu(OptionEnums.SettingsMenuOption.values(), InputManager::handleSettingsInput);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleTransferMethod() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> {
                Main.getUsers().transferByAccountID();
                Menu.printMenu(OptionEnums.TransferMenuOption.values(), InputManager::handleTransferMethod);
            }
            case "2" -> {
                Main.getUsers().transferByContact();
                Menu.printMenu(OptionEnums.TransferMenuOption.values(), InputManager::handleTransferMethod);
            }
            case "3" -> {
                Main.getUsers().transferByRecentUser();
                Menu.printMenu(OptionEnums.TransferMenuOption.values(), InputManager::handleTransferMethod);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleContactsInput() {
        String selection = getSelection(3);
        switch (selection) {
            case "1" -> {
                Contact.getNewContactInfo();
                Menu.printContactsMenu();
            }
            case "2" -> {
                //Main.getUsers().getCurrentUser().showAndEditContact();
                Menu.printContactsMenu();
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleManagementInput() {
        String selection = getSelection(5);
        switch (selection) {
            case "1" -> {
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            case "2" -> {
                Main.getUsers().getCurrentUser().getAccount().displayBalance();
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            case "3" -> {
                Main.getUsers().getCurrentUser().getSimCard().displayBalance();
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            case "4" -> {
                Menu.printMenu(OptionEnums.ShowReceiptsOption.values(), InputManager::handleShowReceipt);
            }
            default -> Menu.printMenu(OptionEnums.UserMainMenuOption.values(), InputManager::handleUserMainMenuInput);
        }
    }

    public static void handleShowReceipt() {
        String selection = getSelection(4);
        switch (selection) {
            case "1" -> {
                Main.getUsers().getCurrentUser().displayReceipts();
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            case "2" -> {
                Main.getUsers().getCurrentUser().filterReceipt();
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            case "3" -> {
                Main.getUsers().getCurrentUser().generateReport();
                Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
            }
            default -> Menu.printMenu(OptionEnums.ManagementMenuOption.values(), InputManager::handleManagementInput);
        }
    }

    public static boolean chargeAmountValidity(String input) {
        String numberRegex = "\\d+";
        Pattern numPattern = Pattern.compile(numberRegex);
        Matcher numMatcher = numPattern.matcher(input);
        return numMatcher.matches() && input.length() < 13 && Long.parseLong(input) != 0;
    }

    public static String setUserName() {
        String name = InputManager.getInput();
        while (!checkStringValidity(name)) {
            System.out.println(Color.RED + "Please enter your name correctly" + Color.RESET);
            name = InputManager.getInput();
        }
        return name;
    }

    public static boolean checkStringValidity(String name) {
        String regex = "[a-zA-z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
