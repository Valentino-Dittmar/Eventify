package individual.domain.service;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateServiceRequest {
    private Long serviceId;
    @NotBlank
    private String name;
    private String description;
}
