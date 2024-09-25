package individual.persistence.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceEntity {
    public Long serviceId;
    public String name;
    public String description;
}
