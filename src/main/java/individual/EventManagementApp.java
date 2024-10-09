package individual;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@SpringBootApplication
public class EventManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(EventManagementApp.class, args);
    }


   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173","http://localhost:4173" )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Customize the methods you want to allow
                .allowedHeaders("*")
                .allowCredentials(true);
    }*/


}
