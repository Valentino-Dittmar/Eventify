package individual.business.implementation;

import individual.business.AttendEventUseCase;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendEventUseCaseImpl implements AttendEventUseCase {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void attendEvent(Long eventId, Long userId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + eventId + " not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        boolean alreadyAttending = event.getAttendants().stream()
                .anyMatch(attendant -> attendant.getUserId().equals(userId));

        if (alreadyAttending) {
            throw new RuntimeException("User is already attending this event");
        }

        event.getAttendants().add(user);

        eventRepository.save(event);
    }
}