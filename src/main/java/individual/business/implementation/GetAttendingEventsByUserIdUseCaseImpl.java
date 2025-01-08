package individual.business.implementation;

import individual.business.GetAttendingEventsByUserIdUseCase;
import individual.domain.event.Event;
import individual.persistence.EventAttendantRepository;
import individual.persistence.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetAttendingEventsByUserIdUseCaseImpl implements GetAttendingEventsByUserIdUseCase {

    private final EventAttendantRepository eventAttendantRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public List<Event> getAttendingEventsByUserId(Long userId) {
        List<Long> eventIds = eventAttendantRepository.findEventIdsByUserId(userId);

        if (eventIds.isEmpty()) {
            return List.of();
        }

        return eventRepository.findAllById(eventIds).stream()
                .map(EventConverter::convert)
                .collect(Collectors.toList());
    }
}