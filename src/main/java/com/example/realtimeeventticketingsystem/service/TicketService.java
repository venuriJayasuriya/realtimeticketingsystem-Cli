package com.example.realtimeeventticketingsystem.service;

import com.example.realtimeeventticketingsystem.model.TicketPool;
import com.example.realtimeeventticketingsystem.model.TicketPoolStatus;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketPool ticketPool;

    public TicketService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public TicketPoolStatus getStatus() {
        return new TicketPoolStatus(
                ticketPool.getCurrentTicketCount(),
                ticketPool.getMaxTicketCapacity(),
                ticketPool.isSoldOut(),
                ticketPool.getCurrentTicketCount(),
                ticketPool.getMaxTicketCapacity() - ticketPool.getCurrentTicketCount()
        );
    }
}