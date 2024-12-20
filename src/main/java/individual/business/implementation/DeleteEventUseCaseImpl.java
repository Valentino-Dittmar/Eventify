package individual.business.implementation;

import individual.business.DeleteEventUseCase;
import individual.persistence.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {
    EventRepository eventRepository;
    @Transactional
    @Override
    public void deleteEvent(Long eventId)
    {
        if(eventId == null ) {
            throw(new IllegalArgumentException("Event id is null"));
        }
        eventRepository.deleteById(eventId);
    }

}
