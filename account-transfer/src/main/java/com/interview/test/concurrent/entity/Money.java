package com.interview.test.concurrent.entity;

public class Money {

    double value;

    public Money(double value){
        this.value = value;
    }

    protected double getValue() {
        return value;
    }
}
