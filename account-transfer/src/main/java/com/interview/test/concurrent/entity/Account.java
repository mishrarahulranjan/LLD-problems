package com.interview.test.concurrent.entity;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private String accountNumber;

    private Money balance;

    private final ReentrantLock lock = new ReentrantLock();

    public Account(String accountNumber){
        this.accountNumber = accountNumber;
        this.balance = new Money(0);
    }

    public Account(String accountNumber, Money value){
        this.accountNumber = accountNumber;
        this.balance = value;
    }

    public void credit(Money creditBalance){
        lock.lock();
        try{
            this.balance = new Money(balance.getValue()+ creditBalance.getValue());
        }finally {
            lock.unlock();
        }

    }

    public void debit(Money debitBalance){
        lock.lock();
        try{
            if(debitBalance.getValue() > balance.getValue()){
                throw new RuntimeException("debitBalance is greater than actual account balance");
            }
            this.balance = new Money(balance.getValue()-debitBalance.getValue());
        }finally {
            lock.unlock();
        }

    }

    public double getBalanceValue() {
        return balance.getValue();
    }

    public ReentrantLock getLock() {
        return lock;
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
