package com.interview.test.lb.strategy;

import com.interview.test.server.BackendServer;

import java.util.List;

public interface LoadBalancingStrategy {

    BackendServer getBackEndServer(List<BackendServer> serverList);
}
