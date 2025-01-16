package individual.persistence;

import individual.persistence.entity.EventAttendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAttendantRepository extends JpaRepository<EventAttendant, Long> {
    @Query("SELECT ea.event.eventId FROM EventAttendant ea WHERE ea.user.userId = :userId")
    List<Long> findEventIdsByUserId(Long userId);
}
