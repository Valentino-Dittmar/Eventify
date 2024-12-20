package individual.business.implementation;

import individual.business.CreateEventUseCase;
import individual.domain.event.CreateEventRequest;
import individual.domain.event.CreateEventResponse;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CreateEventResponse createEvent(CreateEventRequest request) {
        UserEntity creator = userRepository.findByUserId(request.getCreatorId());
        if (creator == null) {
            throw new IllegalArgumentException("Creator with ID " + request.getCreatorId() + " not found");
        }

        List<UserEntity> attendants = request.getAttendantIds() != null && !request.getAttendantIds().isEmpty()
                ? userRepository.findAllById(request.getAttendantIds())
                : List.of();

        EventEntity newEvent = EventEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .date(request.getDate())
                .creator(creator)
                .attendants(attendants)
                .build();

        EventEntity savedEvent = eventRepository.save(newEvent);

        return CreateEventResponse.builder()
                .eventId(savedEvent.getEventId())
                .build();
    }
}