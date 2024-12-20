package individual.business;

import individual.domain.event.CreateEventRequest;
import individual.domain.event.CreateEventResponse;

public interface CreateEventUseCase {
    CreateEventResponse createEvent(CreateEventRequest request);
}
