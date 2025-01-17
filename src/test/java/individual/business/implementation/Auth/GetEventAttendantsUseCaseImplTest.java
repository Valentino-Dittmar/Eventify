package individual.business.implementation.Auth;

import individual.business.implementation.GetEventAttendantsUseCaseImpl;
import individual.domain.User.User;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventAttendantsUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private GetEventAttendantsUseCaseImpl getEventAttendantsUseCase;

    @Test
    void shouldReturnListOfAttendants() {
        // Arrange
        Long eventId = 1L;

        UserEntity user1 = new UserEntity();
        user1.setUserId(1L);
        user1.setName("John Doe");

        UserEntity user2 = new UserEntity();
        user2.setUserId(2L);
        user2.setName("Jane Doe");

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setAttendants(new ArrayList<>(List.of(user1, user2)));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        List<User> attendants = getEventAttendantsUseCase.getAttendantsByEventId(eventId);

        // Assert
        assertNotNull(attendants);
        assertEquals(2, attendants.size());
        assertEquals("John Doe", attendants.get(0).getName());
        assertEquals("Jane Doe", attendants.get(1).getName());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                getEventAttendantsUseCase.getAttendantsByEventId(eventId));

        assertEquals("Event with ID 1 not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void shouldReturnEmptyListWhenNoAttendants() {
        // Arrange
        Long eventId = 1L;

        EventEntity event = new EventEntity();
        event.setEventId(eventId);
        event.setAttendants(new ArrayList<>()); // No attendants

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        List<User> attendants = getEventAttendantsUseCase.getAttendantsByEventId(eventId);

        // Assert
        assertNotNull(attendants);
        assertTrue(attendants.isEmpty());
        verify(eventRepository, times(1)).findById(eventId);
    }
}