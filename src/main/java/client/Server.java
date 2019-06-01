package client;

public class Server {

    private static final int INACTIVE = 0;
    private static final int BOOTING = 1;
    private static final int IDLE = 2;
    private static final int ACTIVE = 3;
    public static final int UNAVAILABLE = 4;

    // Initialising server attributes
    private String type;
    private int limit;
    private int bootupTime;
    private double rate;
    private int coreCount;
    private int memorySize;
    private int diskSize;
    private int id;
    private int serverState;
    private int availableTime;

    private String serverId;

    public Server() {
    }

    public boolean isIdle() {
        return IDLE == serverState;
    }

    public boolean isAvailableTimeSufficient(Job job) {
        boolean hasTime;
        if (isIdle()) {
            hasTime = availableTime >= job.getEstimatedRunTime();
        } else {
            hasTime =  availableTime >= job.getEstimatedRunTime() && bootupTime + job.getEstimatedRunTime() <= job.getSubmitTime();
        }
        return hasTime;
    }

    public static Server createFromResponse(String response) {

        Server result = new Server();
        String[] tokens = response.split(" ");
        result.setType(tokens[0]);
        result.setId(Integer.valueOf(tokens[1]));
        result.setServerState(Integer.valueOf(tokens[2]));
        result.setAvailableTime(Integer.valueOf(tokens[3]));
        result.setCoreCount(Integer.valueOf(tokens[4]));
        result.setMemorySize(Integer.valueOf(tokens[5]));
        result.setDiskSize(Integer.valueOf(tokens[6]));

        return result;
    }

    public Server(String resource) {
        String[] tokens = resource.split(" ");
        this.type = tokens[0];
        this.serverId = tokens[1];
        this.serverState = Integer.valueOf(tokens[2]);
        this.availableTime = Integer.valueOf(tokens[3]);
        this.coreCount = Integer.valueOf(tokens[4]);
        this.memorySize = Integer.valueOf(tokens[5]);
        this.diskSize = Integer.valueOf(tokens[6]);
    }

    public boolean hasEnoughStorage(int jobStorage) {
        return diskSize >= jobStorage;
    }

    public boolean hasEnoughMemory(int jobMemory) {
        return memorySize >= jobMemory;
    }

    public boolean hasEnoughCores(int jobCoreCount) {
        return coreCount >= jobCoreCount;
    }

    public String getType() {
        return type;
    }

    public int getLimit() {
        return limit;
    }

    public int getBootupTime() {
        return bootupTime;
    }

    public double getRate() {
        return rate;
    }

    public int getCoreCount() {
        return coreCount;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setBootupTime(int bootupTime) {
        this.bootupTime = bootupTime;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setCoreCount(int coreCount) {
        this.coreCount = coreCount;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerState() {
        return serverState;
    }

    public void setServerState(int serverState) {
        this.serverState = serverState;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
}
