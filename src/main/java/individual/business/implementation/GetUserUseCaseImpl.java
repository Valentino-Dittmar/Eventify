package individual.business.implementation;

import individual.business.GetUserUseCase;
import individual.domain.User.User;
import individual.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@AllArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserConverter::convert);
    }
}
