package com.evanlin.cloud.video;

import java.io.File;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
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

	
	// Add Https support to Tomcat
	@Bean
	EmbeddedServletContainerCustomizer containerCustomizer(
			@Value("${keystore.file}") String keystoreFile,
			@Value("${keystore.pass}") final String keystorePass)
			throws Exception {

		
		// This is boiler plate code to setup https on embedded Tomcat
		// with Spring Boot:
		
		final String absoluteKeystoreFile = new File(keystoreFile)
				.getAbsolutePath();

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
				tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {

					@Override
					public void customize(Connector connector) {
						connector.setPort(8443);
						connector.setSecure(true);
						connector.setScheme("https");

						Http11NioProtocol proto = (Http11NioProtocol) connector
								.getProtocolHandler();
						proto.setSSLEnabled(true);
						
						// If you update the keystore, you need to change
						// these parameters to match the keystore that you generate
						proto.setKeystoreFile(absoluteKeystoreFile);
						proto.setKeystorePass(keystorePass);
						proto.setKeystoreType("JKS");
						proto.setKeyAlias("tomcat");

					}
				});
			}

		};
	}
}
