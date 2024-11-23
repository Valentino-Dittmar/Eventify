package individual.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();

    Long getStudentId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
