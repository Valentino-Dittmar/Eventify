package individual.persistence.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceEntity {
    private Long serviceId;
    private String name;
    private String description;
}
