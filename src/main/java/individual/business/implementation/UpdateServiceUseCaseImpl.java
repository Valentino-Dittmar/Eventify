package individual.business.implementation;

import individual.business.UpdateServiceUseCase;
import individual.business.exception.ServiceNotFoundException;
import individual.domain.service.UpdateServiceRequest;
import individual.persistence.EventRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UpdateServiceUseCaseImpl implements UpdateServiceUseCase {
    private final ServiceRepository serviceRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public void updateService(UpdateServiceRequest request) {
        ServiceEntity service = serviceRepository.findByServiceId(request.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundException(request.getServiceId()));

        if (request.getName() != null) {
            service.setName(request.getName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }
        if (request.getDuration() != null) {
            service.setDuration(request.getDuration());
        }
        if (request.getEventId() != null) {
            EventEntity event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found"));
            service.setEvent(event);
        }

        serviceRepository.save(service);
    }
}