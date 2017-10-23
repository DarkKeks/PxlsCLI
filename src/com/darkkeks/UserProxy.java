package com.darkkeks;

public class UserProxy {

    private final String host;
    private final int port;

    public UserProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
