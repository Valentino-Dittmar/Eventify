package individual.domain.User;


import individual.persistence.entity.AuthProvider;
import individual.persistence.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String name;
    private String profilePicture;
    private AuthProvider provider;
    private String providerId;
    private LocalDateTime createdAt;
    private Role role;
}
