package individual.domain.service;


import jakarta.validation.constraints.NotBlank;
public class UpdateServiceRequest {
    private Long id;
    @NotBlank
    private String name;
}
