package client;

public class AllToLargestSchedulerImpl extends Scheduler {

    public AllToLargestSchedulerImpl(boolean isVerbose) {
        super(isVerbose);
    }

    @Override
    protected Server getServer(Job currentJob, Servers availableServers) {
        Server server;
        server = availableServers.findLargest();
        return server;
    }
}
