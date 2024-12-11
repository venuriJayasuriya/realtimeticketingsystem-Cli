Customer customer = applicationContext.getBean(Customer.class);
            customer.initialize(ticketPool, config.getCustomerRetrievalRate(), i);