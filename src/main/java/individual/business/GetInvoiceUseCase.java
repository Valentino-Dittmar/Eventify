package individual.business;

import individual.domain.invoice.Invoice;

import java.util.Optional;

public interface GetInvoiceUseCase {

    Optional<Invoice> getInvoiceById(Long invoiceId);
}
