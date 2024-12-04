package individual.configuration.security.token;

import individual.persistence.entity.Role;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Long getUserId();
    Role getRole();
    boolean hasRole(Role role);

}
