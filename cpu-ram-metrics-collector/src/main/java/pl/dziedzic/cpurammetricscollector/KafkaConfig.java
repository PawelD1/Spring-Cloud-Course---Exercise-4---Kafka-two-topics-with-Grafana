package pl.dziedzic.cpurammetricscollector;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.dziedzic.CpuMetrics;
import pl.dziedzic.RamMetrics;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, RamMetrics> producerRamMetricsFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        JsonSerializer<RamMetrics> serializer = new JsonSerializer<>();

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), serializer);
    }

    @Bean
    public ProducerFactory<String, CpuMetrics> producerCpuMetricsFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        JsonSerializer<CpuMetrics> serializer = new JsonSerializer<>();

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), serializer);
    }


    @Bean
    public KafkaTemplate<String, RamMetrics> kafkaRamMetricsTemplate() { //łączenie się ms z kafką
        return new KafkaTemplate<>(producerRamMetricsFactory()); //dzięki temu będziemy mogli wstrzykiwać Kafkę do klasy SystemMetrics
    }

    @Bean
    public KafkaTemplate<String, CpuMetrics> kafkaCpuMetricsTemplate() { //łączenie się ms z kafką
        return new KafkaTemplate<>(producerCpuMetricsFactory()); //dzięki temu będziemy mogli wstrzykiwać Kafkę do klasy SystemMetrics
    }
}
