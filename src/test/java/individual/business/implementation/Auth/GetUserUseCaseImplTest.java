package individual.business.implementation.Auth;

import individual.business.implementation.GetUserUseCaseImpl;
import individual.domain.User.User;
import individual.persistence.UserRepository;
import individual.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @Test
    void shouldReturnUserWhenFound() {
        // Arrange
        Long userId = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setName("John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = getUserUseCase.getUserById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals(1L, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = getUserUseCase.getUserById(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }
}