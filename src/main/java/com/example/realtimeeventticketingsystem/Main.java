package com.example.realtimeeventticketingsystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Realtime Event Ticketing System!");
        prompts();
    }

    public static void prompts() {
        Scanner scanner = new Scanner(System.in);
        systemCLI ticketInfo = new systemCLI();
        System.out.println("Please enter the total number of tickets for the event:");
        ticketInfo.setTotalTickets(scanner.nextInt());
        System.out.println("Please enter the rate of ticket release for the event:");
        ticketInfo.setTicketReleaseRate(scanner.nextDouble());
        System.out.println("Please enter the customer retrieval rate for the event:");
        ticketInfo.setCustomerRetrievalRate(scanner.nextDouble());
        System.out.println("Please enter the max number of ticket capacity for the event:");
        ticketInfo.setMaxTicketCapacity(scanner.nextInt());
        System.out.println(ticketInfo);
    }
}
