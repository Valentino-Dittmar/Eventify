package individual.business.implementation;

import individual.business.GetServicesUseCase;
import individual.domain.service.GetAllServicesRequest;
import individual.domain.service.GetAllServicesResponse;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@AllArgsConstructor
public class GetServicesUseCaseImpl implements GetServicesUseCase {
    private final ServiceRepository serviceRepository;

    @Override
    public GetAllServicesResponse getServices(final GetAllServicesRequest request) {
        List<ServiceEntity> results = StringUtils.hasText(request.getName())
                ? serviceRepository.findAllByName(request.getName())
                : serviceRepository.findAll();

        List<individual.domain.service.Service> services = results.stream()
                .map(ServiceConverter::convert)
                .toList();

        return GetAllServicesResponse.builder()
                .services(services)
                .build();
    }


}
