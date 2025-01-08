package individual.business;

import individual.domain.User.User;

import java.util.List;

public interface GetEventAttendantsUseCase {
    List<User> getAttendantsByEventId(Long eventId);
}
