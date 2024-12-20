package individual.business.implementation;

import individual.business.RegisterUserUseCase;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.domain.User.CreateUserRequest;
import individual.persistence.UserRepository;
import individual.persistence.entity.AuthProvider;
import individual.persistence.entity.Role;
import individual.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Qualifier("oAuth")
@RequiredArgsConstructor
public class RegisterOAuthUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final AccessTokenEncoder accessTokenEncoder;

    @Transactional
    @Override
    public String processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String providerId = oauth2User.getAttribute("sub");

        if (email == null || name == null) {
            throw new IllegalArgumentException("Google token is missing required information");
        }

        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            user = UserEntity.builder()
                    .email(email)
                    .provider(AuthProvider.GOOGLE)
                    .providerId(providerId)
                    .name(name)
                    .profilePicture(picture)
                    .createdAt(LocalDateTime.now())
                    .role(Role.CUSTOMER)
                    .build();
            user = userRepository.save(user);
        }

        AccessTokenImpl accessToken = new AccessTokenImpl( user.getEmail(), user.getUserId(), user.getRole());

        String token = accessTokenEncoder.encode(accessToken);

        return token;
    }

    @Override
    public String register(CreateUserRequest request) {
        return "";
    }
}
