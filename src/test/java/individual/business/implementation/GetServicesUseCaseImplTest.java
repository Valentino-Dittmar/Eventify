package individual.business.implementation;

import individual.domain.service.GetAllServicesRequest;
import individual.domain.service.GetAllServicesResponse;
import individual.domain.service.Service;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetServicesUseCaseImplTest {

    @Mock
    ServiceRepository serviceRepository;

    @InjectMocks
    GetServicesUseCaseImpl getServicesUseCase;

    @Test
    public void shouldGetServices() {
        // Arrange
        ServiceEntity serviceEntity = ServiceEntity.builder()
                .serviceId(1L)
                .name("name")
                .description("description")
                .price(BigDecimal.valueOf(12.50))
                .duration(BigDecimal.valueOf(15.00))
                .build();

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(serviceEntity));

        GetAllServicesRequest getAllServicesRequest = GetAllServicesRequest.builder()
                .build();

        // Act
        GetAllServicesResponse response = getServicesUseCase.getServices(getAllServicesRequest);

        List<Service> expectedServices = Arrays.asList(serviceEntity)
                .stream()
                .map(ServiceConverter::convert)
                .collect(Collectors.toList());

        // Assert
        assertEquals(expectedServices, response.getServices());
        assertEquals(response.getServices().size(), 1);
        assertEquals(response.getServices().isEmpty(), false);
    }
}


