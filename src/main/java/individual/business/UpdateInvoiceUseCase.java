package individual.business;

import individual.domain.invoice.UpdateInvoiceRequest;
import individual.persistence.entity.InvoiceEntity;
import org.springframework.transaction.annotation.Transactional;

public interface UpdateInvoiceUseCase {

    @Transactional
    InvoiceEntity updateInvoice(UpdateInvoiceRequest request);
}
