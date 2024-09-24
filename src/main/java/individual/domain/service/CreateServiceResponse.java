package individual.domain.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateServiceResponse {
    private Long serviceId;
}
