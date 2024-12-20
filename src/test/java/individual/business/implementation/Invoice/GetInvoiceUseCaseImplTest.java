package individual.business.implementation.Invoice;

import individual.business.implementation.GetInvoiceUseCaseImpl;
import individual.domain.invoice.Invoice;
import individual.persistence.InvoiceRepository;
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
class GetInvoiceUseCaseImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private GetInvoiceUseCaseImpl getInvoiceUseCase;

    @Test
    void shouldReturnInvoiceByIdWhenFound() {
        // Arrange
        Long invoiceId = 1L;
        EventEntity event = EventEntity.builder().eventId(2L).build();
        ServiceEntity service1 = ServiceEntity.builder().serviceId(3L).build();
        ServiceEntity service2 = ServiceEntity.builder().serviceId(4L).build();

        InvoiceEntity invoiceEntity = InvoiceEntity.builder()
                .invoiceId(invoiceId)
                .totalAmount(BigDecimal.valueOf(300))
                .description("Test Invoice")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(30))
                .event(event)
                .services(List.of(service1, service2))
                .build();

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(invoiceEntity);

        // Act
        Optional<Invoice> result = getInvoiceUseCase.getInvoiceById(invoiceId);

        // Assert
        assertTrue(result.isPresent());
        Invoice invoice = result.get();
        assertEquals(invoiceId, invoice.getId());
        assertEquals(BigDecimal.valueOf(300), invoice.getAmount());
        assertEquals("Test Invoice", invoice.getDescription());
        assertEquals(LocalDate.now().atStartOfDay(), invoice.getIssueDate());
        assertEquals(LocalDate.now().atStartOfDay().plusDays(30), invoice.getDueDate());
        assertEquals(2L, invoice.getEventId());
        assertEquals(List.of(3L, 4L), invoice.getServiceIds());

        verify(invoiceRepository, times(1)).findByInvoiceId(invoiceId);
    }

    @Test
    void shouldThrowExceptionWhenInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;

        when(invoiceRepository.findByInvoiceId(invoiceId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getInvoiceUseCase.getInvoiceById(invoiceId);
        });
        assertEquals("Invoice not found", exception.getMessage());

        verify(invoiceRepository, times(1)).findByInvoiceId(invoiceId);
    }
}