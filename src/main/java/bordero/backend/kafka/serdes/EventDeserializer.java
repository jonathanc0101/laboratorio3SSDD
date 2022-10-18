package bordero.backend.kafka.serdes;

import bordero.backend.kafka.Event;
import bordero.dto.PlayDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class EventDeserializer implements Deserializer<Event> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Event deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.error("Se pidió deserializar un valor nulo");
                return null;
            }
            log.debug("Deserializando ...");
            return mapper.readValue(
                    new String(data, "UTF-8"),
                    new TypeReference<Event<PlayDTO>>() {
                    });
        } catch (Exception e) {
            throw new SerializationException("Error en la deserialización de byte[] a Event");
        }
    }
}
