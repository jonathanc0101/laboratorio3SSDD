package bordero.backend.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event<DTO> {
    @Builder.Default
    public final EventType type = null;
    @Builder.Default
    public final String serverId = null;
    @Builder.Default
    public final DTO dto = null;
}
