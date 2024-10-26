package com.interview.test.lb.strategy;

import com.interview.test.server.BackendServer;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancingStrategy implements LoadBalancingStrategy {

    private Random random = new Random();

    @Override
    public BackendServer getBackEndServer(List<BackendServer> serverList) {
        int serverIndex = random.nextInt(serverList.size());
        return serverList.get(serverIndex);
    }
}
