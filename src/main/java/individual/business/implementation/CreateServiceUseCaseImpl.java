package individual.business.implementation;

import individual.business.CreateServiceUseCase;
import individual.domain.service.CreateServiceRequest;
import individual.domain.service.CreateServiceResponse;
import individual.persistence.EventRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.ServiceEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateServiceUseCaseImpl implements CreateServiceUseCase {
    private final ServiceRepository serviceRepository;
    private final EventRepository eventRepository;

    @Override
    public CreateServiceResponse createService(CreateServiceRequest request) {
        ServiceEntity savedEntity = saveNewService(request);
        return CreateServiceResponse.builder()
                .serviceId(savedEntity.getServiceId())
                .build();
    }
    @Transactional
    public ServiceEntity saveNewService(CreateServiceRequest request) {
        EventEntity event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found for ID: " + request.getEventId()));
        ServiceEntity newService = ServiceEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .duration(request.getDuration())
                .event(event)
                .build();

        return serviceRepository.save(newService);
    }



}
