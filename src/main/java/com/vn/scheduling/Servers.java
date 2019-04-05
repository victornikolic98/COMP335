package com.vn.scheduling;

import java.util.ArrayList;
import java.util.List;

public class Servers {

    List<Server> servers = new ArrayList<>();

    public void add(Server server) {
        servers.add(server);
    }

    public Server findLargest() {
        Server max = servers.get(0);
        for(Server server: servers) {
            if(server.getCoreCount() > max.getCoreCount()) {
                max = server;
            }
        }
        return max;
    }

}
