package individual.controller;

import individual.business.GetUserUseCase;
import individual.configuration.security.token.AccessToken;
import individual.configuration.security.token.AccessTokenDecoder;
import individual.configuration.security.token.exceptions.InvalidAccessTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/auth")
public class UserController {
    private final AccessTokenDecoder accessTokenDecoder;
    private final GetUserUseCase getUserUseCase;

    @GetMapping("/token/me")
    public ResponseEntity<?> getUserFromToken(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid Authorization header"));
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();

        try {
            AccessToken accessToken = accessTokenDecoder.decode(token);

            Long userId = accessToken.getUserId();

            return getUserUseCase.getUserById(Long.valueOf(userId))
                    .map(user -> {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("userId", user.getId());
                        userData.put("email", user.getEmail());
                        userData.put("name", user.getName());
                        userData.put("profilePicture", user.getProfilePicture());
                        userData.put("provider", user.getProvider().toString());
                        userData.put("roles", List.of(user.getRole().toString()));

                        return ResponseEntity.ok(userData);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "User not found")));

        } catch (InvalidAccessTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token invalid or expired"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }
}