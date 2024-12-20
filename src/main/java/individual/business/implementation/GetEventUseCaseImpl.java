package individual.business.implementation;

import individual.business.GetEventUseCase;
import individual.domain.event.Event;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetEventUseCaseImpl implements GetEventUseCase {

    private final EventRepository eventRepository;
    @Override
    public Event getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        return EventConverter.convert(eventEntity);
    }
}
