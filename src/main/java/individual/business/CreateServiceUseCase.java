package individual.business;

import individual.domain.service.CreateServiceRequest;
import individual.domain.service.CreateServiceResponse;

public interface CreateServiceUseCase {
    CreateServiceResponse createService(CreateServiceRequest request);
}
