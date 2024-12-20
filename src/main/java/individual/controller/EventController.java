package individual.controller;

import individual.business.*;
import individual.domain.event.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public ResponseEntity<Event> getEvent(@PathVariable(value = "id") final long id) {
        final Event event = getEventUseCase.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(event);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
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
}
