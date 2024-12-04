package individual.persistence;

import individual.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity findByUserId(long id);
    Boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
    UserEntity findByProviderId(String providerId);
}
