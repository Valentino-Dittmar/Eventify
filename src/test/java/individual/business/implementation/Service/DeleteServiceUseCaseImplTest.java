package individual.business.implementation.Service;

import individual.business.exception.ServiceNotFoundException;
import individual.business.implementation.DeleteServiceUseCaseImpl;
import individual.persistence.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class DeleteServiceUseCaseImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private DeleteServiceUseCaseImpl deleteServiceUseCase;

    @Test
    void shouldDeleteServiceWhenServiceExists() {
        // Arrange
        Long serviceId = 1L;

        when(serviceRepository.existsById(serviceId)).thenReturn(true);

        // Act
        deleteServiceUseCase.deleteService(serviceId);

        // Assert
        verify(serviceRepository, times(1)).deleteById(serviceId);
    }

    @Test
    void shouldThrowExceptionWhenServiceDoesNotExist() {
        // Arrange
        Long missingServiceId = 100L;

        when(serviceRepository.existsById(missingServiceId)).thenReturn(false);

        // Act & Assert
        ServiceNotFoundException exception = assertThrows(ServiceNotFoundException.class, () ->
                deleteServiceUseCase.deleteService(missingServiceId));

        assertEquals("Service with ID 100 not found", exception.getMessage());
        verify(serviceRepository, never()).deleteById(missingServiceId);
    }
}