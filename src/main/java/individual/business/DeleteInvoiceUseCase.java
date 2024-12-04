package individual.business;

import org.springframework.stereotype.Service;

@Service
public interface DeleteInvoiceUseCase {
    void deleteInvoice(long invoiceId);
}
