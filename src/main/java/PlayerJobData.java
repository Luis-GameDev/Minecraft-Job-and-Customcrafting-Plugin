public class PlayerJobData {
    private JobType activeJob;
    private double xp;

    public PlayerJobData(JobType activeJob, double xp) {
        this.activeJob = activeJob;
        this.xp = xp;
    }

    public JobType getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(JobType activeJob) {
        this.activeJob = activeJob;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void addXp(double amount) {
        this.xp += amount;
    }
}
