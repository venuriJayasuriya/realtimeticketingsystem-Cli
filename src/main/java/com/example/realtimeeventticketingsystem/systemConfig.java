package com.example.realtimeeventticketingsystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class systemConfig {
    private int totalTickets;
    private double ticketReleaseRate;
    private double customerRetrievalRate;
    private int maxTicketCapacity;

    public systemConfig() {
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

    @Override
    public String toString() {
        return "Ticket information {" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }

    public void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("ticket_info.json")) {
            gson.toJson(this, writer);  // Serialize the object and save to a file
            System.out.println("Ticketing information saved to ticket_info.json");
        } catch (IOException e) {
            System.out.println("Error saving ticketing information to JSON file: " + e.getMessage());
        }
    }

    public systemConfig loadFromJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader("ticket_info.json")) {
            return gson.fromJson(reader, systemConfig.class);  // Deserialize the JSON back to an object
        } catch (IOException e) {
            System.out.println("Error loading ticketing information from JSON file: " + e.getMessage());
            return null;
        }
    }
}
