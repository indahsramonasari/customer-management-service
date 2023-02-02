package com.spring.customermanagementservice.service;

public class Balance extends Customer {

    private int balance;
    private int amount;

    public Balance (int balance, int amount) {
        this.balance = balance;
        this.amount = amount;
    }

    @Override
    public int cashWithdrawal() {
        return balance - amount;
    }

    @Override
    public int cashDeposit() {
        return balance + amount;
    }
}
