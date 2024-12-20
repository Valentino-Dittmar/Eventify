package individual.business.implementation.Auth;

import static org.junit.jupiter.api.Assertions.*;

import individual.business.implementation.LoginUserUseCaseImpl;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.domain.User.UserLoginRequest;
import individual.persistence.UserRepository;
import individual.persistence.entity.AuthProvider;
import individual.persistence.entity.Role;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginUserUseCaseImpl loginUserUseCase;

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        String accessToken = "mockAccessToken";

        UserEntity userEntity = UserEntity.builder()
                .userId(1L)
                .email(email)
                .password(encodedPassword)
                .provider(AuthProvider.LOCAL)
                .role(Role.CUSTOMER)
                .build();

        UserLoginRequest request = UserLoginRequest.builder()
                .email(email)
                .password(rawPassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn(accessToken);

        // Act
        String result = loginUserUseCase.login(request);

        // Assert
        assertNotNull(result);
        assertEquals(accessToken, result);
        //Verfy
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(accessTokenEncoder, times(1)).encode(any(AccessTokenImpl.class));
    }

    @Test
    void shouldThrowExceptionForNonLocalAuthProvider() {
        // Arrange
        String email = "google@example.com";

        UserEntity userEntity = UserEntity.builder()
                .userId(1L)
                .email(email)
                .password("password123")
                .provider(AuthProvider.GOOGLE)
                .role(Role.CUSTOMER)
                .build();

        UserLoginRequest request = UserLoginRequest.builder()
                .email(email)
                .password("password123")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () ->
                loginUserUseCase.login(request));
        assertEquals("This account is linked to Google. Please log in with Google.", exception.getMessage());
       //Verify
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoInteractions(passwordEncoder, accessTokenEncoder);
    }

    @Test
    void shouldThrowExceptionForInvalidCredentials() {
        // Arrange
        String email = "test@example.com";
        String rawPassword = "wrongPassword";

        UserEntity userEntity = UserEntity.builder()
                .userId(1L)
                .email(email)
                .password("encodedPassword123")
                .provider(AuthProvider.LOCAL)
                .role(Role.CUSTOMER)
                .build();

        UserLoginRequest request = UserLoginRequest.builder()
                .email(email)
                .password(rawPassword)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        when(passwordEncoder.matches(rawPassword, userEntity.getPassword())).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                loginUserUseCase.login(request));
        assertEquals("Invalid credentials", exception.getMessage());

        //Verify
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, userEntity.getPassword());
        verifyNoInteractions(accessTokenEncoder);
    }
}