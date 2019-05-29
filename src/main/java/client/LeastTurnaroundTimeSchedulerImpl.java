package client;

public class LeastTurnaroundTimeSchedulerImpl extends Scheduler {

    public LeastTurnaroundTimeSchedulerImpl(boolean isVerbose) {
        super(isVerbose);
    }

    @Override
    protected Server getServer(Job currentJob, Servers availableServers) {
        Server server;
        server = availableServers.findSuitableFirst(currentJob);
        return server;
    }
}
