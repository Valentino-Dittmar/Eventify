package individual.configuration.security.token;

import individual.persistence.entity.Role;

public interface AccessToken {
    String getSubject();

    Long getUserId();
    Role getRole();
    boolean hasRole(Role role);

}
