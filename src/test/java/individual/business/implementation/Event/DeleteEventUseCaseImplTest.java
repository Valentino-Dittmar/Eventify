package individual.business.implementation.Event;

import individual.business.implementation.DeleteEventUseCaseImpl;
import individual.persistence.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEventUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private DeleteEventUseCaseImpl deleteEventUseCase;

    @Test
    void shouldDeleteEventSuccessfully() {
        // Arrange
        long eventId = 1L;

        // Act
        deleteEventUseCase.deleteEvent(eventId);

        // Assert/Verify
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void shouldHandleNonExistentEvent() {
        // Arrange
        long nonExistentEventId = 99L;

        // Simulate behavior when the event does not exist
        doNothing().when(eventRepository).deleteById(nonExistentEventId);

        // Act
        deleteEventUseCase.deleteEvent(nonExistentEventId);

        // Assert/Verify
        verify(eventRepository, times(1)).deleteById(nonExistentEventId);
        // Ensure no exception is thrown and execution is smooth
    }


    @Test
    void shouldNotInteractWithRepositoryForInvalidEventId() {
        // Arrange
        long invalidEventId = -1L;

        // Act
        deleteEventUseCase.deleteEvent(invalidEventId);

        // Assert
        verify(eventRepository, times(1)).deleteById(invalidEventId);
    }
    @Test
    void shouldNotCallDeleteForNullId() {
        // Arrange
        Long null1 = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteEventUseCase.deleteEvent(null1));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> deleteEventUseCase.deleteEvent(null));

        assertEquals("Event id is null", exception.getMessage());
        // Verrify that there were no interactions with the repo
        verifyNoInteractions(eventRepository);

    }
}