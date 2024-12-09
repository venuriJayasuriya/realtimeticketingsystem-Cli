package com.example.realtimeeventticketingsystem.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SystemConfig {
    private static final Logger logger = LogManager.getLogger(SystemConfig.class);

    private int maxTicketCapacity; //the maximum number of tickets the event can hold
    private int totalTickets; //the number of tickets at the beginning
    private int vendorCount;
    private int ticketReleaseRate; //rate at which the vendors will release tickets
    private int customerRetrievalRate; //rate at which the customers will retrieve tickets

    public SystemConfig() {

    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public void setVendorCount(int vendorCount) {
        this.vendorCount = vendorCount;
    }
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
    public int getTotalTickets() {
        return totalTickets;
    }
    public int getVendorCount() {
        return vendorCount;
    }
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }


    @Override
    public String toString() {
        return  "-------------------------------------------------------------\nTicket information of the Event\n-------------------------------------------------------------\n"+
                "Maximum Ticket Capacity event can hold            = " + maxTicketCapacity +
                "\nTotal Tickets in the Ticket pool at the beginning = " + totalTickets +
                "\nNumber of Vendors selling tickets                 = " + vendorCount +
                "\nTicket Released Rate by Vendors(in seconds)       = " + ticketReleaseRate +
                "\nTicket Retrieval Rate by Customers(in seconds)    = " + customerRetrievalRate +
                "\n-------------------------------------------------------------\n";
    }

    public void saveToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("ticket_info.json")) {
            gson.toJson(this, writer);  // Serialize the object and save to a file
            logger.info("Ticketing information saved to ticket_info.json");
        } catch (IOException e) {
            logger.error("Error saving ticketing information to JSON file: {} " , e.getMessage());
        }
    }

    public SystemConfig loadFromJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileReader reader = new FileReader("ticket_info.json")) {
            return gson.fromJson(reader, SystemConfig.class);  // Deserialize the JSON back to an object
        } catch (IOException e) {
            logger.error("Error loading ticketing information from JSON file: {}" ,e.getMessage());
            return null;
        }
    }

    //method to save and load ticket information to JSON
    public void saveAndLoadToJson() {
        saveToJson();

        System.out.println();

        SystemConfig loadedTicketInfo = loadFromJson();
        if (loadedTicketInfo != null) {
            logger.info("Loaded ticketing information from JSON:");
            logger.info(loadedTicketInfo);
        }
    }

}
