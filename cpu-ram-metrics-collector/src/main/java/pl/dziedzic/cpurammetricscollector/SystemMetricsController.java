package pl.dziedzic.cpurammetricscollector;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import pl.dziedzic.CpuMetrics;
import pl.dziedzic.RamMetrics;

@RestController
@RequestMapping("/metrics")
public class SystemMetricsController {

    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private final CentralProcessor processor = hardware.getProcessor();
    private final GlobalMemory memory = hardware.getMemory();

    private long[] prevTicks = processor.getSystemCpuLoadTicks();

    private final KafkaTemplate<String, RamMetrics> kafkaRamMetricsTemplate;
    private final KafkaTemplate<String, CpuMetrics> kafkaCpuMetricsTemplate;


    public SystemMetricsController(KafkaTemplate<String, RamMetrics> kafkaRamMetricsTemplate,
                                   KafkaTemplate<String, CpuMetrics> kafkaCpuMetricsTemplate) {
        this.kafkaRamMetricsTemplate = kafkaRamMetricsTemplate;
        this.kafkaCpuMetricsTemplate = kafkaCpuMetricsTemplate;
    }

    @GetMapping("/system")
    public String getSystemMetrics() {
        long[] newTicks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = newTicks;
        long[] frequencies = processor.getCurrentFreq();
        double avgFreq = frequencies.length > 0 ?
                java.util.Arrays.stream(frequencies).sum() / frequencies.length/ 1_000_000_000.0 : 0.0;
        long totalMemory = memory.getTotal();
        long usedMemory = totalMemory - memory.getAvailable();
        double ramUsagePercentage = ((double) usedMemory / (double) totalMemory) * 100;

        RamMetrics ramMetrics = new RamMetrics(
                Math.round((totalMemory/ (1024.0* 1024 *1024)) + 100.0) / 100.0,
                Math.round(ramUsagePercentage * 100.0)/ 100.0);

        CpuMetrics cpuMetrics = new CpuMetrics(
                avgFreq,
                Math.round(cpuLoad * 100.0) /100.0);
        kafkaCpuMetricsTemplate.send("cpu-metrics", cpuMetrics)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("✅ Sent CPU Metrics successfully: " + result.getRecordMetadata());
                    } else {
                        System.err.println("❌ Failed to send CPU Metrics: " + ex.getMessage());
                    }
                });
        kafkaRamMetricsTemplate.send("ram-metrics", ramMetrics)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("✅ Sent RAM Metrics successfully: " + result.getRecordMetadata());
                    } else {
                        System.err.println("❌ Failed to send RAM Metrics: " + ex.getMessage());
                    }
                });

        return "1. Cpu metrics: " + cpuMetrics + ";\n2. Ram metrics: " + ramMetrics;
    }
}
