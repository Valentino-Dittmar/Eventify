package individual.business;

import individual.domain.event.Event;
import individual.domain.event.GetAllEventsRequest;
import individual.domain.event.GetAllEventsResponse;

import java.util.List;

public interface GetEventsUseCase {

    GetAllEventsResponse getAllEvents(GetAllEventsRequest request);

    List<Event> getEventsByCreator(Long creatorId);

    Event getEventById(Long eventId);
}
