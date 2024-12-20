package individual.business.implementation;

import individual.business.DeleteEventUseCase;
import individual.persistence.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {
    EventRepository eventRepository;
    @Override
    public void deleteEvent(long eventId)
    {
        this.eventRepository.deleteById(eventId);
    }
}
