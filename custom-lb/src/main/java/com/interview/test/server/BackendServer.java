package com.interview.test.server;

import java.util.Objects;

public class BackendServer {

    private String hostName;

    private String ip;

    private int weight = 1;

    public BackendServer(String hostName) {
        this.hostName = hostName;
    }

    public BackendServer(String hostName, int weight) {
        this.hostName = hostName;
        this.weight = weight;
    }

    public BackendServer(String hostName, int weight,String ip) {
        this.hostName = hostName;
        this.weight = weight;
        this.ip = ip;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "BackendServer{" +
                "hostName='" + hostName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackendServer that = (BackendServer) o;
        return weight == that.weight && Objects.equals(hostName, that.hostName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostName, weight);
    }
}
