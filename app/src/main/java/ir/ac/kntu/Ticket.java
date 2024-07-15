package ir.ac.kntu;

import java.io.Serializable;

enum Status {SUBMITTED, PENDING, CLOSED}

enum Type {REPORT, CONTACTS, TRANSFER, SETTINGS, AUTHENTICATION, FUNDS, CHARGE, CARD}

public class Ticket implements Serializable {
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

    private static void addTicketToDatabase(Ticket newTicket) {
        Main.getUsers().getCurrentUser().addNewTicket(newTicket);
        Main.getAdminData().addNewTicket(newTicket);
    }

    public void setWeWillContactMessage(){
        adminMessage = "Our colleagues will contact you soon";
        status = Status.CLOSED;
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

