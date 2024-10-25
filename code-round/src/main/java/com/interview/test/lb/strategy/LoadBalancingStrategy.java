package com.interview.test.lb.strategy;

public interface LoadBalancingStrategy {

    int getBackEndServerIndex(int backEndServerCount);
}
