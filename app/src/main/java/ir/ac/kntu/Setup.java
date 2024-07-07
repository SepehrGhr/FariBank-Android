package ir.ac.kntu;

public class Setup {
    public static void userSetup() {
        User test = new User("Sepehr", "Ghardashi", new PhoneNumber("09111262338", 0), "5820175281", "Sepehr1384@");
        User test2 = new User("Sina", "Najafi", new PhoneNumber("09121103946", 0), "0250388109", "IloveMoney$420");
        User test3 = new User("Hedie", "Tahmouresi", new PhoneNumber("09109056296", 0), "0124523423", "H@Tah1384");
        User test6 = new User("Amir", "Akbari", new PhoneNumber("09110408447", 0), "5820148237", "Saj@1234");
        test.setAuthenticated(true);
        test.addReceipt(new ChargeReceipt(10000, 10000));
        test.addReceipt(new TransferReceipt(5000, test, test2, Method.CONTACT));
        test2.setAuthenticated(true);
        test3.setAuthenticated(true);
        test6.setAuthenticated(true);
        test.setAccount();
        test2.setAccount();
        test3.setAccount();
        test6.setAccount();
        test6.getAccount().setAccountID("023123123000");
        test6.getAccount().getCreditCard().setCardNumber("5892101523537385");
        test.addNewContact(new Contact(test3, "Hedieeee", "Tah", "09109056296"));
        test.addNewContact(new Contact(test2, "Sina", " ", "09121103946"));
        test3.addNewContact(new Contact(test, "Sep", "khals", "09111262338"));
        Main.getUsers().addUser(test);
        Main.getUsers().addUser(test2);
        Main.getUsers().addUser(test3);
        Main.getUsers().addForeignUser(test6);
    }
}
