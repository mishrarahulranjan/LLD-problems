package com.interview.test.lb;

import com.interview.test.constant.Constant;
import com.interview.test.lb.strategy.LoadBalancingStrategy;
import com.interview.test.server.BackendServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ApplicationLoadBalancer {

    private int maxBackEndServerCount;

    private Map<String, BackendServer> uniqueBackendHostsMap ;

    private LoadBalancingStrategy loadBalancingStrategy;

    private Lock lock  = new ReentrantLock();

    public ApplicationLoadBalancer(int maxBackEndServerCount, LoadBalancingStrategy loadBalancingStrategy){
        this.maxBackEndServerCount = maxBackEndServerCount;
        this.loadBalancingStrategy = loadBalancingStrategy;
        uniqueBackendHostsMap = new HashMap<>(maxBackEndServerCount);
    }


    public boolean register(BackendServer hostInfo){
       try{
           lock.lock();
            if(uniqueBackendHostsMap.size()>= maxBackEndServerCount){
                throw new RuntimeException(Constant.Message.LB_MAX_CAPACITY_MSG);
            }else{
                //uniqueness check
                if(!uniqueBackendHostsMap.containsKey(hostInfo.getHostName())){
                    uniqueBackendHostsMap.put(hostInfo.getHostName(), hostInfo);
                    return true;
                }else{
                    throw new RuntimeException(Constant.Message.LB_HOST_DUPLICATE_MSG);
                }
            }
        }finally{
           lock.unlock();
        }
   }

    public String getBackEndInstance(){
        List<BackendServer> serverList = new ArrayList<>(uniqueBackendHostsMap.values());
        if(serverList.size() ==0){
            throw new RuntimeException(Constant.Message.LB_NO_BACKEND_SERVER);
        }
        return loadBalancingStrategy.getBackEndServer(serverList).toString();
    }
}
