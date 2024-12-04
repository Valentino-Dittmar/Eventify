package individual.domain.User;

import individual.persistence.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOAuthRequest {
    private String email;
    private String providerId;
    private String name;
    private String profilePicture;
    private Role role;
}
