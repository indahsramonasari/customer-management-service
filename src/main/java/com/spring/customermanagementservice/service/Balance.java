package com.spring.customermanagementservice.service;

public class Balance implements Customer {

    private int amountBalance;
    private int amount;

    public Balance (int balance, int amount) {
        this.amountBalance = balance;
        this.amount = amount;
    }

    @Override
    public int cashWithdrawal() {
        return amountBalance - amount;
    }

    @Override
    public int cashDeposit() {
        return amountBalance + amount;
    }
}
