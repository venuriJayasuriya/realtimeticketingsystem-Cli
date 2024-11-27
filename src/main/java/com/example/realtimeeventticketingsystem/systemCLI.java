package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class systemCLI {

    public static void prompts() {
        Scanner scanner = new Scanner(System.in);
        systemConfig ticketInfo = new systemConfig();

        System.out.println("Please enter the total number of tickets for the event:");
        ticketInfo.setTotalTickets(intValidator(scanner));
        System.out.println("Please enter the rate of ticket release for the event:");
        ticketInfo.setTicketReleaseRate(doubleValidator(scanner));
        System.out.println("Please enter the customer retrieval rate for the event:");
        ticketInfo.setCustomerRetrievalRate(doubleValidator(scanner));
        System.out.println("Please enter the max number of ticket capacity for the event:");
        ticketInfo.setMaxTicketCapacity(intValidator(scanner));

        System.out.println();
        System.out.println(ticketInfo);
    }

    private static int intValidator(Scanner scanner) {
        while(true){
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive integer");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }

    private static double doubleValidator(Scanner scanner) {
        while(true){
            if (scanner.hasNextDouble()) {
                double input = scanner.nextDouble();
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive integer");
                }
            }
            else {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }


}
