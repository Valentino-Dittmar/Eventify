package individual.business.implementation.Event;

import individual.business.implementation.GetEventsUseCaseImpl;
import individual.domain.event.Event;
import individual.domain.event.GetAllEventsRequest;
import individual.domain.event.GetAllEventsResponse;
import individual.persistence.EventRepository;
import individual.persistence.entity.EventEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventsUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private GetEventsUseCaseImpl getEventsUseCase;

    @Test
    void shouldReturnAllEventsWhenNoTitleIsProvided() {
        // Arrange
        EventEntity event1 = EventEntity.builder()
                .eventId(1L)
                .title("Event One")
                .description("Description One")
                .date(LocalDateTime.now())
                .build();

        EventEntity event2 = EventEntity.builder()
                .eventId(2L)
                .title("Event Two")
                .description("Description Two")
                .date(LocalDateTime.now().plusDays(1))
                .build();

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        GetAllEventsRequest request = GetAllEventsRequest.builder().build();

        // Act
        GetAllEventsResponse response = getEventsUseCase.getAllEvents(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getEvents().size());
        assertEquals("Event One", response.getEvents().get(0).getTitle());
        assertEquals("Event Two", response.getEvents().get(1).getTitle());
        verify(eventRepository).findAll();
    }

    @Test
    void shouldFilterEventsByTitle() {
        // Arrange
        String title = "Event One";
        EventEntity event = EventEntity.builder()
                .eventId(1L)
                .title(title)
                .description("Description One")
                .date(LocalDateTime.now())
                .build();

        when(eventRepository.findAllByTitle(title)).thenReturn(List.of(event));

        GetAllEventsRequest request = GetAllEventsRequest.builder().title(title).build();

        // Act
        GetAllEventsResponse response = getEventsUseCase.getAllEvents(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getEvents().size());
        assertEquals(title, response.getEvents().get(0).getTitle());
        verify(eventRepository).findAllByTitle(title);
    }

    @Test
    void shouldReturnEventsByCreatorId() {
        // Arrange
        Long creatorId = 1L;
        EventEntity event = EventEntity.builder()
                .eventId(1L)
                .title("Creator Event")
                .description("Event by Creator")
                .date(LocalDateTime.now())
                .build();

        when(eventRepository.findByCreatorUserId(creatorId)).thenReturn(List.of(event));

        // Act
        List<Event> events = getEventsUseCase.getEventsByCreator(creatorId);

        // Assert
        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Creator Event", events.get(0).getTitle());
        verify(eventRepository).findByCreatorUserId(creatorId);
    }

    @Test
    void shouldReturnEventById() {
        // Arrange
        Long eventId = 1L;
        EventEntity event = EventEntity.builder()
                .eventId(eventId)
                .title("Specific Event")
                .description("Detailed Description")
                .date(LocalDateTime.now())
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        Event result = getEventsUseCase.getEventById(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getEventId());
        assertEquals("Specific Event", result.getTitle());
        verify(eventRepository).findById(eventId);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFoundById() {
        // Arrange
        Long eventId = 99L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                getEventsUseCase.getEventById(eventId));

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository).findById(eventId);
    }
}