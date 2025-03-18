package pl.dziedzic;

import java.time.LocalDateTime;

public class CpuMetrics {
    private double cpuPower;
    private double cpuUsagePercentage;
    private LocalDateTime time;

    public CpuMetrics() {
    }

    public CpuMetrics(double cpuPower, double cpuUsagePercentage, LocalDateTime time) {
        this.cpuPower = cpuPower;
        this.cpuUsagePercentage = cpuUsagePercentage;
        this.time = time;
    }

    public CpuMetrics(double cpuPower, double cpuUsagePercentage) {
        this.cpuPower = cpuPower;
        this.cpuUsagePercentage = cpuUsagePercentage;
        this.time = LocalDateTime.now();
    }
    public double getCpuPower() {
        return cpuPower;
    }

    public void setCpuPower(double cpuPower) {
        this.cpuPower = cpuPower;
    }

    public double getCpuUsagePercentage() {
        return cpuUsagePercentage;
    }

    public void setCpuUsagePercentage(double cpuUsagePercentage) {
        this.cpuUsagePercentage = cpuUsagePercentage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CpuMetrics{" +
                "cpuPower=" + cpuPower +
                ", cpuUsagePercentage=" + cpuUsagePercentage +
                ", time=" + time +
                '}';
    }
}
