package individual.persistence.implementation;
import individual.persistence.ServiceRepository;
import individual.persistence.entity.ServiceEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeServiceRepositoryImpl implements ServiceRepository {
    private static long NEXT_ID = 1;
    private final List<ServiceEntity> savedServices;

    public FakeServiceRepositoryImpl() {
        this.savedServices = new ArrayList<>();
    }

    @Override
    public boolean existsByName(String name) {
        return this.savedServices
                .stream()
                .anyMatch(serviceEntity -> serviceEntity.getName().equals(name));
    }

    @Override
    public ServiceEntity save(ServiceEntity service) {
        if (service.getServiceId() == null) {
            service.setServiceId(NEXT_ID);
            NEXT_ID++;
            this.savedServices.add(service);
        }
        return service;
    }

    @Override
    public void deleteById(long serviceId) {
        this.savedServices.removeIf(serviceEntity -> serviceEntity.getServiceId().equals(serviceId));
    }

    @Override
    public List<ServiceEntity> findAll() {
        return Collections.unmodifiableList(this.savedServices);
    }
    @Override
    public List<ServiceEntity> findAllByName(String name) {
        return Collections.unmodifiableList(savedServices)
                .stream()
                .filter(serviceEntity -> serviceEntity.getName().contains(name))
                .toList();

    }

    @Override
    public Optional<ServiceEntity> findById(long serviceId) {
        return this.savedServices.stream()
                .filter(serviceEntity -> serviceEntity.getServiceId().equals(serviceId))
                .findFirst();
    }
}

