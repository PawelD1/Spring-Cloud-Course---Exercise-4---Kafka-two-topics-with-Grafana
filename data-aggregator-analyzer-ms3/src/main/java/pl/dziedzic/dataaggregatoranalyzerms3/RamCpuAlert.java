package pl.dziedzic.dataaggregatoranalyzerms3;

import java.time.LocalDateTime;

public class RamCpuAlert {

    private String message;
    private double ramUsage;
    private double cpuUsage;
    private LocalDateTime time;

    public RamCpuAlert() {
    }

    public RamCpuAlert(String message, double ramUsage, double cpuUsage, LocalDateTime time) {
        this.message = message;
        this.ramUsage = ramUsage;
        this.cpuUsage = cpuUsage;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(double ramUsage) {
        this.ramUsage = ramUsage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    @Override
    public String toString() {
        return "RamCpuAlert{" +
                "message='" + message + '\'' +
                ", ramUsage=" + ramUsage +
                ", cpuUsage=" + cpuUsage +
                ", time=" + time +
                '}';
    }
}
