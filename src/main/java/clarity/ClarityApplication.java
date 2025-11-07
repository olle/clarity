package clarity;

import demo.Demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class ClarityApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClarityApplication.class, args);
  }

  @Configuration
  @Import({Demo.class})
  public static class Modules {}
}
