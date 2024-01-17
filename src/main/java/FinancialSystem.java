import Structures.Table.Currency;
import Structures.Table.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FinancialSystem {
    public static void startSystem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Financial System!");
        Boolean isCommandSelected = false;
        String command = "";
        // Loop for user interaction
        while (!isCommandSelected) {
            // Display available commands
            System.out.println("\nAvailable Commands:");
            System.out.println("1. generate sessions calculations");
            System.out.println("2. generate static measurements");
            System.out.println("3. generate value distribution");

            // Get user command
            System.out.print("Enter a command: ");
            command = scanner.nextLine();

            // Check the user's command
            switch (command.toLowerCase()) {
                case "generate sessions calculations":
                case "generate static measurements":
                case "generate value distribution":
                    isCommandSelected = true;
                    break;
                default:
                    System.out.println("Invalid command. Please enter a valid command.");
            }
        }
        handleCommand(command, scanner);
        System.out.println(FinancialSystemNBPAPI.connection("tables/B"));
    }

    // Method to handle specific commands
    private static void handleCommand(String command, Scanner scanner) {
        System.out.println("You selected: " + command);
        List<Table> currencies = getCurencies();

        // Get time option
        String timeOption;
        while (true) {
            if (command.equalsIgnoreCase("generate value distribution")) {
                System.out.println("\nSelect time option:");
                System.out.println("1. month");
                System.out.println("2. quarter");
                System.out.print("Enter the time option: ");
                String timeOptionChoice = scanner.nextLine().toLowerCase();

                // Validate and set time option
                if (isValidValueDistributionTimeOption(timeOptionChoice)) {
                    timeOption = timeOptionChoice;
                    break;
                } else {
                    System.out.println("Invalid time option. Please enter a valid time option.");
                }
            } else {
                // For other commands, allow all time options
                System.out.println("\nSelect time option:");
                System.out.println("1. one week");
                System.out.println("2. two weeks");
                System.out.println("3. month");
                System.out.println("4. quarter");
                System.out.print("Enter the time option: ");
                timeOption = scanner.nextLine().toLowerCase();

                // Validate time option
                if (isValidTimeOption(timeOption)) {
                    break;
                } else {
                    System.out.println("Invalid time option. Please enter a valid time option.");
                }
            }
        }

        // Get currency code
        String currencyCode1;
        while (true) {
            System.out.print("Enter the currency code (ISO 4217): ");
            currencyCode1 = scanner.nextLine();

            if (isValidCurrencyCode(currencyCode1, currencies)) {
                break; // Break the loop if validation passes
            } else {
                System.out.println("Invalid currency code 1. Please enter a valid currency code.");
            }
        }

        // For Shift Distribution, get the second currency code
        String currencyCode2 = "";
        if (command.equalsIgnoreCase("generate value distribution")) {
            while (true) {
                System.out.print("Enter the second currency code (ISO 4217): ");
                currencyCode2 = scanner.nextLine();

                if (isValidCurrencyCode(currencyCode2, currencies)) {
                    break; // Break the loop if validation passes
                } else {
                    System.out.println("Invalid currency code 2. Please enter a valid currency code.");
                }
            }
        }

        // Display selected options
        System.out.println("\nSelected Options:");
        System.out.println("Command: " + command);
        System.out.println("Time Option: " + timeOption);
        System.out.println("Currency Code 1: " + currencyCode1);
        System.out.println("Currency Code 2: " + currencyCode2);

        switch (command) {
            case "generate sessions calculations":
                FinancialSystemHelper.generateSessionsCalculations(timeOption, currencyCode1);
                break;
            case "generate static measurements":
                FinancialSystemHelper.generateStaticMeasurements(timeOption, currencyCode1);
                break;
            case "generate value distribution":
                FinancialSystemHelper.generateValueDistribution(timeOption, currencyCode1, currencyCode2);
                break;
        }
    }

    private static List<Table> getCurencies() {
        Gson gson = new Gson();
        String currenciesA = FinancialSystemNBPAPI.connection("tables/A");
        String currenciesB = FinancialSystemNBPAPI.connection("tables/B");
        String currenciesC = FinancialSystemNBPAPI.connection("tables/C");
        TypeToken<List<Table>> myToken = new TypeToken<>(){};
        List<Table> myInfoA = gson.fromJson(currenciesA, myToken.getType());
        List<Table> myInfoB = gson.fromJson(currenciesB, myToken.getType());
        List<Table> myInfoC = gson.fromJson(currenciesC, myToken.getType());
        List<Table> tables = new ArrayList<>();
        tables.addAll(myInfoA);
        tables.addAll(myInfoB);
        tables.addAll(myInfoC);
        return tables;
    }

    // Method to validate time option
    private static boolean isValidTimeOption(String timeOption) {
        return timeOption.equals("one week") ||
                timeOption.equals("two weeks") ||
                timeOption.equals("month") ||
                timeOption.equals("quarter");
    }

    // Method to validate time option for value distribution
    private static boolean isValidValueDistributionTimeOption(String timeOption) {
        return timeOption.equals("month") || timeOption.equals("quarter");
    }

    private static boolean isValidCurrencyCode(String currencyCode, List<Table> currencies) {
        for (Table currencyTable : currencies) {
            for (Currency currency : currencyTable.getRates()) {
                if (currency.getCode().equalsIgnoreCase(currencyCode)) {
                    return true; // Valid currency code found
                }
            }
        }
        return false; // Currency code not found in the list
    }
}
