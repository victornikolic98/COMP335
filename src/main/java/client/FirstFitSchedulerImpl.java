package client;

public class FirstFitSchedulerImpl extends Scheduler {

    public FirstFitSchedulerImpl(boolean isVerbose) {
        super(isVerbose);
    }

    @Override
    protected Server getServer(Job currentJob, Servers availableServers) {
        Server server;
        server = availableServers.findFirstFit(currentJob);
        return server;
    }
}
