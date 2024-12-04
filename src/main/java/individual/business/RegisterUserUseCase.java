package individual.business;

import individual.domain.User.CreateOAuthRequest;
import individual.domain.User.CreateUserRequest;
import individual.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface RegisterUserUseCase {

    @Transactional
    String processOAuth2User(OAuth2User oauth2User);

    @Transactional
    String register(CreateUserRequest request);
}