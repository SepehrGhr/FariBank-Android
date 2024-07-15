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

    public static boolean checkPasswordValidity(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=?])[^\\s]{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }

    private static boolean checkSecurityNumberValidity(String securityNumber) {
        String ssnRegex = "\\d{10}";
        Pattern ssnPattern = Pattern.compile(ssnRegex);
        Matcher ssnMatcher = ssnPattern.matcher(securityNumber);
        return ssnMatcher.matches();
    }

    public static boolean checkPhoneNumberValidity(String phoneNumber) {
        String numberRegex = "\\d{11}";
        Pattern numberPattern = Pattern.compile(numberRegex);
        Matcher numberMatcher = numberPattern.matcher(phoneNumber);
        return numberMatcher.matches() && "09".equals(phoneNumber.substring(0, 2));
    }
}
