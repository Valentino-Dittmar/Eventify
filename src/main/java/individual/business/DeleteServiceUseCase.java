package individual.business;

import org.springframework.transaction.annotation.Transactional;

public interface DeleteServiceUseCase {

    @Transactional
    void deleteService(Long serviceId);
}
