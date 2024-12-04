package individual.business.implementation;

import individual.business.UpdateEventUseCase;
import individual.domain.event.UpdateEventRequest;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void updateEvent(UpdateEventRequest request) {
        EventEntity event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getDate() != null) {
            event.setDate(request.getDate());
        }
        if (request.getCreatorId() != null) {
            UserEntity creator = userRepository.findById(request.getCreatorId())
                    .orElseThrow(() -> new IllegalArgumentException("Creator with ID " + request.getCreatorId() + " not found"));
            event.setCreator(creator);
        }
        if (request.getAttendantIds() != null && !request.getAttendantIds().isEmpty()) {
            List<UserEntity> attendants = userRepository.findAllById(request.getAttendantIds());
            event.setAttendants(attendants);
        }

        eventRepository.save(event);
    }
}