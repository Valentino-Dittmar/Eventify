package individual.business.implementation.Service;

import individual.business.implementation.GetServiceUseCaseImpl;
import individual.domain.service.Service;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private GetServiceUseCaseImpl getServiceUseCase;

    @Test
    void shouldReturnServiceWithAllFieldsAndNestedEntity() {
        // Arrange
        Long serviceId = 1L;
        Long eventId = 2L;
        String serviceName = "Test Service";
        String serviceDescription = "This is a test description.";
        BigDecimal price = BigDecimal.valueOf(50.0);
        BigDecimal duration = BigDecimal.valueOf(1.5);


        EventEntity eventEntity = EventEntity.builder()
                .eventId(eventId)
                .build();

        ServiceEntity serviceEntity = ServiceEntity.builder()
                .serviceId(serviceId)
                .name(serviceName)
                .description(serviceDescription)
                .price(price)
                .duration(duration)
                .event(eventEntity)
                .build();

        // Mock repository behavior :)
        when(serviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(serviceEntity));

        // Act
        Optional<Service> result = getServiceUseCase.findById(serviceId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(serviceId, result.get().getId());
        assertEquals(serviceName, result.get().getName());
        assertEquals(serviceDescription, result.get().getDescription());
        assertEquals(price, result.get().getPrice());
        assertEquals(duration, result.get().getDuration());
        assertEquals(eventId, result.get().getEventId());

        // Verify
        verify(serviceRepository, times(1)).findByServiceId(serviceId);
    }

    @Test
    void shouldReturnEmptyWhenServiceNotFound() {
        // Arrange
        Long missingServiceId = 100L;
        when(serviceRepository.findByServiceId(missingServiceId)).thenReturn(Optional.empty());

        // Act
        Optional<Service> result = getServiceUseCase.findById(missingServiceId);

        // Assert
        assertFalse(result.isPresent(), "Service should not be found");
        verify(serviceRepository, times(1)).findByServiceId(missingServiceId);
    }
}