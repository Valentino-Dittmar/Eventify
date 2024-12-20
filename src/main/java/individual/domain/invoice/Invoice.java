package individual.domain.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private Long eventId;
    private List<Long> serviceIds;
}
