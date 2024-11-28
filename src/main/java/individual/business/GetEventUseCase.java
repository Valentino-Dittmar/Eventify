package individual.business;

import individual.domain.event.Event;

import java.util.Optional;

public interface GetEventUseCase {
    Optional<Event> findById(Long eventId);
}
