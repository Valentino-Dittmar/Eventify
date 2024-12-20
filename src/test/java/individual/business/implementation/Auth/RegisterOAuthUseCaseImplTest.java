package individual.business.implementation.Auth;

import individual.business.implementation.RegisterOAuthUseCaseImpl;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.persistence.UserRepository;
import individual.persistence.entity.AuthProvider;
import individual.persistence.entity.Role;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterOAuthUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private RegisterOAuthUseCaseImpl registerOAuthUseCase;

    @Test
    void shouldRegisterNewOAuthUserSuccessfully() {
        // Arrange
        String email = "test@example.com";
        String name = "Test User";
        String providerId = "12345";
        String picture = "http://example.com/picture.jpg";
        String expectedToken = "mockAccessToken";

        when(oAuth2User.getAttribute("email")).thenReturn(email);
        when(oAuth2User.getAttribute("name")).thenReturn(name);
        when(oAuth2User.getAttribute("sub")).thenReturn(providerId);
        when(oAuth2User.getAttribute("picture")).thenReturn(picture);
        when(userRepository.findByEmail(email)).thenReturn(null);

        UserEntity savedUser = UserEntity.builder()
                .email(email)
                .name(name)
                .provider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .profilePicture(picture)
                .createdAt(LocalDateTime.now())
                .role(Role.CUSTOMER)
                .build();
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn(expectedToken);

        // Act
        String result = registerOAuthUseCase.processOAuth2User(oAuth2User);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(accessTokenEncoder, times(1)).encode(any(AccessTokenImpl.class));
    }

    @Test
    void shouldReturnTokenForExistingOAuthUser() {
        // Arrange
        String email = "test@example.com";
        String name = "Test User";
        String providerId = "12345";
        String picture = "http://example.com/picture.jpg";
        String expectedToken = "mockAccessToken";

        // Mocking OAuth2User attributes
        when(oAuth2User.getAttribute("email")).thenReturn(email);
        when(oAuth2User.getAttribute("name")).thenReturn(name);
        when(oAuth2User.getAttribute("sub")).thenReturn(providerId);
        when(oAuth2User.getAttribute("picture")).thenReturn(picture);

        // Mocking the UserRepository behavior
        UserEntity existingUser = UserEntity.builder()
                .userId(1L)
                .email(email)
                .name(name)
                .provider(AuthProvider.GOOGLE)
                .providerId(providerId)
                .profilePicture(picture)
                .role(Role.CUSTOMER)
                .build();
        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        // Mocking the AccessTokenEncoder behavior
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn(expectedToken);

        // Act
        String result = registerOAuthUseCase.processOAuth2User(oAuth2User);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(accessTokenEncoder, times(1)).encode(any(AccessTokenImpl.class));
        verifyNoMoreInteractions(userRepository, accessTokenEncoder); // Ensures no unexpected calls were made
    }

    @Test
    void shouldThrowExceptionWhenEmailOrNameIsMissing() {
        // Arrange
        when(oAuth2User.getAttribute("email")).thenReturn(null);
        when(oAuth2User.getAttribute("name")).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerOAuthUseCase.processOAuth2User(oAuth2User));
        assertEquals("Google token is missing required information", exception.getMessage());

        //Verify
        verifyNoInteractions(userRepository, accessTokenEncoder);

    }
}