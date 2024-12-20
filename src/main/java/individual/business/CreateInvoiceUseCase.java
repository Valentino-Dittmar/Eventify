package individual.business;

import individual.domain.invoice.CreateInvoiceRequest;
import individual.domain.invoice.CreateInvoiceResponse;
import org.springframework.transaction.annotation.Transactional;

public interface CreateInvoiceUseCase {
    @Transactional
    CreateInvoiceResponse createInvoice(CreateInvoiceRequest request);
}
