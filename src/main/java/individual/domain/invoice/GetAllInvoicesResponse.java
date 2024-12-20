package individual.domain.invoice;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class GetAllInvoicesResponse {
    private List<Invoice> invoices;
}
