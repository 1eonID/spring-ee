package springee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableRetry
@EnableWebSecurity

public class SpringeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringeeApplication.class, args);
	}
}
