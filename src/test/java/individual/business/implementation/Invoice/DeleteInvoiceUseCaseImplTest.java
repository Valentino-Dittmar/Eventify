package individual.business.implementation.Invoice;

import individual.business.implementation.DeleteInvoiceUseCaseImpl;
import individual.persistence.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteInvoiceUseCaseImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private DeleteInvoiceUseCaseImpl deleteInvoiceUseCase;

    @Test
    void shouldDeleteInvoiceById() {
        // Arrange
        long invoiceId = 1L;

        // Act
        deleteInvoiceUseCase.deleteInvoice(invoiceId);

        // Assert
        verify(invoiceRepository, times(1)).deleteById(invoiceId);
    }

    @Test
    void shouldHandleNonExistingInvoice() {
        // Arrange
        long nonExistingInvoiceId = 999L;

        doThrow(new IllegalArgumentException("Invoice not found"))
                .when(invoiceRepository).deleteById(nonExistingInvoiceId);

        // Act & Assert
        try {
            deleteInvoiceUseCase.deleteInvoice(nonExistingInvoiceId);
        } catch (Exception e) {
            assertEquals("Invoice not found", e.getMessage());
        }

        verify(invoiceRepository, times(1)).deleteById(nonExistingInvoiceId);
    }
}