package individual.domain.invoice;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder

public class UpdateInvoiceRequest {
    private Long invoiceId;
    private BigDecimal totalAmount;
    private String description;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private List<Long> serviceIds;
    private Long eventId;
}
