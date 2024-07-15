package ir.ac.kntu;


public class AuthenticationRequest {
    private User user;
    private boolean checked = false;
    private boolean approved = false;
    private String errorMassage;

    public AuthenticationRequest(User user) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isApproved() {
        return approved;
    }
}
