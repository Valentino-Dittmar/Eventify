package individual.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/")
public class LoginController {
    @GetMapping
    public Map<String, Object> getUserInfo(OAuth2AuthenticationToken authenticationToken) {
        return authenticationToken.getPrincipal().getAttributes();
    }
}
