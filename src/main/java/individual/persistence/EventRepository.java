package individual.persistence;
import individual.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAll();
    List<EventEntity> findAllByTitle(String title);
    List<EventEntity> findByCreatorUserId(Long userId);
    EventEntity findByEventId(Long eventId);
    void deleteByEventId(Long eventId);
}
