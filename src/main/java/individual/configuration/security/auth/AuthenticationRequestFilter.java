package individual.configuration.security.auth;

import individual.configuration.security.token.AccessToken;
import individual.configuration.security.token.AccessTokenDecoder;
import individual.configuration.security.token.exceptions.InvalidAccessTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class AuthenticationRequestFilter extends OncePerRequestFilter {

    private static final String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";

    @Autowired
    private AccessTokenDecoder accessTokenDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (isPublicPath(request)) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String accessTokenString = requestTokenHeader.substring(7);
            AccessToken accessToken = accessTokenDecoder.decode(accessTokenString);
            setupSpringSecurityContext(accessToken);
            chain.doFilter(request, response);
        } catch (InvalidAccessTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.flushBuffer();
        }
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getServletPath();
        return  path.startsWith("/swagger-ui/");
    }


    private void setupSpringSecurityContext(AccessToken accessToken) {
        GrantedAuthority authority = new SimpleGrantedAuthority(SPRING_SECURITY_ROLE_PREFIX+ accessToken.getRole().toString());

        UserDetails userDetails = new User(
                accessToken.getSubject(),
                "",
                Collections.singletonList(authority));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
