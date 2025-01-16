package individual.business;

import individual.domain.User.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GetUserUseCase {
    @Transactional
    Optional<User> getUserById(Long userId);
}
