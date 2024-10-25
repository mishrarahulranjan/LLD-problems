package com.interview.test;

import com.interview.test.contant.Constant;
import com.interview.test.lb.ApplicationLoadBalancer;
import com.interview.test.lb.strategy.LoadBalancingStrategy;
import com.interview.test.lb.strategy.RandomLoadBalancingStrategy;
import com.interview.test.lb.strategy.RoundRobinLoadBalancingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
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
        boolean status = applicationLoadBalancer.register("host-1");
        Assertions.assertTrue(status);
    }

    @DisplayName("sample registering test for backend Server with max capacity")
    @Test
    void registerBackendServerWithMaximumCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(2,loadBalancingStrategy);
        Assertions.assertTrue(applicationLoadBalancer.register("host-1"));
        Assertions.assertTrue(applicationLoadBalancer.register("host-2"));
    }

    @DisplayName("sample registering test for backend Server with duplicate host info")
    @Test
    void registerBackendServerWithDuplicateHostInfoCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(2,loadBalancingStrategy);
        applicationLoadBalancer.register("host-1");

       boolean status = Assertions.assertThrows(RuntimeException.class,()->
               applicationLoadBalancer.register("host-1")

        ).getMessage().equals(Constant.Message.LB_HOST_DUPLICATE_MSG);

       Assertions.assertTrue(status);

    }

    @DisplayName("sample registering test for backend Server while adding host info more than capacity")
    @Test
    void registerBackendServerWithMoreThanCapacityInfoCapacityTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(1,loadBalancingStrategy);
        applicationLoadBalancer.register("host-1");

        boolean status = Assertions.assertThrows(RuntimeException.class,()->
                applicationLoadBalancer.register("host-2")
         ).getMessage().equals(Constant.Message.LB_MAX_CAPACITY_MSG);

        Assertions.assertTrue(status);
    }

    @DisplayName("Get backend instance from lodbalancer with random loadbalancing strategy")
    @Test
    void getInstanceWithRandomLoadBalancingStrategyTest(){
        applicationLoadBalancer =  new ApplicationLoadBalancer(4,loadBalancingStrategy);
        applicationLoadBalancer.register("host-1");
        applicationLoadBalancer.register("host-2");
        applicationLoadBalancer.register("host-3");
        applicationLoadBalancer.register("host-4");

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
        applicationLoadBalancer.register("host-1");
        applicationLoadBalancer.register("host-2");
        applicationLoadBalancer.register("host-3");
        applicationLoadBalancer.register("host-4");

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
}
