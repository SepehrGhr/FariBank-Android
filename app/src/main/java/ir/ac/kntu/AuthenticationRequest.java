package ir.ac.kntu;


public class AuthenticationRequest {
    private User user;
    private boolean checked = false;
    private boolean approved = false;
    private String errorMassage;

    public AuthenticationRequest(User user) {
        this.user = user;
    }

    public void editInformation() {
        String selection = InputManager.getInput();
        while (!InputManager.isInputValid(selection, 2)) {
            System.out.println(Color.RED + "Please enter a number between 1 and 2" + Color.RESET);
            selection = InputManager.getInput();
        }
        switch (selection) {
            case "1" -> {
                Main.getAdminData().getRequests().get(Main.getUsers().getCurrentUser()).getNewInformation();
            }
            case "2" -> Menu.userLogout();
            default -> {
            }
        }
    }

    public void getNewInformation() {
        Main.getAdminData().removeRequest(Main.getUsers().getCurrentUser());
        Main.getUsers().removeUser(Main.getUsers().getCurrentUser());
        System.out.println(Color.WHITE + "Please enter your name" + Color.RESET);
        Main.getUsers().getCurrentUser().setName(InputManager.setUserName());
        System.out.println(Color.WHITE + "Please enter your last name" + Color.RESET);
        Main.getUsers().getCurrentUser().setLastName(InputManager.setUserName());
        System.out.println(Color.WHITE + "Please enter your phone number" + Color.RESET);
        String phoneNumber = Menu.setPhoneNumber();
        while ("".equals(phoneNumber)) {
            System.out.println(Color.WHITE + "Please enter another phone number" + Color.RESET);
            phoneNumber = Menu.setPhoneNumber();
        }
        Main.getUsers().getCurrentUser().setPhoneNumber(new PhoneNumber(phoneNumber, 0));
        System.out.println(Color.YELLOW + "Please enter your security number" + Color.RESET);
        String securityNumber = Menu.setSecurityNumber();
        while ("".equals(securityNumber)) {
            System.out.println(Color.WHITE + "Please enter your correct security number" + Color.RESET);
            securityNumber = Menu.setSecurityNumber();
        }
        Main.getUsers().getCurrentUser().setSecurityNumber(Menu.setSecurityNumber());
        System.out.println(Color.YELLOW + "Please enter your password" + Color.WHITE + " (it must contain at least " +
                "one lowercase,uppercase,number and character)" + Color.RESET);
        Main.getUsers().getCurrentUser().setPassword(Menu.setPassword());
        System.out.println(Color.GREEN + "Your information has been successfully registered and will be checked soon" + Color.RESET);
        addNewAuthenticationRequest();
        Main.getUsers().addUser(Main.getUsers().getCurrentUser());
    }

    private void addNewAuthenticationRequest() {
        AuthenticationRequest newRequest = new AuthenticationRequest(Main.getUsers().getCurrentUser());
        Ticket newTicket = new Ticket("", Type.AUTHENTICATION, Main.getUsers().getCurrentUser());
        Main.getAdminData().addNewTicket(newTicket);
        Main.getAdminData().addAuthenticationRequest(newRequest);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setErrorMassage(String errorMassage) {
        this.errorMassage = errorMassage;
    }

    public void chooseAcceptOrReject(Ticket selected) {
        System.out.println(Color.WHITE + "enter 1 to accept and 2 to reject" + Color.RESET);
        String selection = InputManager.getSelection(2);
        if ("1".equals(selection)) {
            this.setupAccepted();
            System.out.println(Color.GREEN + "Selected request has been successfully accepted" + Color.RESET);
        } else if ("2".equals(selection)) {
            this.setChecked(true);
            String massage = setRejectReason();
            selected.setAdminMessage(massage);
            this.setErrorMassage(massage);
            System.out.println(Color.GREEN + "Selected request has been successfully rejected" + Color.RESET);
        }
        selected.setStatus(Status.CLOSED);
    }

    public void setupAccepted() {
        this.setApproved(true);
        this.setChecked(true);
        this.getUser().setAccount();
        this.getUser().setAuthenticated(true);
    }

    private static String setRejectReason() {
        System.out.println(Color.WHITE + "Please enter the reason you rejected this request" + Color.RESET);
        return InputManager.getInput();
    }

    public User getUser() {
        return user;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void showErrorMassage() {
        System.out.println(Color.RED + "Your authentication request has been declined by our admins." + Color.RESET);
        System.out.println(Color.WHITE + "Admin's message: " + Color.RED + errorMassage + Color.RESET);
        System.out.println(Color.WHITE + "Enter " + Color.BLUE + "1" + Color.WHITE + " to edit your information or enter " +
                Color.BLUE + "2 " + Color.WHITE + "to log out" + Color.RESET);
    }

    public void showUserInformation() {
        System.out.println(Color.CYAN + "*".repeat(35));
        System.out.println(Color.WHITE + "name : " + Color.BLUE + user.getName() + '\n' +
                Color.WHITE + "last name : " + Color.BLUE + user.getLastName() +
                '\n' + Color.WHITE + "security number : " + Color.BLUE + user.getSecurityNumber() +
                '\n' + Color.WHITE + "phone number : " + Color.BLUE + user.getPhoneNumber() + '\n' +
                Color.WHITE + "password : " + Color.BLUE + user.getPassword() + '\n' + Color.CYAN +
                "*".repeat(35) + Color.RESET);
    }

}
