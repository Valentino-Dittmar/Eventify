package individual.business.implementation.Event;

import individual.business.implementation.GetEventUseCaseImpl;
import individual.domain.event.Event;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private GetEventUseCaseImpl getEventUseCase;

    @Test
    void shouldReturnEventWhenEventExists() {
        // Arrange
        Long eventId = 1L;
        EventEntity eventEntity = EventEntity.builder()
                .eventId(eventId)
                .title("Test Event")
                .description("Test Description")
                .location("Test Location")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));

        // Act
        Event result = getEventUseCase.getEventById(eventId);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(eventId, result.getEventId(), "Event ID should match");
        assertEquals("Test Event", result.getTitle(), "Title should match");
        assertEquals("Test Description", result.getDescription(), "Description should match");
        assertEquals("Test Location", result.getLocation(), "Location should match");
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void shouldThrowExceptionWhenEventDoesNotExist() {
        // Arrange
        Long nonExistentEventId = 2L;
        when(eventRepository.findById(nonExistentEventId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                getEventUseCase.getEventById(nonExistentEventId));
        assertEquals("Event not found with ID: " + nonExistentEventId, exception.getMessage());
        verify(eventRepository, times(1)).findById(nonExistentEventId);
    }
}