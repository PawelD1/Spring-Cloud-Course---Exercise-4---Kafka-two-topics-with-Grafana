package pl.dziedzic.alertsdispatcherms4;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dziedzic.dataaggregatoranalyzerms3.RamCpuAlert;

import java.util.List;

@Controller
public class AlertController {

    private final AlertConsumer alertConsumer;

    public AlertController(AlertConsumer alertConsumer) {
        this.alertConsumer = alertConsumer;
    }

    @GetMapping("/get-alerts")
    public ResponseEntity<List<RamCpuAlert>> getAlerts(@RequestParam(required = false) String level) {
        List<RamCpuAlert> ramAlertList = alertConsumer.getRamCpuAlertList();
        if (level != null && !level.isEmpty()) {
            ramAlertList = ramAlertList
                    .stream()
                    .filter(element -> element.getMessage().equalsIgnoreCase(level))
                    .toList();
        }
        return ResponseEntity.ok(ramAlertList);
    }
}
