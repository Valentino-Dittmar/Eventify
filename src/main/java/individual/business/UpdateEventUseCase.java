package individual.business;

import individual.domain.event.UpdateEventRequest;

public interface UpdateEventUseCase {
    void updateEvent(UpdateEventRequest request);
}
