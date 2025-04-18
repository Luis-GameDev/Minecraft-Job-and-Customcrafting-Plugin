public class PlayerJobData {
    private JobType activeJob;
    private int xp;

    public PlayerJobData(JobType activeJob, int xp) {
        this.activeJob = activeJob;
        this.xp = xp;
    }

    public JobType getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(JobType activeJob) {
        this.activeJob = activeJob;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int amount) {
        this.xp += amount;
    }
}
