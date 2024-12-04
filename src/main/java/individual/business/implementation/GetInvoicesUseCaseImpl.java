package individual.business.implementation;


import individual.business.GetInvoicesUseCase;
import individual.domain.invoice.GetAllInvoicesResponse;
import individual.domain.invoice.Invoice;
import individual.persistence.InvoiceRepository;
import individual.persistence.entity.InvoiceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetInvoicesUseCaseImpl implements GetInvoicesUseCase {

    private final InvoiceRepository invoiceRepository;

    @Override
    public GetAllInvoicesResponse getAllInvoices() {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findAllWithServices();

        List<Invoice> invoices = invoiceEntities.stream()
                .map(InvoiceConverter::convert)
                .collect(Collectors.toList());

        return GetAllInvoicesResponse.builder()
                .invoices(invoices)
                .build();
    }
}
