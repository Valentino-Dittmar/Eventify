package individual.business;

import individual.domain.service.Service;

import java.util.Optional;

public interface GetServiceUseCase {
    Optional<Service> findById(Long serviceId);
}
