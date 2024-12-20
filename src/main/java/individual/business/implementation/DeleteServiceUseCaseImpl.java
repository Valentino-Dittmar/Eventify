package individual.business.implementation;

import individual.business.DeleteServiceUseCase;
import individual.business.exception.ServiceNotFoundException;
import individual.persistence.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DeleteServiceUseCaseImpl implements DeleteServiceUseCase {

    private final ServiceRepository serviceRepository;


    @Transactional
    @Override
    public void deleteService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new ServiceNotFoundException(serviceId);
        }
        serviceRepository.deleteById(serviceId);
    }
}
