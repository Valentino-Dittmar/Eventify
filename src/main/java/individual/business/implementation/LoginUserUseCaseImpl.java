package individual.business.implementation;

import individual.business.LoginUserUseCase;
import individual.configuration.security.token.AccessToken;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.domain.User.UserLoginRequest;
import individual.persistence.UserRepository;
import individual.persistence.entity.AuthProvider;
import individual.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AccessTokenEncoder accessTokenEncoder;
    AccessToken accessToken;

    @Override
    public String login(UserLoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail());

        if (!user.getProvider().equals(AuthProvider.LOCAL)) {
            throw new IllegalStateException("This account is linked to Google. Please log in with Google.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        AccessTokenImpl accessToken = new AccessTokenImpl(

                user.getEmail(),
                user.getUserId(),
                user.getRole()
        );

        return accessTokenEncoder.encode(accessToken);
    }
}
