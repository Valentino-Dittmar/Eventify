package individual.controller;

import individual.business.*;
import individual.configuration.security.token.AccessTokenDecoder;
import individual.domain.User.User;
import individual.domain.event.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {
    private final GetEventUseCase getEventUseCase;
    private final GetEventsUseCase getEventsUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final AttendEventUseCase attendEventUseCase;
    private final AccessTokenDecoder accessTokenDecoder;
    private final GetEventAttendantsUseCase getEventAttendantsUseCase;
    private final GetAttendingEventsByUserIdUseCase getAttendingEventsByUserIdUseCase;


    @GetMapping("{id}")
    public ResponseEntity<Event> getEvent(@PathVariable(value = "id") final long id) {
        final Event event = getEventUseCase.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(event);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PreAuthorize("hasAnyRole('EVENT_MANAGER', 'CUSTOMER', 'VENDOR')")
    @GetMapping
    public ResponseEntity<GetAllEventsResponse> getAllEvents(
            @RequestParam(required = false) String title
    ) {
        GetAllEventsRequest request = GetAllEventsRequest.builder()
                .title(title)
                .build();

        GetAllEventsResponse response = getEventsUseCase.getAllEvents(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long eventId) {
        deleteEventUseCase.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    @PostMapping
    public ResponseEntity<CreateEventResponse> createEvent(@RequestBody @Valid CreateEventRequest request) {
        CreateEventResponse response = createEventUseCase.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable("id") long id,
                                            @RequestBody @Valid UpdateEventRequest request) {
        request.setEventId(id);
        updateEventUseCase.updateEvent(request);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("{eventId}/attend")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> attendEvent(@PathVariable("eventId") long eventId, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "").trim();

        Long userId = accessTokenDecoder.decode(token).getUserId();

        attendEventUseCase.attendEvent(eventId, userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("{eventId}/attendants")
    @PreAuthorize("hasAnyRole('EVENT_MANAGER', 'CUSTOMER', 'VENDOR')")
    public ResponseEntity<List<User>> getEventAttendants(@PathVariable("eventId") long eventId) {
        List<User> attendants = getEventAttendantsUseCase.getAttendantsByEventId(eventId);
        return ResponseEntity.ok(attendants);
    }
    @GetMapping("/attending")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Event>> getAttendingEvents(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "").trim();
            Long userId = accessTokenDecoder.decode(token).getUserId();
            List<Event> events = getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
