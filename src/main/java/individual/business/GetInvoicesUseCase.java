package individual.business;

import individual.domain.invoice.GetAllInvoicesResponse;
import individual.domain.invoice.Invoice;

import java.util.List;

public interface GetInvoicesUseCase {
    GetAllInvoicesResponse getAllInvoices();
}
