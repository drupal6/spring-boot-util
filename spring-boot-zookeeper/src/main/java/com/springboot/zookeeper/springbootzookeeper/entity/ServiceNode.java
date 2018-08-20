package com.springboot.zookeeper.springbootzookeeper.entity;

public class ServiceNode {

    private String path;
    private String address;
    private int port;

    public ServiceNode(String path, String address, int port) {
        this.path = path;
        this.address = address;
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
