package individual.domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {
    private long userId;
    private BigDecimal totalAmount;
    private String description;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private List<Long> serviceIds;
    private Long eventId;
}
