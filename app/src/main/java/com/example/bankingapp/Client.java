package com.example.bankingapp;

public class Client {

    private int iban;
    private double balance;
    private int pin;

    public Client(int iban, int pin, double balance) {
        this.iban = iban;
        this.balance = balance;
        this.pin = pin;
    }

    public Client() {
    }

    public int getIban() {
        return iban;
    }

    public void setIban(int iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
