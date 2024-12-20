package individual.business.implementation;

import individual.business.GetInvoiceUseCase;
import individual.domain.invoice.Invoice;
import individual.persistence.InvoiceRepository;
import individual.persistence.entity.InvoiceEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetInvoiceUseCaseImpl implements GetInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;

    @Override
    public Optional<Invoice> getInvoiceById(Long invoiceId) {
        Optional<InvoiceEntity> invoiceEntityOptional = Optional.ofNullable(invoiceRepository.findByInvoiceId(invoiceId));

        if (invoiceEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Invoice not found");
        }
        return invoiceEntityOptional.map(InvoiceConverter::convert);
    }
}