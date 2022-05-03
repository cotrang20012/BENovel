package mobile.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
               registry.addMapping("/**")
                       .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                       .allowedHeaders("*")
                       .allowedOrigins("http://localhost:3000/","https://tranduy26913.github.io/","https://thichtruyenchu.vercel.app/")
                       .allowCredentials(true)
                       .maxAge(3600);
            }
        };
    }

}
