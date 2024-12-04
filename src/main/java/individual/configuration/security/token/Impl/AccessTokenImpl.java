package individual.configuration.security.token.Impl;
import individual.configuration.security.token.AccessToken;
import individual.persistence.entity.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final Long userId;
    private final String subject;
    private final Role role;

    public AccessTokenImpl(String subject,Long userId, Role role) {
        this.subject = subject;
        this.role = role;
        this.userId = userId;
    }

    @Override
    public boolean hasRole(Role role) {
        return this.role == role;
    }
}
