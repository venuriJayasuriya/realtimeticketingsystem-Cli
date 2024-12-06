package com.example.realtimeeventticketingsystem;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Real-time Event Ticketing System!");
        SystemCLI Cli = new SystemCLI();
        Cli.prompts();//called the prompt method to gather information on tickets save and load to JSON
        Cli.saveAndLoadToJson();//Save and load ticket information to JSON
    }
}
