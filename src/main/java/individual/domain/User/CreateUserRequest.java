package individual.domain.User;

import individual.persistence.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Role role = Role.CUSTOMER;
}
