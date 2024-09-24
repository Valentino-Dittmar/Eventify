package individual.business;

import individual.domain.service.GetAllServicesRequest;
import individual.domain.service.GetAllServicesResponse;

public interface GetServicesUseCase {
    GetAllServicesResponse getStudents(GetAllServicesRequest request);
}
