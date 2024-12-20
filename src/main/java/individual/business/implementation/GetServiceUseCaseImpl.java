package individual.business.implementation;

import individual.business.GetServiceUseCase;
import individual.persistence.ServiceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetServiceUseCaseImpl implements GetServiceUseCase {
    private ServiceRepository serviceRepository;
    @Override
    public Optional<individual.domain.service.Service> findById(Long serviceId) {
        return serviceRepository.findByServiceId(serviceId).map(ServiceConverter::convert);
    }
}
