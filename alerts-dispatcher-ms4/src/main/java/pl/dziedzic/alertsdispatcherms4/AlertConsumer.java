package pl.dziedzic.alertsdispatcherms4;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.dziedzic.dataaggregatoranalyzerms3.RamCpuAlert;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertConsumer {

    private final List<RamCpuAlert> ramCpuAlertList;

    public AlertConsumer() {
        this.ramCpuAlertList = new ArrayList<>();
    }

    public List<RamCpuAlert> getRamCpuAlertList() {
        return ramCpuAlertList;
    }

    @KafkaListener(
            topics = "alerts",
            groupId = "data-alerts-dispatcher",
            containerFactory = "getKafkaListenerContainerFactory"
    )
    public void analyzeMetrics(RamCpuAlert ramCpuAlert) {//dzięki messageConverter możliwe jest danie tutaj typu SystemMetrics jako parametr, w przeciwnym razie trzeba by zamieniać Object na SystemMetrics
        ramCpuAlertList.add(ramCpuAlert);
    }

}