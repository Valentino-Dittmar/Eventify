package individual.business.implementation.Event;

import individual.business.implementation.AttendEventUseCaseImpl;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendEventUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttendEventUseCaseImpl attendEventUseCase;

    @Test
    void shouldAttendEventSuccessfully() {
        // Arrange
        Long eventId = 1L;
        Long userId = 2L;

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setAttendants(new ArrayList<>());

        UserEntity user = new UserEntity();
        user.setUserId(userId);
        //when=mock response
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        attendEventUseCase.attendEvent(eventId, userId);

        // Assert
        assertTrue(event.getAttendants().contains(user));
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        Long eventId = 1L;
        Long userId = 2L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                attendEventUseCase.attendEvent(eventId, userId));

        assertEquals("Event with ID 1 not found", exception.getMessage());
        verify(eventRepository).findById(eventId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long eventId = 1L;
        Long userId = 2L;

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setAttendants(new ArrayList<>());

        //when=mock response
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                attendEventUseCase.attendEvent(eventId, userId));

        assertEquals("User with ID 2 not found", exception.getMessage());
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyAttending() {
        // Arrange
        Long eventId = 1L;
        Long userId = 2L;

        UserEntity user = new UserEntity();
        user.setUserId(userId);

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setAttendants(new ArrayList<>() {{
            add(user);
        }});
        //when=mock response
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                attendEventUseCase.attendEvent(eventId, userId));

        assertEquals("User is already attending this event", exception.getMessage());
        verify(eventRepository).findById(eventId);
        verify(userRepository).findById(userId);
    }
}