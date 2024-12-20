package individual.persistence;

import individual.persistence.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    boolean existsByName(String name);

    ServiceEntity save(ServiceEntity service);

    void deleteByServiceId(long serviceId);

    List<ServiceEntity> findAll();

    List<ServiceEntity> findAllByName(String name);

    Optional<ServiceEntity> findByServiceId(long serviceId);
}

