package individual.business.implementation.Event;

import individual.business.implementation.UpdateEventUseCaseImpl;
import individual.domain.event.UpdateEventRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEventUseCaseImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateEventUseCaseImpl updateEventUseCase;

    @Test
    void shouldUpdateAllFieldsSuccessfully() {
        // Arrange
        Long eventId = 1L;
        Long creatorId = 2L;
        List<Long> attendantIds = List.of(3L, 4L);
        EventEntity existingEvent = EventEntity.builder()
                .eventId(eventId)
                .title("Old Title")
                .description("Old Description")
                .location("Old Location")
                .date(LocalDateTime.of(2023, 12, 25, 15, 0))
                .creator(UserEntity.builder().userId(5L).build())
                .attendants(List.of())
                .build();

        UserEntity newCreator = UserEntity.builder().userId(creatorId).build();
        List<UserEntity> attendants = List.of(
                UserEntity.builder().userId(3L).build(),
                UserEntity.builder().userId(4L).build()
        );

        UpdateEventRequest request = UpdateEventRequest.builder()
                .eventId(eventId)
                .title("New Title")
                .description("New Description")
                .location("New Location")
                .date(LocalDateTime.of(2024, 1, 1, 12, 0))
                .creatorId(creatorId)
                .attendantIds(attendantIds)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(newCreator));
        when(userRepository.findAllById(attendantIds)).thenReturn(attendants);

        // Act
        updateEventUseCase.updateEvent(request);

        // Assert
        assertEquals("New Title", existingEvent.getTitle());
        assertEquals("New Description", existingEvent.getDescription());
        assertEquals("New Location", existingEvent.getLocation());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), existingEvent.getDate());
        assertEquals(newCreator, existingEvent.getCreator());
        assertEquals(attendants, existingEvent.getAttendants());

        verify(eventRepository, times(1)).findById(eventId);
        verify(userRepository, times(1)).findById(creatorId);
        verify(userRepository, times(1)).findAllById(attendantIds);
        verify(eventRepository, times(1)).save(existingEvent);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        Long eventId = 1L;
        UpdateEventRequest request = UpdateEventRequest.builder()
                .eventId(eventId)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                updateEventUseCase.updateEvent(request));
        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenCreatorNotFound() {
        // Arrange
        Long eventId = 1L;
        Long creatorId = 2L;
        EventEntity existingEvent = EventEntity.builder().eventId(eventId).build();

        UpdateEventRequest request = UpdateEventRequest.builder()
                .eventId(eventId)
                .creatorId(creatorId)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(userRepository.findById(creatorId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                updateEventUseCase.updateEvent(request));
        assertEquals("Creator with ID " + creatorId + " not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
        verify(userRepository, times(1)).findById(creatorId);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void shouldUpdateEventWithPartialFields() {
        // Arrange
        Long eventId = 1L;
        EventEntity existingEvent = EventEntity.builder()
                .eventId(eventId)
                .title("Old Title")
                .description("Old Description")
                .build();

        UpdateEventRequest request = UpdateEventRequest.builder()
                .eventId(eventId)
                .title("New Title")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));

        // Act
        updateEventUseCase.updateEvent(request);

        // Assert
        assertEquals("New Title", existingEvent.getTitle());
        assertEquals("Old Description", existingEvent.getDescription()); // Unchanged
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(existingEvent);
        verifyNoInteractions(userRepository);
    }
}