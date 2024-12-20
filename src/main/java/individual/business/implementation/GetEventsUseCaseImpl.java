package individual.business.implementation;

import individual.business.GetEventsUseCase;
import individual.domain.event.Event;
import individual.domain.event.GetAllEventsRequest;
import individual.domain.event.GetAllEventsResponse;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import lombok.AllArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetEventsUseCaseImpl implements GetEventsUseCase {

    private final EventRepository eventRepository;

    @Override
    public GetAllEventsResponse getAllEvents(final GetAllEventsRequest request) {
        List<EventEntity> results = StringUtils.hasText(request.getTitle())
                ? eventRepository.findAllByTitle(request.getTitle())
                : eventRepository.findAll();

        List<Event> events = results.stream()
                .map(EventConverter::convert)
                .toList();

        return GetAllEventsResponse.builder()
                .events(events)
                .build();
    }



    @Override
    public List<Event> getEventsByCreator(Long creatorId) {
        List<EventEntity> eventEntities = eventRepository.findByCreatorUserId(creatorId);
        return eventEntities.stream().map(EventConverter::convert).collect(Collectors.toList());
    }

    @Override
    public Event getEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        return EventConverter.convert(eventEntity);
    }
}
