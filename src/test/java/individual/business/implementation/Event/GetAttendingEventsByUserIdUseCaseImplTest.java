package individual.business.implementation.Event;

import individual.business.implementation.GetAttendingEventsByUserIdUseCaseImpl;
import individual.domain.event.Event;
import individual.persistence.EventAttendantRepository;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAttendingEventsByUserIdUseCaseImplTest {

    @Mock
    private EventAttendantRepository eventAttendantRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private GetAttendingEventsByUserIdUseCaseImpl getAttendingEventsByUserIdUseCase;

    @Test
    void shouldReturnListOfAttendingEvents() {
        // Arrange
        Long userId = 1L;
        List<Long> eventIds = List.of(1L, 2L);

        EventEntity event1 = new EventEntity();
        event1.setEventId(1L);
        event1.setTitle("Event 1");

        EventEntity event2 = new EventEntity();
        event2.setEventId(2L);
        event2.setTitle("Event 2");

        when(eventAttendantRepository.findEventIdsByUserId(userId)).thenReturn(eventIds);
        when(eventRepository.findAllById(eventIds)).thenReturn(List.of(event1, event2));

        // Act
        List<Event> events = getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId);

        // Assert
        assertNotNull(events);
        assertEquals(2, events.size());
        assertEquals("Event 1", events.get(0).getTitle());
        assertEquals("Event 2", events.get(1).getTitle());
        verify(eventAttendantRepository, times(1)).findEventIdsByUserId(userId);
        verify(eventRepository, times(1)).findAllById(eventIds);
    }

    @Test
    void shouldReturnEmptyListWhenNoEventsFound() {
        // Arrange
        Long userId = 1L;

        when(eventAttendantRepository.findEventIdsByUserId(userId)).thenReturn(List.of());

        // Act
        List<Event> events = getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId);

        // Assert
        assertNotNull(events);
        assertTrue(events.isEmpty());
        verify(eventAttendantRepository, times(1)).findEventIdsByUserId(userId);
    }

    @Test
    void shouldHandleEmptyEventIdsReturnedFromRepository() {
        // Arrange
        Long userId = 1L;
        List<Long> eventIds = List.of();

        when(eventAttendantRepository.findEventIdsByUserId(userId)).thenReturn(eventIds);

        // Act
        List<Event> events = getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId);

        // Assert
        assertNotNull(events);
        assertTrue(events.isEmpty());
        verify(eventAttendantRepository, times(1)).findEventIdsByUserId(userId);
    }
}