package individual.business.implementation;

import individual.business.GetEventUseCase;
import individual.domain.event.Event;
import individual.persistence.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetEventUseCaseImpl implements GetEventUseCase {
    private final EventRepository eventRepository;

    @Override
    public Optional<Event> findById(Long eventId) {
        return eventRepository.findById(eventId).map(EventConverter::convert);
    }
}
