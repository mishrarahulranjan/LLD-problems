package com.interview.test.lb.strategy;

import com.interview.test.server.BackendServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class WeightedLoadBalancingStrategy implements LoadBalancingStrategy{

    @Override
    public BackendServer getBackEndServer(List<BackendServer> serverList) {
        List<BackendServer> serverBackEndList = new ArrayList<>();

        serverList.stream().forEach(server->{
            IntStream.range(0, server.getWeight()).forEach(i->{
                serverBackEndList.add(server);
            });
        });
        int index = new Random().nextInt(serverBackEndList.size());
        return serverBackEndList.get(index);
    }
}
