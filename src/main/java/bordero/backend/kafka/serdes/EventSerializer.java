package bordero.backend.kafka.serdes;

import bordero.backend.kafka.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class EventSerializer implements Serializer<Event> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, Event event) {
        try {
            if (event == null){
                log.error("Evento nulo recibido");
                return null;
            }
            log.debug("Serializando");
            return mapper.writeValueAsBytes(event);
        } catch (Exception e) {
            throw new SerializationException("Error cuando se serializaba un evento a byte");
        }
    }

}
