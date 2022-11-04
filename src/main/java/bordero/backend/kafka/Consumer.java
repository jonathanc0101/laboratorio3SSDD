package bordero.backend.kafka;

import bordero.backend.TransaccionModel;
import bordero.backend.TransaccionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @Autowired
    TransaccionService transaccionService;

    @Value("${bordero.server.id}")
    private String serverId;

    @KafkaListener(topics="operaciones", groupId = "bordero-kafka")
    public void consume(Event event) {
        log.info("Event received at: " + serverId);
        log.info("Consuming event: " + event.toString());
        log.info("comparation serverId.compareTo(event.serverId)" + serverId.compareTo(event.serverId));
         if (serverId.compareTo(event.serverId)!=0) {
             if (event.type == EventType.CREATE) {
                 TransaccionModel transaccion = (TransaccionModel) event.dto;
                 transaccionService.insert(transaccion);
                 log.info("Transaction inserted by consumer");
             }
         }
    }
}
