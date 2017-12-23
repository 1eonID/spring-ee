package springee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SpringeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringeeApplication.class, args);
	}


}
