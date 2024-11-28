package individual.configuration.security;

import individual.configuration.security.auth.AuthenticationRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebConfig {

    private static final String[] SWAGGER_UI_RESOURCES = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**"};

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
//                                           AuthenticationEntryPoint authenticationEntryPoint,
//                                           AuthenticationRequestFilter authenticationRequestFilter) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(configurer ->
//                        configurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))//should be stateless but just for the sake of testing
//                .authorizeHttpRequests(registry ->
//                        registry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/students", "/tokens").permitAll()
//                                .requestMatchers("/oauth2/**", "/login/**").permitAll()
//                                .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
//                                .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 ->
//                        oauth2
//                                .defaultSuccessUrl("/services", true)
//                )
//                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint));
////                .addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers("/oauth2/**", "/login/**").permitAll()
                            .anyRequest().authenticated()
            )
            .oauth2Login(Customizer.withDefaults());


    return httpSecurity.build();
}


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowCredentials(true);
            }
        };
    }
}