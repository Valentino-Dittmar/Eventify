package individual.business.implementation.Service;

import individual.business.implementation.UpdateServiceUseCaseImpl;
import individual.domain.service.UpdateServiceRequest;
import individual.persistence.EventRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private UpdateServiceUseCaseImpl updateServiceUseCase;

    @Test
    void shouldUpdateServiceWhenFound() {
        // Arrange
        Long serviceId = 1L;
        UpdateServiceRequest request = new UpdateServiceRequest(
                serviceId,
                "Updated Name",
                "Updated Description",
                BigDecimal.valueOf(13.00),
                BigDecimal.valueOf(14.00),
                null
        );

        ServiceEntity existingService = ServiceEntity.builder()
                .serviceId(serviceId)
                .name("Old Name")
                .description("Old Description")
                .price(BigDecimal.valueOf(10.00))
                .duration(BigDecimal.valueOf(1.5))
                .build();

        when(serviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(existingService));

        // Act
        updateServiceUseCase.updateService(request);

        // Assert
        verify(serviceRepository, times(1)).save(existingService);

        assertEquals("Updated Name", existingService.getName());
        assertEquals("Updated Description", existingService.getDescription());
        assertEquals(BigDecimal.valueOf(13.00), existingService.getPrice());
        assertEquals(BigDecimal.valueOf(14.00), existingService.getDuration());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEventDoesNotExist() {
        // Arrange
        Long serviceId = 1L;
        Long missingEventId = 999L;
        UpdateServiceRequest request = new UpdateServiceRequest(
                serviceId,
                null, //No name
                null, //No Description
                null,   //No price
                null, //No Duration
                missingEventId
        );

        ServiceEntity existingService = ServiceEntity.builder()
                .serviceId(serviceId)
                .build();

        when(serviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(existingService));
        when(eventRepository.findById(missingEventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateServiceUseCase.updateService(request));

        verify(eventRepository, times(1)).findById(missingEventId);
        verify(serviceRepository, never()).save(any());
    }
    @Test
    void shouldUpdateOnlyProvidedFields() {
        // Arrange
        Long serviceId = 1L;
        UpdateServiceRequest request = new UpdateServiceRequest(
                serviceId,
                "Updated Name",
                null, // No description
                BigDecimal.valueOf(20.00),
                null, // No duration
                null  // No event ID
        );

        ServiceEntity existingService = ServiceEntity.builder()
                .serviceId(serviceId)
                .name("Old Name")
                .description("Old Description")
                .price(BigDecimal.valueOf(10.00))
                .duration(BigDecimal.valueOf(1.5))
                .build();

        when(serviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(existingService));

        // Act
        updateServiceUseCase.updateService(request);

        // Assert
        verify(serviceRepository, times(1)).save(existingService);

        // Validate updates
        assertEquals("Updated Name", existingService.getName());
        assertEquals("Old Description", existingService.getDescription()); // Unchanged
        assertEquals(BigDecimal.valueOf(20.00), existingService.getPrice());
        assertEquals(BigDecimal.valueOf(1.5), existingService.getDuration()); // Unchanged
    }
}