package individual.business.implementation;
import individual.business.implementation.CreateServiceUseCaseImpl;
import individual.domain.service.CreateServiceRequest;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private CreateServiceUseCaseImpl createServiceUseCase;

    @Test
    void shouldCreateServiceSuccessfully() {
        // Arrange
        CreateServiceRequest createRequest = new CreateServiceRequest("TestName", "TestDescription", BigDecimal.valueOf(12.50) , BigDecimal.valueOf(13.12),1L);

        ServiceEntity serviceEntity = ServiceEntity.builder()
                .serviceId(1L)
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .build();
        when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(serviceEntity);

        when(serviceRepository.findById(serviceEntity.getServiceId())).thenReturn(Optional.of(serviceEntity));

        // Act
        createServiceUseCase.createService(createRequest);

        // Assert
        ServiceEntity savedServiceEntity = serviceRepository.findById(serviceEntity.getServiceId()).orElse(null);
        assertNotNull(savedServiceEntity);
        assertEquals(serviceEntity.getName(), savedServiceEntity.getName());
        assertEquals(serviceEntity.getDescription(), savedServiceEntity.getDescription());
    }
}

