package individual.business;

import jakarta.transaction.Transactional;

public interface AttendEventUseCase {
    @Transactional
    void attendEvent(Long eventId, Long userId);
}
