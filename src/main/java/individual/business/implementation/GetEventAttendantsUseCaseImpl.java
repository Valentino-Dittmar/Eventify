package individual.business.implementation;

import individual.business.GetEventAttendantsUseCase;
import individual.domain.User.User;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetEventAttendantsUseCaseImpl implements GetEventAttendantsUseCase {

    private final EventRepository eventRepository;

    @Override
    public List<User> getAttendantsByEventId(Long eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + eventId + " not found"));

        return event.getAttendants().stream()
                .map(UserConverter::convert)
                .collect(Collectors.toList());
    }
}