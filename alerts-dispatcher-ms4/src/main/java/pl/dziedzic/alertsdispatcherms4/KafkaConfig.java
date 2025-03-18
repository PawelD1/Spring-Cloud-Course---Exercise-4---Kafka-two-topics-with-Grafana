package pl.dziedzic.alertsdispatcherms4;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import pl.dziedzic.dataaggregatoranalyzerms3.RamCpuAlert;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, RamCpuAlert> consumerFactory() {//factoria do konfiguracji brokerów i deserializacji
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "data-alerts-dispatcher"); // po to aby zawsze dostawać ten sam identyfikator dla tego mikroserwisu, nie losowy od Kafki
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //początek czytania od najstarszej informacji na Kafce

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<RamCpuAlert> deserializer = new JsonDeserializer<>(RamCpuAlert.class);
        deserializer.addTrustedPackages("pl.dziedzic");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<?> getKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RamCpuAlert> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
