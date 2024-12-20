package individual.business.implementation.Service;

import individual.business.implementation.CreateServiceUseCaseImpl;
import individual.domain.service.CreateServiceRequest;
import individual.persistence.EventRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private CreateServiceUseCaseImpl createServiceUseCase;

    @Test
    void shouldCreateServiceWithValidEvent() {
        // Arrange
        Long eventId = 1L;
        Long generatedServiceId = 100L;

        CreateServiceRequest createRequest = new CreateServiceRequest(
                "Test Service",
                "This is a test description.",
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1.5),
                eventId
        );

        EventEntity mockEventEntity = new EventEntity();
        mockEventEntity.setEventId(eventId);

        ServiceEntity mockServiceEntity = ServiceEntity.builder()
                .serviceId(generatedServiceId)
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .price(createRequest.getPrice())
                .duration(createRequest.getDuration())
                .event(mockEventEntity)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEventEntity));
        when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(mockServiceEntity);

        // Act
        Long serviceId = createServiceUseCase.createService(createRequest).getServiceId();

        // Assert
        assertNotNull(serviceId);
        assertEquals(generatedServiceId, serviceId);

        // Verify interactions
        verify(eventRepository, times(1)).findById(eventId);
        verify(serviceRepository, times(1)).save(any(ServiceEntity.class));

        // Direct validation of the returned mock
        assertEquals("Test Service", mockServiceEntity.getName());
        assertEquals("This is a test description.", mockServiceEntity.getDescription());
        assertEquals(BigDecimal.valueOf(50.0), mockServiceEntity.getPrice());
        assertEquals(BigDecimal.valueOf(1.5), mockServiceEntity.getDuration());
        assertEquals(mockEventEntity, mockServiceEntity.getEvent());
    }
}