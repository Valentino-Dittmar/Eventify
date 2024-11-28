package individual.business.implementation;

import individual.business.DeleteServiceUseCase;
import individual.persistence.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteServiceUseCaseImpl implements DeleteServiceUseCase {
    private final ServiceRepository serviceRepository;
    @Override
    public void deleteService(long serviceId) {
        this.serviceRepository.deleteByServiceId(serviceId);
    }
}
