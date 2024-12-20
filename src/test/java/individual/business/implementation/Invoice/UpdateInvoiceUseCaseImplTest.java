package individual.business.implementation.Invoice;

import individual.business.implementation.UpdateInvoiceUseCaseImpl;
import individual.domain.invoice.UpdateInvoiceRequest;
import individual.persistence.EventRepository;
import individual.persistence.InvoiceRepository;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.EventEntity;
import individual.persistence.entity.InvoiceEntity;
import individual.persistence.entity.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateInvoiceUseCaseImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private UpdateInvoiceUseCaseImpl updateInvoiceUseCase;

    @Test
    void shouldUpdateInvoiceWithAllFields() {
        // Arrange
        Long invoiceId = 1L;
        Long eventId = 2L;
        List<Long> serviceIds = List.of(3L, 4L);

        InvoiceEntity existingInvoice = InvoiceEntity.builder()
                .invoiceId(invoiceId)
                .totalAmount(BigDecimal.valueOf(200))
                .description("Old Description")
                .issueDate(LocalDate.now().atStartOfDay().minusDays(10))
                .dueDate(LocalDate.now().atStartOfDay().plusDays(10))
                .build();

        EventEntity event = EventEntity.builder().eventId(eventId).build();
        List<ServiceEntity> services = List.of(
                ServiceEntity.builder().serviceId(3L).build(),
                ServiceEntity.builder().serviceId(4L).build()
        );

        UpdateInvoiceRequest request = UpdateInvoiceRequest.builder()
                .invoiceId(invoiceId)
                .totalAmount(BigDecimal.valueOf(500))
                .description("Updated Description")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(30))
                .serviceIds(serviceIds)
                .eventId(eventId)
                .build();

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(existingInvoice);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(serviceRepository.findAllById(serviceIds)).thenReturn(services);
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InvoiceEntity updatedInvoice = updateInvoiceUseCase.updateInvoice(request);

        // Assert
        assertNotNull(updatedInvoice);
        assertEquals(BigDecimal.valueOf(500), updatedInvoice.getTotalAmount());
        assertEquals("Updated Description", updatedInvoice.getDescription());
        assertEquals(LocalDate.now().atStartOfDay(), updatedInvoice.getIssueDate());
        assertEquals(LocalDate.now().atStartOfDay().plusDays(30), updatedInvoice.getDueDate());
        assertEquals(eventId, updatedInvoice.getEvent().getEventId());
        assertEquals(serviceIds, updatedInvoice.getServices().stream().map(ServiceEntity::getServiceId).toList());

        verify(invoiceRepository).findByInvoiceId(invoiceId);
        verify(eventRepository).findById(eventId);
        verify(serviceRepository).findAllById(serviceIds);
        verify(invoiceRepository).save(any(InvoiceEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;
        UpdateInvoiceRequest request = UpdateInvoiceRequest.builder()
                .invoiceId(invoiceId)
                .build();

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                updateInvoiceUseCase.updateInvoice(request));

        assertEquals("Invoice not found", exception.getMessage());
        verify(invoiceRepository).findByInvoiceId(invoiceId);
        verifyNoInteractions(eventRepository, serviceRepository);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        Long invoiceId = 1L;
        Long eventId = 2L;
        UpdateInvoiceRequest request = UpdateInvoiceRequest.builder()
                .invoiceId(invoiceId)
                .eventId(eventId)
                .build();

        InvoiceEntity existingInvoice = InvoiceEntity.builder().invoiceId(invoiceId).build();

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(existingInvoice);
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                updateInvoiceUseCase.updateInvoice(request));

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository).findById(eventId);
        verifyNoInteractions(serviceRepository);
    }

    @Test
    void shouldUpdatePartialFields() {
        // Arrange
        Long invoiceId = 1L;

        InvoiceEntity existingInvoice = InvoiceEntity.builder()
                .invoiceId(invoiceId)
                .totalAmount(BigDecimal.valueOf(200))
                .description("Old Description")
                .issueDate(LocalDate.now().atStartOfDay().minusDays(10))
                .dueDate(LocalDate.now().atStartOfDay().plusDays(10))
                .build();

        UpdateInvoiceRequest request = UpdateInvoiceRequest.builder()
                .invoiceId(invoiceId)
                .description("Partial Update")
                .build();

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(existingInvoice);
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InvoiceEntity updatedInvoice = updateInvoiceUseCase.updateInvoice(request);

        // Assert
        assertNotNull(updatedInvoice);
        assertEquals(BigDecimal.valueOf(200), updatedInvoice.getTotalAmount());
        assertEquals("Partial Update", updatedInvoice.getDescription());
        assertEquals(LocalDate.now().atStartOfDay().minusDays(10), updatedInvoice.getIssueDate());
        assertEquals(LocalDate.now().atStartOfDay().plusDays(10), updatedInvoice.getDueDate());

        verify(invoiceRepository).findByInvoiceId(invoiceId);
        verify(invoiceRepository).save(any(InvoiceEntity.class));
        verifyNoInteractions(eventRepository, serviceRepository);
    }
}