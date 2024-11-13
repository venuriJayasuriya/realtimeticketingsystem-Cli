package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class systemCLI {
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

    public systemCLI() {

    }

    public static void prompts() {
        Scanner scanner = new Scanner(System.in);
        systemCLI ticketInfo = new systemCLI();

        System.out.println("Please enter the total number of tickets for the event:");
        ticketInfo.setTotalTickets(intValidator(scanner));
        System.out.println("Please enter the rate of ticket release for the event:");
        ticketInfo.setTicketReleaseRate(doubleValidator(scanner));
        System.out.println("Please enter the customer retrieval rate for the event:");
        ticketInfo.setCustomerRetrievalRate(doubleValidator(scanner));
        System.out.println("Please enter the max number of ticket capacity for the event:");
        ticketInfo.setMaxTicketCapacity(intValidator(scanner));

        System.out.println(ticketInfo);
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public void setTicketReleaseRate(double ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }
    public void setCustomerRetrievalRate(double customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }
    public double getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public double getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
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

    @Override
    public String toString() {
        return "Ticket information {" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
