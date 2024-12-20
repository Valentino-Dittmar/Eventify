package individual.business;

import individual.domain.event.CreateEventRequest;
import individual.domain.event.CreateEventResponse;
import individual.persistence.entity.EventEntity;
import jakarta.transaction.Transactional;

public interface CreateEventUseCase {
    CreateEventResponse createEvent(CreateEventRequest request);
}
