package individual.domain.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GetAllServicesResponse {
    private List<Service> services;
}
