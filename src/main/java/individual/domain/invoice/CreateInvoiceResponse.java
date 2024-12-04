package individual.domain.invoice;

import individual.persistence.entity.InvoiceEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class CreateInvoiceResponse {
    private long invoiceId;
}
