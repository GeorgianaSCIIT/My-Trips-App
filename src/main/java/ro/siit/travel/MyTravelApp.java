package ro.siit.travel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

/**
 * My Travel App Main Application class
 *
 * @author Georgiana Simpetreanu
 * @version 1.0
 * Date 10/21/2019.
 */


@SpringBootApplication
public class MyTravelApp implements WebMvcConfigurer {


	private static final Logger logger = LoggerFactory.getLogger(MyTravelApp.class);

	public static void main(String[] args) {
		SpringApplication.run(MyTravelApp.class, args);
		logger.info("Application Started");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");

	}

}
