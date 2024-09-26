package individual.business.implementation;

import individual.business.CreateServiceUseCase;
import individual.domain.service.CreateServiceRequest;
import individual.domain.service.CreateServiceResponse;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateServiceUseCaseImpl implements CreateServiceUseCase {
    private final ServiceRepository serviceRepository;

    @Override
    public CreateServiceResponse createService(CreateServiceRequest request) {
        ServiceEntity savedEntity = saveNewService(request);
        return CreateServiceResponse.builder()
                .serviceId(savedEntity.getServiceId())
                .build();
    }

    private ServiceEntity saveNewService(CreateServiceRequest request) {
        ServiceEntity newService = ServiceEntity.builder()
                .name(request.getName())
                .build();
        return serviceRepository.save(newService);
    }



}
