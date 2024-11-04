package individual.business.implementation;

import individual.business.exception.ServiceNotFoundException;
import individual.domain.service.UpdateServiceRequest;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UpdateServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private UpdateServiceUseCaseImpl updateServiceUseCase;
    @Mock
    private ServiceEntity serviceEntity;

    @Test
    void shouldUpdateServiceWhenFound() {
        // Arrange
        Long serviceId = 1L;
        UpdateServiceRequest request = new UpdateServiceRequest(serviceId, "Updated Name", "Updated Description");

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(serviceEntity));

        // Act
        updateServiceUseCase.updateService(request);

        // Assert
        assertEquals("Updated Name" , request.getName());
        assertEquals("Updated Description" , request.getDescription());

        // Verify
        verify(serviceRepository, times(1)).save(serviceEntity);

    }

    @Test
    void shouldThrowExceptionWhenServiceNotFound() {
        // Arrange
        Long missingServiceId = 100L;
        UpdateServiceRequest updateRequest = new UpdateServiceRequest(missingServiceId, "New Name", "New Description");

        when(serviceRepository.findById(missingServiceId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> updateServiceUseCase.updateService(updateRequest));
        // Verify
        verify(serviceRepository, never()).save(any(ServiceEntity.class));
        verify(serviceRepository, times(1)).findById(missingServiceId);
    }
}

