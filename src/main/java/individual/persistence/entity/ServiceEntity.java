package individual.persistence.entity;

import lombok.Data;

@Data
public class ServiceEntity {
    private Long serviceId;
    private String name;
    private String description;
}
