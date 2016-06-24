package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@ComponentScan({"demo"})
public class TesteApplication {
	


    public static void main(String[] args) {
        SpringApplication.run(TesteApplication.class, args);
    }
}