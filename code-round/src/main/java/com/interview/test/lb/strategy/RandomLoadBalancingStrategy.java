package com.interview.test.lb.strategy;

import java.util.Random;

public class RandomLoadBalancingStrategy implements LoadBalancingStrategy {

    private Random random = new Random();

    @Override
    public int getBackEndServerIndex(int backEndServerCount) {
        int serverIndex = random.nextInt(backEndServerCount);
        return serverIndex;
    }
}
