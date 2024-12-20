package individual.business.implementation;

import individual.domain.event.Event;
import individual.persistence.entity.EventEntity;

public class EventConverter {
    private EventConverter() {}

    public static Event convert(EventEntity eventEntity) {
        return Event.builder()
                .eventId(eventEntity.getEventId())
                .title(eventEntity.getTitle())
                .description(eventEntity.getDescription())
                .location(eventEntity.getLocation())
                .date(eventEntity.getDate())
                .creatorId(eventEntity.getCreator() != null ? eventEntity.getCreator().getUserId() : null)
                .build();
    }
}