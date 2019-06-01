package client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Servers {

    private List<Server> servers = new ArrayList<>();

    public void add(Server server) {
        servers.add(server);
    }

    public Server findLargest() {
        Comparator<Server> comparator = Comparator.comparingInt( Server::getCoreCount );
        return servers.stream().max(comparator).get();
    }

    public Server findFirstFit(Job currentJob) {
        return servers.stream()
                .filter((server) -> server.hasEnoughCores(currentJob.getNumberOfCores())
                        && server.hasEnoughStorage(currentJob.getDisk())
                        && server.hasEnoughMemory(currentJob.getMemory())
//                        && server.isAvailableTimeSufficient(currentJob)
                ).findFirst()
                .orElse(findLargest());
    }

    public void removeAllServers() {
        servers.clear();
    }

    public Server findSuitableFirst(Job currentJob) {

        return servers.stream()
                .filter((server) -> server.isIdle()
                        && server.hasEnoughCores(currentJob.getNumberOfCores())
                        && server.hasEnoughStorage(currentJob.getDisk())
                        && server.hasEnoughMemory(currentJob.getMemory())
                        && server.isAvailableTimeSufficient(currentJob)
                ).findFirst()
                .orElse(findFirstFit(currentJob));
    }

}
