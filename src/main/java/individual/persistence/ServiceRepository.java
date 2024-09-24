package individual.persistence;

import individual.persistence.entity.ServiceEntity;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {
    boolean existsByName(String name);

    ServiceEntity save(ServiceEntity service);

    void deleteById(long serviceId);

    List<ServiceEntity> findAll();

    List<ServiceEntity> findAllByName(String name);

    Optional<ServiceEntity> findById(long serviceId);
}

