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

    public static String getSelection(int options) {
        String selection = getInput();
        while (!isInputValid(selection, options)) {
            System.out.println(Color.RED + "Please enter a number between 1 and " + options + Color.RESET);
            selection = getInput();
        }
        return selection;
    }










}
