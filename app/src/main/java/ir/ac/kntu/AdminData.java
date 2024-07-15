package ir.ac.kntu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminData {
    private final Map<User, AuthenticationRequest> requests;
    private final List<Admin> admins;
    private final List<Ticket> tickets;
    private Admin currentAdmin;


    public AdminData() {
        requests = new HashMap<>();
        admins = new ArrayList<>();
        tickets = new ArrayList<>();
    }

    public void addAdmin(Admin newAdmin) {
        admins.add(newAdmin);
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public Admin findAdminByUsername(String input) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(input)) {
                return admin;
            }
        }
        return null;
    }

    public void removeRequest(User user) {
        requests.remove(user);
    }

    public Map<User, AuthenticationRequest> getRequests() {
        return requests;
    }

    public void addAuthenticationRequest(AuthenticationRequest newRequest) {
        requests.put(newRequest.getUser(), newRequest);
    }

    public void addNewTicket(Ticket newTicket) {
        tickets.add(newTicket);
        ticketScheduler(newTicket);
    }

    public void ticketScheduler(Ticket newTicket){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            newTicket.setWeWillContactMessage();
            scheduler.shutdown();
        };
        scheduler.schedule(task, 20, TimeUnit.SECONDS);
    }

    public void displayTicketsMenu() {
        System.out.println(Color.WHITE + "Please select the filter you want to use" + Color.RESET);
        System.out.println(Color.WHITE + "1-" + Color.BLUE + "Status" + Color.RESET);
        System.out.println(Color.WHITE + "2-" + Color.BLUE + "Type" + Color.RESET);
        System.out.println(Color.WHITE + "3-" + Color.BLUE + "User" + Color.RESET);
        System.out.println(Color.WHITE + "4-" + Color.BLUE + "Return" + Color.RESET);
        selectTicketFilter();
    }

    private void selectTicketFilter() {
        String selection = InputManager.getSelection(4);
        List<Ticket> allowedTickets = getCurrentAdminTickets();
        switch (selection) {
            case "1" -> showTicketsByStatus(allowedTickets);
            case "2" -> showTicketsByType(allowedTickets);
            case "3" -> showTicketsByUser(allowedTickets);
            default -> Menu.printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
        }
    }

    private List<Ticket> getCurrentAdminTickets() {
        List<Ticket> allowedTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (currentAdmin.isAllowed(ticket.getType())) {
                allowedTickets.add(ticket);
            }
        }
        return allowedTickets;
    }

    private void showTicketsByUser(List<Ticket> allowedTickets) {
        if (allowedTickets.size() == 0) {
            System.out.println(Color.RED + "There is no ticket currently" + Color.RESET);
            return;
        }
        Ticket selected = Display.pageShow(allowedTickets, ticket -> System.out.println(Color.BLUE + ticket.getSubmitter().getPhoneNumber() + Color.RESET));
        handleSelectedTicket(selected);
    }

    private void submitAdminMessage(Ticket selected) {
        System.out.println(Color.WHITE + "Please enter your message for them" + Color.RESET);
        String adminMessage = InputManager.getInput();
        selected.setAdminMessage(adminMessage);
        selected.setStatus(chooseNewStatus());
        System.out.println(Color.GREEN + "Your message has been submitted and ticket status has been updated" + Color.RESET);
    }

    private Status chooseNewStatus() {
        Menu.printMenu(Status.values(), Main.getAdminData()::getCurrentAdmin);
        String selection = InputManager.getSelection(3);
        Status[] options = Status.values();
        return options[Integer.parseInt(selection) - 1];
    }

    private void showTicketsByType(List<Ticket> allowedTickets) {
        if (allowedTickets.size() == 0) {
            System.out.println(Color.RED + "There is no ticket currently" + Color.RESET);
            return;
        }
        Ticket selected = Display.pageShow(allowedTickets, ticket -> System.out.println(Color.BLUE + ticket.getType().toString() + Color.RESET));
        handleSelectedTicket(selected);
    }

    private void handleSelectedTicket(Ticket selected) {
        if (selected == null) {
            Menu.printMenu(OptionEnums.AdminMenu.values(), InputManager::handleAdminInput);
            return;
        }
        System.out.println(selected);
        if (selected.getType().equals(Type.AUTHENTICATION)) {
            if(selected.getStatus().equals(Status.CLOSED)){
                System.out.println(Color.RED + "This request is already closed" + Color.RESET);
                return;
            }
            AuthenticationRequest selectedReq = requests.get(selected.getSubmitter());
            selectedReq.chooseAcceptOrReject(selected);
            return;
        }
        submitAdminMessage(selected);
    }

    private void showTicketsByStatus(List<Ticket> allowedTickets) {
        if (allowedTickets.size() == 0) {
            System.out.println(Color.RED + "There is no ticket currently" + Color.RESET);
            return;
        }
        Ticket selected = Display.pageShow(allowedTickets, ticket -> System.out.println(Color.BLUE + ticket.getStatus().toString() + Color.RESET));
        handleSelectedTicket(selected);
    }

    public void adminSetup() {
        addAdmin(new Admin("Sep", "Sepehr", "12345"));
        addAdmin(new Admin("Ali", "Ali12", "Ali123"));
    }

    public void addAllAdmins(List<Object> everyone) {
        everyone.addAll(admins);
    }

    public List<Admin> searchByName(String name) {
        List<Admin> matched = new ArrayList<>();
        for (Admin admin : admins) {
            if (name.equals(admin.getName())) {
                matched.add(admin);
            }
        }
        return matched;
    }

    public void addAdminsToList(List<Object> matched) {
        matched.addAll(admins);
    }

    public boolean nameAlreadyExists(String name) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }
}

