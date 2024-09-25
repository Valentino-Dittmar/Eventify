package individual.business.implementation;

import individual.business.GetServiceUseCase;
import individual.persistence.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetServiceUseCaseImpl implements GetServiceUseCase {
    private ServiceRepository serviceRepository;
    @Override
    public Optional<individual.domain.service.Service> findById(Long serviceId) {
        return serviceRepository.findById(serviceId).map(ServiceConverter::convert);
    }
}
