package individual.business;

import individual.domain.event.Event;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GetAttendingEventsByUserIdUseCase {
    @Transactional(readOnly = true)
    List<Event> getAttendingEventsByUserId(Long userId);
}
