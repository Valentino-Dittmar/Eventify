package individual.business.implementation.Invoice;

import individual.business.implementation.CreateInvoiceUseCaseImpl;
import individual.domain.invoice.CreateInvoiceRequest;
import individual.domain.invoice.CreateInvoiceResponse;
import individual.persistence.EventRepository;
import individual.persistence.InvoiceRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.UserRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.InvoiceEntity;
import individual.persistence.entity.ServiceEntity;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateInvoiceUseCaseImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateInvoiceUseCaseImpl createInvoiceUseCase;

    @Test
    void shouldCreateInvoiceWithCorrectFields() {
        // Arrange
        Long userId = 1L;
        Long eventId = 2L;
        List<Long> serviceIds = List.of(3L, 4L);

        UserEntity user = UserEntity.builder().userId(userId).name("Test User").build();
        EventEntity event = EventEntity.builder().eventId(eventId).description("Test Event").build();
        List<ServiceEntity> services = List.of(
                ServiceEntity.builder().serviceId(3L).name("Service A").price(BigDecimal.valueOf(150)).build(),
                ServiceEntity.builder().serviceId(4L).name("Service B").price(BigDecimal.valueOf(350)).build()
        );

        InvoiceEntity savedInvoice = InvoiceEntity.builder()
                .invoiceId(5L)
                .totalAmount(BigDecimal.valueOf(500.0))
                .description("Test Invoice")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(30))
                .user(user)
                .event(event)
                .services(services)
                .build();

        CreateInvoiceRequest request = CreateInvoiceRequest.builder()
                .userId(userId)
                .eventId(eventId)
                .serviceIds(serviceIds)
                .totalAmount(BigDecimal.valueOf(500.0))
                .description("Test Invoice")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(30))
                .build();

        when(eventRepository.findByEventId(eventId)).thenReturn(event);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(serviceRepository.findAllById(serviceIds)).thenReturn(services);
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(savedInvoice);

        // Act
        CreateInvoiceResponse response = createInvoiceUseCase.createInvoice(request);

        // Assert
        assertNotNull(response);
        assertEquals(5L, response.getInvoiceId());

        verify(eventRepository).findByEventId(eventId);
        verify(userRepository).findByUserId(userId);
        verify(serviceRepository).findAllById(serviceIds);
        verify(invoiceRepository).save(any(InvoiceEntity.class));

        // Assert fields
        assertEquals(savedInvoice.getDescription(), "Test Invoice");
        assertEquals(savedInvoice.getTotalAmount(), BigDecimal.valueOf(500.0));
        assertEquals(savedInvoice.getIssueDate(), LocalDate.now().atStartOfDay());
        assertEquals(savedInvoice.getDueDate(), LocalDate.now().atStartOfDay().plusDays(30));
        assertEquals(savedInvoice.getUser(), user);
        assertEquals(savedInvoice.getEvent(), event);
        assertEquals(savedInvoice.getServices(), services);
    }

    @Test
    void shouldFailWhenEventIsMissing() {
        // Arrange
        Long eventId = 2L;

        CreateInvoiceRequest request = CreateInvoiceRequest.builder()
                .eventId(eventId)
                .userId(1L)
                .serviceIds(List.of(3L))
                .build();

        when(eventRepository.findByEventId(eventId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                createInvoiceUseCase.createInvoice(request));
        assertEquals("Event not found", exception.getMessage());

        // Verify
        verify(eventRepository).findByEventId(eventId);
        verifyNoInteractions(userRepository, serviceRepository, invoiceRepository);
    }

    @Test
    void shouldFailWhenUserIsMissing() {
        // Arrange
        Long userId = 1L;

        CreateInvoiceRequest request = CreateInvoiceRequest.builder()
                .eventId(2L)
                .userId(userId)
                .serviceIds(List.of(3L))
                .build();

        when(eventRepository.findByEventId(anyLong())).thenReturn(new EventEntity());
        when(userRepository.findByUserId(userId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                createInvoiceUseCase.createInvoice(request));

        assertEquals("User not found", exception.getMessage());

        //Verify
        verify(userRepository).findByUserId(userId);
        verifyNoInteractions(serviceRepository, invoiceRepository);
    }
}