package ir.ac.kntu;

enum Status {SUBMITTED, PENDING, CLOSED}

enum Type {REPORT, CONTACTS, TRANSFER, SETTINGS, AUTHENTICATION, FUNDS, CHARGE, CARD}

public class Ticket {
    private String userMessage;
    private String adminMessage;
    private Status status;
    private Type type;
    private User submitter;

    public Ticket(String userMessage, Type type, User submitter) {
        this.userMessage = userMessage;
        this.type = type;
        this.status = Status.PENDING;
        this.submitter = submitter;
        this.adminMessage = "";
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Type getType() {
        return type;
    }

    public static void submitNewTicket() {
        printTicketTypes();
        String selection = InputManager.getSelection(8);
        switch (selection) {
            case "1" -> createTicket(Type.REPORT);
            case "2" -> createTicket(Type.CONTACTS);
            case "3" -> createTicket(Type.TRANSFER);
            case "4" -> createTicket(Type.SETTINGS);
            case "5" -> createTicket(Type.FUNDS);
            case "6" -> createTicket(Type.CHARGE);
            case "7" -> createTicket(Type.CARD);
            default -> {}
        }

    }

    private static void createTicket(Type type) {
        String message = getTicketMessage();
        Ticket newTicket = new Ticket(message, type, Main.getUsers().getCurrentUser());
        addTicketToDatabase(newTicket);
    }

    private static void addTicketToDatabase(Ticket newTicket) {
        Main.getUsers().getCurrentUser().addNewTicket(newTicket);
        Main.getAdminData().addNewTicket(newTicket);
        System.out.println(Color.GREEN + "Your Ticket has been submitted successfully ! our admins will help you soon" + Color.RESET);
    }

    private static String getTicketMessage() {
        System.out.println(Color.WHITE + "Please write your problem for our admins" + Color.RESET);
        return InputManager.getInput();
    }

    private static void printTicketTypes() {
        System.out.println(Color.WHITE + "1-" + Color.BLUE + "Report" + Color.RESET);
        System.out.println(Color.WHITE + "2-" + Color.BLUE + "Contacts" + Color.RESET);
        System.out.println(Color.WHITE + "3-" + Color.BLUE + "Transfer" + Color.RESET);
        System.out.println(Color.WHITE + "4-" + Color.BLUE + "Settings" + Color.RESET);
        System.out.println(Color.WHITE + "5-" + Color.BLUE + "Funds" + Color.RESET);
        System.out.println(Color.WHITE + "6-" + Color.BLUE + "Charge" + Color.RESET);
        System.out.println(Color.WHITE + "7-" + Color.BLUE + "Card" + Color.RESET);
        System.out.println(Color.WHITE + "8-" + Color.BLUE + "Return" + Color.RESET);
        System.out.println(Color.WHITE + "Please enter the number of the option you want to select" + Color.RESET);
    }

    @Override
    public String toString() {
        if (type.equals(Type.AUTHENTICATION)) {
            Main.getAdminData().getRequests().get(submitter).showUserInformation();
        } else {
            return Color.CYAN + "*".repeat(35) + '\n' + Color.WHITE + "Type : " +
                    Color.BLUE + type.toString() + '\n' + Color.WHITE + "Status : " +
                    Color.BLUE + status.toString() + '\n' + Color.WHITE + "User message : " +
                    Color.BLUE + userMessage + '\n' + Color.WHITE + "Admin message : " + Color.BLUE +
                    adminMessage + '\n' + Color.CYAN + "*".repeat(35) + Color.RESET;
        }
        return "";
    }
}

