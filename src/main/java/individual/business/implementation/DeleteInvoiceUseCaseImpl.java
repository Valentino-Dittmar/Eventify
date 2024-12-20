package individual.business.implementation;

import individual.business.DeleteInvoiceUseCase;
import individual.persistence.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DeleteInvoiceUseCaseImpl implements DeleteInvoiceUseCase {
    InvoiceRepository invoiceRepository;
    @Transactional
    @Override
    public void deleteInvoice(long invoiceId)
    {
        this.invoiceRepository.deleteById(invoiceId);
    }
}
