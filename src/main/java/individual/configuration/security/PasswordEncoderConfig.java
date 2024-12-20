package individual.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder createBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
