package individual.domain.invoice;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class CreateInvoiceResponse {
    private long invoiceId;
}
