package individual.business.implementation;

import individual.business.UpdateServiceUseCase;
import individual.business.exception.ServiceNotFoundException;
import individual.domain.service.UpdateServiceRequest;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateServiceUseCaseImpl implements UpdateServiceUseCase {
    private final ServiceRepository serviceRepository;

    @Override
    public void updateService(UpdateServiceRequest request) {
        Optional<ServiceEntity> serviceOptional = serviceRepository.findById(request.getServiceId());
        if (serviceOptional.isEmpty()) {
            throw(new ServiceNotFoundException(request.getServiceId()));
        }
        ServiceEntity service = serviceOptional.get();
        updateFields(request, service);
    }
    private void updateFields(UpdateServiceRequest request, ServiceEntity service) {
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        serviceRepository.save(service);
    }


}
