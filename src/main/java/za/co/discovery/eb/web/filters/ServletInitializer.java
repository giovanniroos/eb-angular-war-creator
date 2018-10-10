package za.co.discovery.eb.web.filters;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    System.setProperty("spring.profiles.active", System.getProperty("environment"));
    return application.sources(AngularWarCreatorApplication.class);
  }

}
