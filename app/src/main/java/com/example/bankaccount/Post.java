package com.example.bankaccount;

public class Post {
    private int id;
    private String accountName;
    private int amount;
    private String iban;
    private String currency;

    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public int getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }
}
