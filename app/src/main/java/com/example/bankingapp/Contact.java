package com.example.bankingapp;

public class Contact {
    private Client clientInfo;
    private String name;

    public Contact(Client clientInfo, String name) {
        this.clientInfo = clientInfo;
        this.name = name;
    }

    public Client getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Client clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
