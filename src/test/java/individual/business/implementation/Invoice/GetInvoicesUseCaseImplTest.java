package individual.business.implementation.Invoice;

import individual.business.implementation.GetInvoicesUseCaseImpl;
import individual.domain.invoice.GetAllInvoicesResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetInvoicesUseCaseImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private GetInvoicesUseCaseImpl getInvoicesUseCase;

    @Test
    void shouldReturnAllInvoices() {
        // Arrange
        ServiceEntity service1 = ServiceEntity.builder()
                .serviceId(1L)
                .name("Service A")
                .price(BigDecimal.valueOf(100))
                .build();

        ServiceEntity service2 = ServiceEntity.builder()
                .serviceId(2L)
                .name("Service B")
                .price(BigDecimal.valueOf(200))
                .build();
        EventEntity event1 = EventEntity.builder()
                .eventId(2L)
                .build();
        EventEntity event2 = EventEntity.builder()
                .eventId(3L)
                .build();
        InvoiceEntity invoiceEntity1 = InvoiceEntity.builder()
                .invoiceId(10L)
                .totalAmount(BigDecimal.valueOf(300))
                .description("Test Invoice 1")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(10))
                .services(List.of(service1))
                .event(event1)
                .build();

        InvoiceEntity invoiceEntity2 = InvoiceEntity.builder()
                .invoiceId(11L)
                .totalAmount(BigDecimal.valueOf(400))
                .description("Test Invoice 2")
                .issueDate(LocalDate.now().atStartOfDay())
                .dueDate(LocalDate.now().atStartOfDay().plusDays(20))
                .services(List.of(service2))
                .event(event2)
                .build();

        when(invoiceRepository.findAllWithServices()).thenReturn(List.of(invoiceEntity1, invoiceEntity2));

        // Act
        GetAllInvoicesResponse response = getInvoicesUseCase.getAllInvoices();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getInvoices().size());

        Invoice invoice1 = response.getInvoices().get(0);
        assertEquals(10L, invoice1.getId());
        assertEquals(BigDecimal.valueOf(300), invoice1.getAmount());
        assertEquals("Test Invoice 1", invoice1.getDescription());
        assertEquals(LocalDate.now().atStartOfDay(), invoice1.getIssueDate());
        assertEquals(LocalDate.now().atStartOfDay().plusDays(10), invoice1.getDueDate());

        Invoice invoice2 = response.getInvoices().get(1);
        assertEquals(11L, invoice2.getId());
        assertEquals(BigDecimal.valueOf(400), invoice2.getAmount());
        assertEquals("Test Invoice 2", invoice2.getDescription());
        assertEquals(LocalDate.now().atStartOfDay(), invoice2.getIssueDate());
        assertEquals(LocalDate.now().atStartOfDay().plusDays(20), invoice2.getDueDate());

        // Verify mock interaction
        verify(invoiceRepository, times(1)).findAllWithServices();
    }

    @Test
    void shouldReturnEmptyListWhenNoInvoices() {
        // Arrange
        when(invoiceRepository.findAllWithServices()).thenReturn(List.of());

        // Act
        GetAllInvoicesResponse response = getInvoicesUseCase.getAllInvoices();

        // Assert
        assertNotNull(response);
        assertTrue(response.getInvoices().isEmpty());

        // Verify mock interaction
        verify(invoiceRepository, times(1)).findAllWithServices();
    }
}