package pl.dziedzic.dataaggregatoranalyzerms3;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.dziedzic.CpuMetrics;
import pl.dziedzic.RamMetrics;

import java.time.LocalDateTime;

@Service
public class MetricsAnalyzerService {

    private final KafkaTemplate<String, RamCpuAlert> kafkaTemplate;

    public MetricsAnalyzerService(KafkaTemplate<String, RamCpuAlert> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "cpu-metrics", groupId = "data-aggregator-analyzer")
    public void analyzeCpuMetrics(CpuMetrics cpuMetrics) {
        processMetrics(null, cpuMetrics);
    }

    @KafkaListener(topics = "ram-metrics", groupId = "data-aggregator-analyzer")
    public void analyzeRamMetrics(RamMetrics ramMetrics) {
        processMetrics(ramMetrics, null);
    }

    private void processMetrics(RamMetrics ramMetrics, CpuMetrics cpuMetrics) {
        double ramUsagePercentage = (ramMetrics != null) ? ramMetrics.getRamUsagePercentage() : 0;
        double cpuUsagePercentage = (cpuMetrics != null) ? cpuMetrics.getCpuUsagePercentage() : 0;

        if (ramUsagePercentage >= 70) {
            sendAlert("CRITICAL RAM", ramUsagePercentage, cpuUsagePercentage, ramMetrics.getTime());
        }
        if (cpuUsagePercentage >= 70) {
            sendAlert("CRITICAL CPU", ramUsagePercentage, cpuUsagePercentage, cpuMetrics.getTime());
        }
        if (ramUsagePercentage < 70 && ramUsagePercentage >= 50) {
            sendAlert("WARNING RAM", ramUsagePercentage, cpuUsagePercentage, ramMetrics.getTime());
        }
        if (cpuUsagePercentage < 70 && cpuUsagePercentage >= 50) {
            sendAlert("WARNING CPU", ramUsagePercentage, cpuUsagePercentage, cpuMetrics.getTime());
        }
        if (ramUsagePercentage < 50 && ramUsagePercentage > 0) {
            sendAlert("NORMAL USAGE RAM", ramUsagePercentage, cpuUsagePercentage, ramMetrics.getTime());
        }
        if (cpuUsagePercentage < 50 && cpuUsagePercentage > 0) {
            sendAlert("NORMAL USAGE CPU", ramUsagePercentage, cpuUsagePercentage, cpuMetrics.getTime());
        }
    }


    private void sendAlert(String level, double ramUsage, double cpuUsage, LocalDateTime time) {
        RamCpuAlert alert = new RamCpuAlert(level, ramUsage, cpuUsage, time);
        kafkaTemplate.send("alerts", alert);
        System.out.println(level + " 🛑 " + alert);
    }
}
