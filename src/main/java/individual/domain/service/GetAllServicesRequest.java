package individual.domain.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllServicesRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal duration;
    private Long eventId;
}
