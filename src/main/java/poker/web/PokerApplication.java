package poker.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "poker")
public class PokerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PokerApplication.class, args);
    }
}
