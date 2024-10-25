package com.interview.test.lb.strategy;

import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancingStrategy implements LoadBalancingStrategy {

    private AtomicInteger atomicCounter = new AtomicInteger(0);

    @Override
    public int getBackEndServerIndex(int backEndServerCount) {
        int currentIndex = atomicCounter.incrementAndGet();
        return (currentIndex)%backEndServerCount;
    }
}
