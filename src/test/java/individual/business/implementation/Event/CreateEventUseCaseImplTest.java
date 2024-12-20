package individual.business.implementation.Event;

import individual.business.implementation.CreateEventUseCaseImpl;
import individual.domain.event.CreateEventRequest;
import individual.domain.event.CreateEventResponse;
import individual.persistence.EventRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateEventUseCaseImpl createEventUseCase;

    @Test
    void shouldCreateEventSuccessfully() {
        // Arrange
        Long creatorId = 1L;
        List<Long> attendantIds = List.of(2L, 3L);
        LocalDateTime eventDate = LocalDateTime.now();

        UserEntity creator = UserEntity.builder().userId(creatorId).build();
        List<UserEntity> attendants = List.of(
                UserEntity.builder().userId(2L).build(),
                UserEntity.builder().userId(3L).build()
        );

        EventEntity savedEvent = EventEntity.builder().eventId(4L).build();

        CreateEventRequest request = CreateEventRequest.builder()
                .title("Team Meeting")
                .description("Discuss project updates")
                .location("Conference Room")
                .date(eventDate)
                .creatorId(creatorId)
                .attendantIds(attendantIds)
                .build();

        when(userRepository.findByUserId(creatorId)).thenReturn(creator);
        when(userRepository.findAllById(attendantIds)).thenReturn(attendants);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEvent);

        // Act
        CreateEventResponse response = createEventUseCase.createEvent(request);

        // Assert
        assertNotNull(response);
        assertEquals(4L, response.getEventId());
        verify(userRepository, times(1)).findByUserId(creatorId);
        verify(userRepository, times(1)).findAllById(attendantIds);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatorNotFound() {
        // Arrange
        Long creatorId = 1L;

        CreateEventRequest request = CreateEventRequest.builder()
                .title("Team Meeting")
                .description("Discuss project updates")
                .location("Conference Room")
                .date(LocalDateTime.now())
                .creatorId(creatorId)
                .build();

        when(userRepository.findByUserId(creatorId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                createEventUseCase.createEvent(request));
        assertEquals("Creator with ID 1 not found", exception.getMessage());
        verify(userRepository, times(1)).findByUserId(creatorId);
        verifyNoInteractions(eventRepository);
    }

    @Test
    void shouldCreateEventWithNoAttendants() {
        // Arrange
        Long creatorId = 1L;
        LocalDateTime eventDate = LocalDateTime.now();

        UserEntity creator = UserEntity.builder().userId(creatorId).build();
        EventEntity savedEvent = EventEntity.builder().eventId(4L).build();

        CreateEventRequest request = CreateEventRequest.builder()
                .title("Team Meeting")
                .description("Discuss project updates")
                .location("Conference Room")
                .date(eventDate)
                .creatorId(creatorId)
                .build(); // No attendants

        when(userRepository.findByUserId(creatorId)).thenReturn(creator);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEvent);

        // Act & Assert
        CreateEventResponse response = createEventUseCase.createEvent(request);

        assertNotNull(response);
        assertEquals(4L, response.getEventId());
        verify(userRepository, times(1)).findByUserId(creatorId);
        verify(eventRepository, times(1)).save(any(EventEntity.class));
        verifyNoMoreInteractions(userRepository);
    }
}