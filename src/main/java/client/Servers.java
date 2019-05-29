package client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Servers {

    private List<Server> servers = new ArrayList<>();

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

    public Server findFirst(Job currentJob) {
        Server first = null;
        for(Server server: servers) {
            if(hasEnoughCores(currentJob, server) && hasEnoughMemory(currentJob, server)
                    && hasEnoughStorage(currentJob, server)) {
                 first = server;
                 break;
            }
        }
        return first;
    }

    private boolean hasEnoughStorage(Job currentJob, Server server) {
        return currentJob.getDisk() <= server.getDiskSize();
    }

    private boolean hasEnoughMemory(Job currentJob, Server server) {
        return currentJob.getMemory() <= server.getMemorySize();
    }

    private boolean hasEnoughCores(Job currentJob, Server server) {
        return currentJob.getNumberOfCores() <= server.getCoreCount();
    }

    public void removeAllServers() {
        servers.clear();
    }

    public Server findSuitableFirst(Job currentJob) {

        Server smallestIdle = null;

        List <Server> suitableServers = servers.stream()
                .filter((server) -> hasEnoughCores(currentJob, server)
                        && hasEnoughStorage(currentJob, server)
                        && hasEnoughMemory(currentJob, server)
                        && (server.isIdle() || server.isAvailable())
                        && hasEnoughAvailableTime(currentJob, server)
                )
//                .sorted()
                .collect(Collectors.toList());

        return suitableServers.get(0);

    }

    private boolean hasEnoughAvailableTime(Job currentJob, Server server) {
        return server.isAvailableTimeSufficient(currentJob);
    }

}
