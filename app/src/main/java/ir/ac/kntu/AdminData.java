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
}

