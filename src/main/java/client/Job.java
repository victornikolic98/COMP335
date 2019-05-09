package client;

public class Job {

    public static final int JOB_SUBMITTED = 0;
    public static final int JOB_WAITING = 1;
    public static final int JOB_RUNNING = 2;
    public static final int JOB_SUSPENDED = 3;
    public static final int JOB_COMPLETED = 4;
    public static final int JOB_FAILED = 5;
    public static final int JOB_KILLED = 6;

    private int submitTime;
    private int jobID;
    private int estimatedRunTime;
    private int numberOfCores;
    private int memory;
    private int disk;

    public Job(String jobText) {
        String[] tokens = jobText.split(" ");
        this.submitTime = Integer.valueOf(tokens[1]);
        this.jobID = Integer.valueOf(tokens[2]);
        this.estimatedRunTime = Integer.valueOf(tokens[3]);
        this.numberOfCores = Integer.valueOf(tokens[4]);
        this.memory = Integer.valueOf(tokens[5]);
        this.disk = Integer.valueOf(tokens[6]);
    }

    public int getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(int submitTime) {
        this.submitTime = submitTime;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getEstimatedRunTime() {
        return estimatedRunTime;
    }

    public void setEstimatedRunTime(int estimatedRunTime) {
        this.estimatedRunTime = estimatedRunTime;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDisk() {
        return disk;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }
}
