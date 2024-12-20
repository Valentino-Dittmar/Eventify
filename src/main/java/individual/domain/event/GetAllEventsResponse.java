package individual.domain.event;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class GetAllEventsResponse {
    private List<Event> events;
}
