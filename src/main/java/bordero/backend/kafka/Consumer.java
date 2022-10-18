package bordero.backend.kafka;

import bordero.backend.controller.DTOModelMapper;
import bordero.backend.model.Play;
import bordero.backend.service.PlayService;
import bordero.dto.PlayDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @Autowired
    PlayService playService;

    @Autowired
    DTOModelMapper<PlayDTO, Play> mapper;

    @Value("${bordero.server.id}")
    private String serverId;

    @KafkaListener(topics="plays", groupId = "bordero-kafka")
    public void consume(Event event) {
        log.info("Event received at: " + serverId);
        log.info("Consuming event: " + event.toString());
         if (serverId.compareTo(event.serverId)!=0) {
             if (event.type == EventType.CREATE) {
                 Play play = mapper.toModel((PlayDTO) event.dto);
                 playService.insert(play);
                 log.info("Play inserted by consumer");
             }
         }
    }
}
