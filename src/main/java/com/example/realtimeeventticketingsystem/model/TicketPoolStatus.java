package com.example.realtimeeventticketingsystem.model;

public class TicketPoolStatus {

    private final int currentTicketCount;
    private final int maxTicketCapacity;
    private final boolean soldOut;
    private final int availableTickets;
    private final int totalSold;

    public TicketPoolStatus(int currentTicketCount, int maxTicketCapacity, boolean soldOut, int availableTickets, int totalSold) {
        this.currentTicketCount = currentTicketCount;
        this.maxTicketCapacity = maxTicketCapacity;
        this.soldOut = soldOut;
        this.availableTickets = availableTickets;
        this.totalSold = totalSold;
    }

    public int getCurrentTicketCount() {
        return currentTicketCount;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public boolean isSoldOut() {
        return soldOut;
    }
    public int getAvailableTickets() { return availableTickets; }
    public int getTotalSold() { return totalSold; }
}
