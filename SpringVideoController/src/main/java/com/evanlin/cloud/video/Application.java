package com.evanlin.cloud.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.evanlin.cloud.video.json.HATEOSMapper;
import com.evanlin.cloud.video.videoDB.NoDuplicateVideoDB;
import com.evanlin.cloud.video.videoDB.videoDB;
import com.evanlin.cloud.video.videoDB.VideoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

// Tell Spring that this object represents a Configuration for the
// application
@EnableJpaRepositories(basePackageClasses = VideoRepository.class)
// Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
// so that requests can be routed to our Controllers)
@EnableWebMvc
//Tell Spring that this object represents a Configuration for the
//application
@Configuration
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan
// Tell Spring to automatically inject any dependencies that are marked in
// our classes with @Autowired
@EnableAutoConfiguration
public class Application extends RepositoryRestMvcConfiguration{

	// Tell Spring to launch our app!
	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//	public videoDB VideoDB(){
//		return new NoDuplicateVideoDB();
//	}
	
	@Override
	public ObjectMapper halObjectMapper(){
		return new HATEOSMapper();
	}
	
}
