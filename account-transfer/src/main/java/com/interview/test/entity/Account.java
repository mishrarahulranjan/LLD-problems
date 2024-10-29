package com.interview.test.entity;

import java.util.Objects;

public class Account {

    private String accountNumber;

    private Money balance;

    public Account(String accountNumber){
        this.accountNumber = accountNumber;
        this.balance = new Money(0);
    }

    public Account(String accountNumber,Money value){
        this.accountNumber = accountNumber;
        this.balance = value;
    }

    public void credit(Money creditBalance){
        this.balance = new Money(balance.getValue()+ creditBalance.getValue());
    }

    public void debit(Money debitBalance){
        if(debitBalance.getValue() > balance.getValue()){
            throw new RuntimeException("debitBalance is greater than actual account balance");
        }
        this.balance = new Money(balance.getValue()-debitBalance.getValue());
    }

    public double getBalanceValue() {
        return balance.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
