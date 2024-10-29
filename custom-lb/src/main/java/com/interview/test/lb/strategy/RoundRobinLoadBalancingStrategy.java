package com.interview.test.lb.strategy;

import com.interview.test.server.BackendServer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancingStrategy implements LoadBalancingStrategy {

    private AtomicInteger atomicCounter = new AtomicInteger(0);

    @Override
    public BackendServer getBackEndServer(List<BackendServer> serverList) {
        int currentIndex = atomicCounter.incrementAndGet();
        return serverList.get((currentIndex)%serverList.size());
    }
}
