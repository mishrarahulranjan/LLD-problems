package com.interview.test;

import com.interview.test.constant.Constant;
import com.interview.test.lb.ApplicationLoadBalancer;
import com.interview.test.lb.strategy.LoadBalancingStrategy;
import com.interview.test.lb.strategy.RandomLoadBalancingStrategy;
import com.interview.test.lb.strategy.RoundRobinLoadBalancingStrategy;
import com.interview.test.lb.strategy.WeightedLoadBalancingStrategy;
import com.interview.test.server.BackendServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationLoadBalancerTest {

    private ApplicationLoadBalancer applicationLoadBalancer;

    private LoadBalancingStrategy loadBalancingStrategy;

    @BeforeEach
    void setUp(){
        loadBalancingStrategy = new RandomLoadBalancingStrategy();
        applicationLoadBalancer =  new ApplicationLoadBalancer(10,loadBalancingStrategy);
    }


    @DisplayName("sample registering test for backend Server")
    @Test
    void registerBackendServerTest(){
        boolean status = applicationLoadBalancer.register(new BackendServer("host-1"));
        Assertions.assertTrue(status);
    }

    @DisplayName("sample registering test for backend Server with max capacity")
    @Test
    void registerBackendServerWithMaximumCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(2,loadBalancingStrategy);
        Assertions.assertTrue(applicationLoadBalancer.register(new BackendServer("host-1")));
        Assertions.assertTrue(applicationLoadBalancer.register(new BackendServer("host-2")));
    }

    @DisplayName("sample registering test for backend Server with duplicate host info")
    @Test
    void registerBackendServerWithDuplicateHostInfoCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(2,loadBalancingStrategy);
        applicationLoadBalancer.register(new BackendServer("host-1"));

       boolean status = Assertions.assertThrows(RuntimeException.class,()->
               applicationLoadBalancer.register(new BackendServer("host-1"))

        ).getMessage().equals(Constant.Message.LB_HOST_DUPLICATE_MSG);

       Assertions.assertTrue(status);

    }

    @DisplayName("sample registering test for backend Server while adding host info more than capacity")
    @Test
    void registerBackendServerWithMoreThanCapacityInfoCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(1,loadBalancingStrategy);
        applicationLoadBalancer.register(new BackendServer("host-1"));

        boolean status = Assertions.assertThrows(RuntimeException.class,()->
                applicationLoadBalancer.register(new BackendServer("host-2"))
         ).getMessage().equals(Constant.Message.LB_MAX_CAPACITY_MSG);

        Assertions.assertTrue(status);
    }

    @DisplayName("Get backend instance from lodbalancer with random loadbalancing strategy")
    @Test
    void getInstanceWithRandomLoadBalancingStrategyTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(4,loadBalancingStrategy);
        applicationLoadBalancer.register(new BackendServer("host-1"));
        applicationLoadBalancer.register(new BackendServer("host-2"));
        applicationLoadBalancer.register(new BackendServer("host-3"));
        applicationLoadBalancer.register(new BackendServer("host-4"));

        Set<String> backEndServerSet = new HashSet<>();
        Set<Boolean> backEndServerUnique = new HashSet<>();
        for(int i=0;i<20;i++){
            System.out.println("instance Id: "+applicationLoadBalancer.getBackEndInstance());
            boolean status = backEndServerSet.add(applicationLoadBalancer.getBackEndInstance());
            backEndServerUnique.add(status);
        }

        Assertions.assertTrue(backEndServerUnique.size() <20);
    }

    @DisplayName("Get backend instance from lodbalancer with round robin loadbalancing strategy")
    @Test
    void getInstanceWithRoundRobinBalancingStrategyTest(){
        loadBalancingStrategy = new RoundRobinLoadBalancingStrategy();
        applicationLoadBalancer =  new ApplicationLoadBalancer(4,loadBalancingStrategy);
        applicationLoadBalancer.register(new BackendServer("host-1"));
        applicationLoadBalancer.register(new BackendServer("host-2"));
        applicationLoadBalancer.register(new BackendServer("host-3"));
        applicationLoadBalancer.register(new BackendServer("host-4"));

        Set<String> backEndServerSet = new HashSet<>();

        for(int i=0;i<4;i++){
            backEndServerSet.add(applicationLoadBalancer.getBackEndInstance());
        }

        Assertions.assertTrue(backEndServerSet.size() == 4);
    }

    @DisplayName("Get backend instance from lodbalancer without backend Server")
    @Test
    void getInstanceWithoutBackEndServerTest(){
        loadBalancingStrategy = new RoundRobinLoadBalancingStrategy();
        applicationLoadBalancer =  new ApplicationLoadBalancer(4,loadBalancingStrategy);

        boolean status = Assertions.assertThrows(RuntimeException.class, ()->
                applicationLoadBalancer.getBackEndInstance()).getMessage()
                .equals(Constant.Message.LB_NO_BACKEND_SERVER);
        Assertions.assertTrue(status);
    }

    @DisplayName("Get backend instance from lodbalancer with weighted loadbalancing strategy")
    @Test
    void getInstanceWithWeightedLoadBalancingStrategyTest(){
        loadBalancingStrategy = new WeightedLoadBalancingStrategy();
        applicationLoadBalancer =  new ApplicationLoadBalancer(4,loadBalancingStrategy);

        BackendServer server1 = new BackendServer("host-1",1);
        BackendServer server2 = new BackendServer("host-2",1);
        BackendServer server3 = new BackendServer("host-3",2);
        BackendServer server4 = new BackendServer("host-4",2);

        applicationLoadBalancer.register(server1);
        applicationLoadBalancer.register(server2);
        applicationLoadBalancer.register(server3);
        applicationLoadBalancer.register(server4);

        Map<String,Integer> backEndServerCountMap = new HashMap(4);

        String key = applicationLoadBalancer.getBackEndInstance();
        int noOfRequest = 12;
        for(int i=0;i<noOfRequest;i++){
           backEndServerCountMap.merge(applicationLoadBalancer.getBackEndInstance(),1, Integer::sum);
        }

        Integer valueCount = backEndServerCountMap.get(server3.toString());
        Assertions.assertTrue(valueCount!=null);
        int totalWeight =  server1.getWeight()+server2.getWeight()+server3.getWeight()+server4.getWeight();
        int expectedRequestOnServer3 = (server3.getWeight()/totalWeight)*noOfRequest;

        Assertions.assertTrue(valueCount.intValue() >= expectedRequestOnServer3);



        valueCount = backEndServerCountMap.get(server1.toString());
        Assertions.assertTrue(valueCount!=null);
        int expectedRequestOnServer1 = (server1.getWeight()/totalWeight)*noOfRequest;

        Assertions.assertTrue(valueCount.intValue() >= expectedRequestOnServer1);
    }
}
