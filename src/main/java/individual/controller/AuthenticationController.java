package individual.controller;

import individual.business.LoginUserUseCase;
import individual.business.RegisterUserUseCase;
import individual.configuration.security.token.AccessTokenEncoder;
import individual.configuration.security.token.Impl.AccessTokenImpl;
import individual.domain.User.CreateUserRequest;
import individual.domain.User.UserLoginRequest;
import individual.persistence.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    // Qualifier specifies which implementation of the same interface I want to use!
    private final RegisterUserUseCase registerUserUseCase;
    @Qualifier("oAuth")
    private final RegisterUserUseCase registerAuthUseCase;
    private final LoginUserUseCase loginUserUseCase;


    @PostMapping("/local-login")
    public ResponseEntity<String> localLogin(@RequestBody UserLoginRequest request) {
        String token = loginUserUseCase.login(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/oauth2-callback")
    public void loginWithGoogle(HttpServletResponse response, @AuthenticationPrincipal OAuth2User oauth2User) throws IOException {
        // Process user login/registration and get the token
        String token = registerAuthUseCase.processOAuth2User(oauth2User);

        // Redirect to frontend with token as a query parameter
        String redirectUrl = String.format("http://localhost:5173/oauth2/callback?token=%s", token);
        response.sendRedirect(redirectUrl);
    }
    @PostMapping("/local-register")
    public ResponseEntity<String> localRegister(@RequestBody CreateUserRequest request) {
        String token = registerUserUseCase.register(request);
        return ResponseEntity.ok(token);
    }

}
