package ir.ac.kntu;

import java.util.List;

public class Display {
    public static <T> T pageShow(List<T> items, ItemPrinter<T> itemPrinter) {
        int pageIndex = 0;
        while (true) {
            int startIndex = pageIndex * 5;
            int endIndex = Math.min(startIndex + 5, items.size());
            printItems(startIndex, endIndex, items, itemPrinter);
            int userChoice = fetchValidChoice(startIndex + 1, endIndex + 1);
            if (userChoice == endIndex + 1) {
                if (endIndex < items.size()) {
                    pageIndex++;
                } else {
                    System.out.println(Color.RED + "Your already on last page" + Color.RESET);
                }
            } else if (userChoice == endIndex + 2) {
                if (pageIndex > 0) {
                    pageIndex--;
                } else {
                    System.out.println(Color.RED + "Your on first page" + Color.RESET);
                }
            } else if (userChoice == endIndex + 3) {
                return null;
            } else {
                return items.get(userChoice - 1);
            }
        }
    }

    private static <T> void printItems(int startIndex, int endIndex, List<T> items, ItemPrinter<T> itemPrinter) {
        System.out.println(Color.YELLOW + "<>".repeat(20));
        for (int i = startIndex; i < endIndex; i++) {
            T item = items.get(i);
            System.out.print(Color.WHITE + (i+1) + "-");
            itemPrinter.printItem(item);
        }
        System.out.println(
                Color.WHITE + (endIndex+1) +  "-" + Color.CYAN + "Next " +
                        Color.WHITE + (endIndex+2) + "-" + Color.CYAN + "Previous " +
                        Color.WHITE + (endIndex+3) + "-" + Color.CYAN + "Return\n" +
                        Color.YELLOW + "<>".repeat(20) + '\n' +
                        Color.WHITE + "Please select an option" + Color.RESET);
    }

    private static int fetchValidChoice(int minValid, int maxValid) {
        while (true) {
            String input = InputManager.getInput();
            if (isChoiceValid(input, minValid, maxValid)) {
                return Integer.parseInt(input);
            }
            System.out.println(Color.RED + "Please select a valid option" + Color.RESET);
        }
    }

    private static boolean isChoiceValid(String input, int minVal, int maxVal) {
        try {
            int choice = Integer.parseInt(input);
            return (choice == maxVal || choice == maxVal+1 || choice == maxVal+2) ||
                    (choice >= minVal && choice <= maxVal);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int distance(String str1, String str2) {
        int length1 = str1.length();
        int length2 = str2.length();
        int[][] distances = new int[length1 + 1][length2 + 1];
        for (int i = 0; i <= length1; i++) {
            for (int j = 0; j <= length2; j++) {
                if (i == 0) {
                    distances[i][j] = j;
                } else if (j == 0) {
                    distances[i][j] = i;
                } else {
                    int insertion = distances[i][j - 1] + 1;
                    int deletion = distances[i - 1][j] + 1;
                    int substitution = distances[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1);
                    distances[i][j] = Math.min(insertion, Math.min(deletion, substitution));
                }
            }
        }
        return distances[length1][length2];
    }
}
