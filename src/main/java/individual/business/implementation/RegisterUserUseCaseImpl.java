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
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@Transactional
@Primary
@AllArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
        // Generate the token
        String token = accessTokenEncoder.encode(accessToken);

        return token;
    }

    @Transactional
    @Override
    public String register(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("This email is already registered. Please log in.");
        }


        String hashedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .name(request.getName())
                .role(request.getRole())
                .provider(AuthProvider.LOCAL) // Default provider for local registration
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        // Generate access token for the newly registered user
        AccessTokenImpl accessToken = new AccessTokenImpl(
                newUser.getEmail(),
                newUser.getUserId(),
                newUser.getRole()
        );

        return accessTokenEncoder.encode(accessToken);
    }


}
