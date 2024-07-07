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

    public void changeFeeMenu() {
        Menu.printMenu(OptionEnums.ManagerChangeFee.values(), this::selectFeeToChange);
    }

    private void selectFeeToChange() {
        String selection = InputManager.getSelection(6);
        switch (selection) {
            case "1" -> this.changePayaFee();
            case "2" -> this.changePolFee();
            case "3" -> this.changeFariFee();
            case "4" -> this.changeCardFee();
            case "5" -> this.changeSimCardFee();
            default -> {
            }
        }
    }

    private void changePolFee() {
        System.out.println(Color.WHITE + "Please enter your new fee percentage (Maximum: 100)" + Color.RESET);
        System.out.println(Color.WHITE + "Current Pol fee percentage: " + Color.GREEN + Main.getManagerData().getFeeRate().getPolFee() * 100
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 100) {
            System.out.println(Color.RED + "Please enter a valid percentage. Maximum: 100");
            input = InputManager.getInput();
        }
        Main.getManagerData().getFeeRate().setPolFee(Double.parseDouble(input) / 100);
        System.out.println(Color.GREEN + "Pol fee percentage has been successfully changed" + Color.RESET);
    }

    private void changeSimCardFee() {
        System.out.println(Color.WHITE + "Please enter your new fee (Maximum: 10000)" + Color.RESET);
        System.out.println(Color.WHITE + "Current SimCard fee: " + Color.GREEN + Main.getManagerData().getFeeRate().getSimCardFee()
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 10000) {
            System.out.println(Color.RED + "Please enter a valid amount. Maximum: 10000");
            input = InputManager.getInput();
        }
        Main.getManagerData().getFeeRate().setSimCardFee(Long.parseLong(input));
        System.out.println(Color.GREEN + "SimCard fee has been successfully changed" + Color.RESET);
    }

    private void changeCardFee() {
        System.out.println(Color.WHITE + "Please enter your new fee (Maximum: 10000)" + Color.RESET);
        System.out.println(Color.WHITE + "Current Card fee: " + Color.GREEN + Main.getManagerData().getFeeRate().getCardFee()
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 10000) {
            System.out.println(Color.RED + "Please enter a valid amount. Maximum: 10000");
            input = InputManager.getInput();
        }
        Main.getManagerData().getFeeRate().setCardFee(Long.parseLong(input));
        System.out.println(Color.GREEN + "Card fee has been successfully changed" + Color.RESET);
    }

    private void changeFariFee() {
        System.out.println(Color.WHITE + "Please enter your new fee (Maximum: 10000)" + Color.RESET);
        System.out.println(Color.WHITE + "Current Fari fee: " + Color.GREEN + Main.getManagerData().getFeeRate().getFariFee()
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 10000) {
            System.out.println(Color.RED + "Please enter a valid amount. Maximum: 10000");
            input = InputManager.getInput();
        }
        Main.getManagerData().getFeeRate().setFariFee(Long.parseLong(input));
        System.out.println(Color.GREEN + "Fari fee has been successfully changed" + Color.RESET);
    }

    private void changePayaFee() {
        System.out.println(Color.WHITE + "Please enter your new fee (Maximum: 10000)" + Color.RESET);
        System.out.println(Color.WHITE + "Current Paya fee: " + Color.GREEN + Main.getManagerData().getFeeRate().getPayaFee()
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 10000) {
            System.out.println(Color.RED + "Please enter a valid amount. Maximum: 10000");
            input = InputManager.getInput();
        }
        Main.getManagerData().getFeeRate().setPayaFee(Long.parseLong(input));
        System.out.println(Color.GREEN + "Paya fee has been successfully changed" + Color.RESET);
    }

    public static boolean feeAmountValidity(String input) {
        String numberRegex = "\\d+";
        Pattern numPattern = Pattern.compile(numberRegex);
        Matcher numMatcher = numPattern.matcher(input);
        return numMatcher.matches() && input.length() < 13;
    }

    public void changeInterestRate() {
        System.out.println(Color.WHITE + "Please enter your new Interest percentage (Maximum: 100)" + Color.RESET);
        System.out.println(Color.WHITE + "Current Interest percentage: " + Color.GREEN + Main.getManagerData().getInterestRate() * 100
                + Color.RESET);
        String input = InputManager.getInput();
        while (!feeAmountValidity(input) || Long.parseLong(input) > 100) {
            System.out.println(Color.RED + "Please enter a valid percentage. Maximum: 100");
            input = InputManager.getInput();
        }
        Main.getManagerData().setInterestRate(Double.parseDouble(input) / 100);
        System.out.println(Color.GREEN + "Interest percentage has been successfully changed" + Color.RESET);
    }

    public void editUserMenu(User selected) {
        Menu.printMenu(OptionEnums.EditUserMenu.values(), Main.getManagerData().getCurrentManager()::getName);
        String selection = InputManager.getSelection(4);
        OptionEnums.EditUserMenu[] options = OptionEnums.EditUserMenu.values();
        String selectedOption = options[Integer.parseInt(selection) - 1].toString();
        switch (selectedOption) {
            case "CHANGE_NAME" -> this.changeUserName(selected);
            case "CHANGE_LASTNAME" -> this.changeUserLastname(selected);
            case "BLOCK_OR_UNBLOCK" -> this.blockUser(selected);
            default -> {
            }
        }
    }

    private void blockUser(User selected) {
        if (selected.isBlocked()) {
            System.out.println(Color.RED + "this user is currently blocked" + Color.RESET);
        } else {
            System.out.println(Color.GREEN + "this user is currently not blocked" + Color.RESET);
        }
        System.out.println(Color.WHITE + "Please enter" + Color.BLUE + " 1 " + Color.WHITE + "to change block status or"
                + Color.BLUE + " 2 " + Color.WHITE + "to remain the same" + Color.RESET);
        String selection = InputManager.getSelection(2);
        if ("1".equals(selection)) {
            selected.setBlocked(!selected.isBlocked());
            System.out.println(Color.GREEN + "selected user's status was changed successfully" + Color.RESET);
        }
    }

    public boolean canEditOrBlock(Manager toBlock) {
        return this.level < toBlock.getLevel();
    }

    private void changeUserLastname(User selected) {
        System.out.println(Color.WHITE + "Please enter the new lastname" + Color.RESET);
        selected.setLastName(InputManager.setUserName());
        System.out.println(Color.GREEN + "Selected user's lastname has been changed successfully" + Color.RESET);
    }

    private void changeUserName(User selected) {
        System.out.println(Color.WHITE + "Please enter the new name" + Color.RESET);
        selected.setName(InputManager.setUserName());
        System.out.println(Color.GREEN + "Selected user's name has been changed successfully" + Color.RESET);
    }

    @Override
    public String toString() {
        return Color.CYAN + "*".repeat(35) + '\n' +
                Color.RED + "Manager: " + '\n' +
                Color.WHITE + "Name: " + Color.BLUE + name + '\n' +
                Color.WHITE + "Password: " + Color.BLUE + password + '\n' +
                Color.CYAN + "*".repeat(35) + Color.RESET;
    }

    public void editManagerMenu(Manager selected) {
        if (!this.canEditOrBlock(selected)) {
            System.out.println(Color.RED + "You cant edit the information of your higher-ups" + Color.RESET);
            return;
        }
        Menu.printMenu(OptionEnums.EditManagerMenu.values(), Main.getManagerData().getCurrentManager()::getName);
        String selection = InputManager.getSelection(3);
        OptionEnums.EditManagerMenu[] options = OptionEnums.EditManagerMenu.values();
        String selectedOption = options[Integer.parseInt(selection) - 1].toString();
        switch (selectedOption) {
            case "CHANGE_NAME" -> this.changeUsername(selected);
            case "BLOCK_OR_UNBLOCK" -> this.blockManager(selected);
            default -> {
            }
        }
    }

    private void blockManager(Manager selected) {
        if (selected.isBlocked()) {
            System.out.println(Color.RED + "this manager is currently blocked" + Color.RESET);
        } else {
            System.out.println(Color.GREEN + "this manager is currently not blocked" + Color.RESET);
        }
        System.out.println(Color.WHITE + "Please enter" + Color.BLUE + " 1 " + Color.WHITE + "to change block status or"
                + Color.BLUE + " 2 " + Color.WHITE + "to remain the same" + Color.RESET);
        String selection = InputManager.getSelection(2);
        if ("1".equals(selection)) {
            selected.setBlocked(!selected.isBlocked());
            System.out.println(Color.GREEN + "selected manager's status was changed successfully" + Color.RESET);
        }
    }

    private void changeUsername(Manager selected) {
        System.out.println(Color.WHITE + "Please enter the new username" + Color.RESET);
        String username = InputManager.setUserName();
        selected.setName(username);
        System.out.println(Color.GREEN + "Selected manager's name has been successfully changed" + Color.RESET);
    }

    private void changeUsername(Admin selected) {
        System.out.println(Color.WHITE + "Please enter the new username" + Color.RESET);
        String username = InputManager.setUserName();
        selected.setUsername(username);
        System.out.println(Color.GREEN + "Selected manager's name has been successfully changed" + Color.RESET);
    }

    public void editAdminMenu(Admin selected) {
        Menu.printMenu(OptionEnums.EditAdminMenu.values(), this::getName);
        String selection = InputManager.getSelection(4);
        OptionEnums.EditAdminMenu[] options = OptionEnums.EditAdminMenu.values();
        OptionEnums.EditAdminMenu selectedOption = options[Integer.parseInt(selection) - 1];
        switch (selectedOption) {
            case CHANGE_NAME -> this.changeUsername(selected);
            case BLOCK_OR_UNBLOCK -> blockAdmin(selected);
            case MODIFY_RESPONSIBILITIES -> modifyResponsibility(selected);
            default -> {
            }
        }
    }

    private void modifyResponsibility(Admin selected) {
        Type[] options = Type.values();
        int count = 1;
        for (Type type : options) {
            if (selected.isAllowed(type)) {
                System.out.println(Color.WHITE + count + "-" + Color.GREEN + type.toString() + Color.RESET);
            } else {
                System.out.println(Color.WHITE + count + "-" + Color.RED + type.toString() + Color.RESET);
            }
            count++;
        }
        System.out.println(Color.WHITE + "please select the responsibility you want to change" + Color.RESET);
        String selection = InputManager.getSelection(8);
        changeResponsibility(selected, options[Integer.parseInt(selection) - 1]);
        System.out.println(Color.GREEN + "Selected responsibility has been successfully changed for the selected admin" + Color.RESET);
    }

    private void changeResponsibility(Admin selected, Type type) {
        if(selected.isAllowed(type)){
            selected.removeAllowed(type);
        } else {
            selected.addAllowed(type);
        }
    }

    private void blockAdmin(Admin selected) {
        if (selected.isBlocked()) {
            System.out.println(Color.RED + "this admin is currently blocked" + Color.RESET);
        } else {
            System.out.println(Color.GREEN + "this admin is currently not blocked" + Color.RESET);
        }
        System.out.println(Color.WHITE + "Please enter" + Color.BLUE + " 1 " + Color.WHITE + "to change block status or"
                + Color.BLUE + " 2 " + Color.WHITE + "to remain the same" + Color.RESET);
        String selection = InputManager.getSelection(2);
        if ("1".equals(selection)) {
            selected.setBlocked(!selected.isBlocked());
            System.out.println(Color.GREEN + "selected admin's status was changed successfully" + Color.RESET);
        }
    }
}
