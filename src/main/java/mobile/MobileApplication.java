package mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@EnableMongoRepositories
@EnableMongoAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MobileApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MobileApplication.class, args);
    }

}
