package individual.domain.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class CreateEventResponse {
    private Long eventId;
}
