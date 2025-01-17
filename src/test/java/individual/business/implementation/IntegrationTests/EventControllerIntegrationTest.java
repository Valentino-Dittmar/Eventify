package individual.business.implementation.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import individual.business.*;
//import individual.business.implementation.TestSecurityConfig;
import individual.configuration.security.token.AccessToken;
import individual.configuration.security.token.AccessTokenDecoder;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.domain.User.User;
import individual.domain.event.*;
import individual.persistence.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@Import(TestSecurityConfig.class)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.datasource.username=sa",
//        "spring.datasource.password=",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.jpa.show-sql=true",
//        "spring.jpa.properties.hibernate.format_sql=true"
//})
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetEventUseCase getEventUseCase;

    @MockBean
    private GetEventsUseCase getEventsUseCase;

    @MockBean
    private DeleteEventUseCase deleteEventUseCase;

    @MockBean
    private CreateEventUseCase createEventUseCase;

    @MockBean
    private UpdateEventUseCase updateEventUseCase;

    @MockBean
    private AttendEventUseCase attendEventUseCase;

    @MockBean
    private AccessTokenDecoder accessTokenDecoder;

    @MockBean
    private AccessTokenEncoder accessTokenEncoder;
    @MockBean
    private GetEventAttendantsUseCase getEventAttendantsUseCase;

    @MockBean
    private GetAttendingEventsByUserIdUseCase getAttendingEventsByUserIdUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnEventWhenFound() throws Exception {
        // Arrange
        long eventId = 1L;
        Event event = Event.builder()
                .eventId(eventId)
                .title("Test Event")
                .description("Test Description")
                .build();

        when(getEventUseCase.getEventById(eventId)).thenReturn(event);

        // Act & Assert
        mockMvc.perform(get("/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(getEventUseCase, times(1)).getEventById(eventId);
    }

    @Test
    void shouldReturnNotFoundWhenEventNotFound() throws Exception {
        // Arrange
        long eventId = 1L;

        when(getEventUseCase.getEventById(eventId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/events/{id}", eventId))
                .andExpect(status().isNotFound());

        verify(getEventUseCase, times(1)).getEventById(eventId);
    }
//WORKS BUT NEEDS PreAuthorize to be stopped in the controller
//    @Test
//    void shouldReturnAllEvents() throws Exception {
//        // Arrange
//        GetAllEventsResponse response = GetAllEventsResponse.builder()
//                .events(List.of(
//                        Event.builder().eventId(1L).title("Event 1").build(),
//                        Event.builder().eventId(2L).title("Event 2").build()
//                ))
//                .build();
//
//        when(getEventsUseCase.getAllEvents(any())).thenReturn(response);
//
//        // Act & Assert
//        mockMvc.perform(get("/events"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.events").isArray())
//                .andExpect(jsonPath("$.events[0].title").value("Event 1"))
//                .andExpect(jsonPath("$.events[1].title").value("Event 2"));
//
//        verify(getEventsUseCase, times(1)).getAllEvents(any());
//    }

    @Test
    void shouldDeleteEventSuccessfully() throws Exception {
        // Arrange
        long eventId = 1L;

        doNothing().when(deleteEventUseCase).deleteEvent(eventId);

        // Act & Assert
        mockMvc.perform(delete("/events/{eventId}", eventId))
                .andExpect(status().isNoContent());

        verify(deleteEventUseCase, times(1)).deleteEvent(eventId);
    }
//WORKS BUT NEEDS PreAuthorize to be stopped in the controller
//    @Test
//    void shouldCreateEventSuccessfully() throws Exception {
//        // Arrange
//        CreateEventRequest request = CreateEventRequest.builder()
//                .title("New Event")
//                .description("New Description")
//                .build();
//
//        CreateEventResponse response = CreateEventResponse.builder()
//                .eventId(1L)
//                .build();
//
//        when(createEventUseCase.createEvent(any())).thenReturn(response);
//
//        // Act & Assert
//        mockMvc.perform(post("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.eventId").value(1L));
//
//        verify(createEventUseCase, times(1)).createEvent(any(CreateEventRequest.class));
//    }

    @Test
    void shouldUpdateEventSuccessfully() throws Exception {
        // Arrange
        long eventId = 1L;
        long userId = 123L;
        String token = "test-token";
        String subject = "test-subject";
        Role role = Role.CUSTOMER;
        UpdateEventRequest request = UpdateEventRequest.builder()
                .title("Updated Event")
                .description("Updated Description")
                .build();
        // Mock AccessTokenImpl
        AccessTokenImpl mockAccessToken = new AccessTokenImpl(subject, userId, role);

        when(accessTokenDecoder.decode(token)).thenReturn(mockAccessToken);

        doNothing().when(updateEventUseCase).updateEvent(any());

        // Act & Assert
        mockMvc.perform(put("/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(updateEventUseCase, times(1)).updateEvent(any(UpdateEventRequest.class));
    }

    @Test
    void shouldAttendEventSuccessfully() throws Exception {
        // Arrange
        long eventId = 1L;
        long userId = 123L;
        String token = "test-token";
        String subject = "test-subject";
        Role role = Role.CUSTOMER;

        AccessTokenImpl mockAccessToken = new AccessTokenImpl(subject, userId, role);

        when(accessTokenDecoder.decode(token)).thenReturn(mockAccessToken);
        doNothing().when(attendEventUseCase).attendEvent(eventId, userId);

        // Act & Assert
        mockMvc.perform(post("/events/{eventId}/attend", eventId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(attendEventUseCase, times(1)).attendEvent(eventId, userId);
    }

    @Test
    void shouldReturnAttendantsSuccessfully() throws Exception {
        // Arrange
        long userId = 123L;
        String token = "test-token";
        String subject = "test-subject";
        Role role = Role.CUSTOMER;
        List<Event> events = Arrays.asList(
                Event.builder().eventId(1L).title("Event 1").build(),
                Event.builder().eventId(2L).title("Event 2").build()
        );

        AccessTokenImpl mockAccessToken = new AccessTokenImpl(subject, userId, role);

        when(accessTokenDecoder.decode(token)).thenReturn(mockAccessToken);

        when(getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId)).thenReturn(events);

        // Act & Assert
        mockMvc.perform(get("/events/attending")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].title").value("Event 2"));

        verify(getAttendingEventsByUserIdUseCase, times(1)).getAttendingEventsByUserId(userId);
    }
//WORKS BUT NEEDS PreAuthorize to be stopped in the controller
//    @Test
//    void shouldReturnAttendingEventsSuccessfully() throws Exception {
//        // Arrange
//        long userId = 123L;
//        String token = "test-token";
//        List<Event> events = Arrays.asList(
//                Event.builder().eventId(1L).title("Event 1").build(),
//                Event.builder().eventId(2L).title("Event 2").build()
//        );
//
//        when(accessTokenDecoder.decode(token)).thenReturn((AccessToken) Map.of("userId", userId));
//
//        when(getAttendingEventsByUserIdUseCase.getAttendingEventsByUserId(userId)).thenReturn(events);
//
//        // Act & Assert
//        mockMvc.perform(get("/events/attending")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title").value("Event 1"))
//                .andExpect(jsonPath("$[1].title").value("Event 2"));
//
//        verify(getAttendingEventsByUserIdUseCase, times(1)).getAttendingEventsByUserId(userId);
//    }
}
