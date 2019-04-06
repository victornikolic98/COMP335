package vn_cm.scheduling;

public class Server {

    // Initialising server attributes
    private String type;
    private int limit;
    private int bootupTime;
    private double rate;
    private int coreCount;
    private int memorySize;
    private int diskSize;

    private String serverId;
    private int serverState;
    private int availableTime;

    public Server() {
    }

    public Server(String resource) {

        String[] tokens = resource.split(" ");
        this.type = tokens[0];
        this.serverId = tokens[1];
        this.serverState = Integer.valueOf(tokens[2]);
        this.availableTime = Integer.valueOf(tokens[3]);
        this.coreCount = Integer.valueOf(tokens[4]);
        this.memorySize = Integer.valueOf(tokens[5]);;
        this.diskSize = Integer.valueOf(tokens[6]);;
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

}
