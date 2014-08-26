package com.evanlin.cloud.video;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.xml.ResourceServerBeanDefinitionParser;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.evanlin.cloud.video.auth.ClientUserdetailService;
import com.evanlin.cloud.video.auth.UserDetailObj;
import com.evanlin.cloud.video.client.VideoSvcApi;

@Configuration
public class SecurityConfig {
	//Setup Spring Security to intercept incoming requests to the Controllers

	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		protected void registerAuthentication(
				final AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService);
		}
//		private static final AuthenticationSuccessHandler NO_REDIRECT_SUCCESS_HANDLER = new AuthenticationSuccessHandler() {
//			@Override
//			public void onAuthenticationSuccess(HttpServletRequest request,
//					HttpServletResponse response, Authentication authentication)
//					throws IOException, ServletException {
//				response.setStatus(HttpStatus.SC_OK);
//			}
//		};
//		
//		private static final LogoutSuccessHandler JSON_LOGOUT_SUCCESS_HANDLER = new LogoutSuccessHandler() {
//			@Override
//			public void onLogoutSuccess(HttpServletRequest request,
//					HttpServletResponse response, Authentication authentication)
//					throws IOException, ServletException {
//				response.setStatus(HttpStatus.SC_OK);
//				response.setContentType("application/json");
//				response.getWriter().write("{}");
//			}
//		};
//		
//		@Override
//		protected void configure(final HttpSecurity http) throws Exception {
//			http.csrf().disable();
//			http.requestCache().requestCache(new NullRequestCache());
//			// clients login configuration.
//			http.formLogin()
//				.loginProcessingUrl(VideoSvcApi.LOGIN_PATH)
//				.successHandler(NO_REDIRECT_SUCCESS_HANDLER) //no redirect after login
//				.permitAll(); // Allow everyone to access the login URL
//			
//			// Make sure that clients can logout too!!
//			http.logout()
//				.logoutUrl(VideoSvcApi.LOGOUT_PATH)			//no redirect after logout
//				.logoutSuccessHandler(JSON_LOGOUT_SUCCESS_HANDLER)
//				.permitAll();
//			
//			
//			// We force clients to authenticate before accessing ANY URLs
//			// but need ADMIN for search method
//			// other than the login and logout that we have configured above.			
//			http.authorizeRequests()
//	        .antMatchers("/video/search/**").hasRole("ADMIN") 
//	        .anyRequest().authenticated();
//		}
//		
//		
//		// Account setting
//		@Autowired
//		protected void registerAuthentication(
//				final AuthenticationManagerBuilder auth) throws Exception {
//			
//			// This example creates a simple in-memory UserDetailService that
//			// is provided by Spring
//			auth.inMemoryAuthentication()
//					// User 1 : kkdai is admin/user
//					.withUser("kkdai")
//					.password("1234")
//					.roles("ADMIN","USER")
//					.and()
//					// User 2 : test is user
//					.withUser("test")
//					.password("1234")
//					.roles("USER");
//		}
	}

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerBeanDefinitionParser extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.authorizeRequests().antMatchers("/oauth/token").anonymous();
			http.authorizeRequests().antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')");			
			http.authorizeRequests().antMatchers("/**").access("#oauth2.hasScope('write')");
		}
	}

	@Configuration
	@EnableAuthorizationServer
	@Order(Ordered.LOWEST_PRECEDENCE - 100)
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
		@Autowired
		private AuthenticationManager authenticationManager;
		private ClientUserdetailService conbineService_;

		public OAuth2Config() throws Exception {
			// Create a service that has the credentials for all our clients
			ClientDetailsService csvc = new InMemoryClientDetailsServiceBuilder()
					// Create a client that has "read" and "write" access to the
			        // video service
					.withClient("mobile").authorizedGrantTypes("password")
					.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
					.scopes("read","write").resourceIds("video")
					.and()
					// Create a second client that only has "read" access to the
					// video service
					.withClient("mobileReader").authorizedGrantTypes("password")
					.authorities("ROLE_CLIENT")
					.scopes("read").resourceIds("video")
					.accessTokenValiditySeconds(3600).and().build();

			// Create a series of hard-coded users. 
			UserDetailsService svc = new InMemoryUserDetailsManager(
					Arrays.asList(
							UserDetailObj.create("kkdai", "1234", "ROLE_ADMIN", "ROLE_USER"),
							UserDetailObj.create("test", "1234", "ROLE_USER")));

			// Since clients have to use BASIC authentication with the client's id/secret,
			// when sending a request for a password grant, we make each client a user
			// as well. When the BASIC authentication information is pulled from the
			// request, this combined UserDetailsService will authenticate that the
			// client is a valid "user". 
			conbineService_ = new ClientUserdetailService(csvc, svc);
		}
		
		 // Return the list of trusted client information to anyone who asks for it. 
		@Bean
		public ClientDetailsService clientDetailsService() throws Exception {
			return conbineService_;
		}

		// Return all of our user information to anyone in the framework who requests it.
		@Bean
		public UserDetailsService userDetailsService() {
			return conbineService_;
		}

		// This method tells our AuthorizationServerConfigurerAdapter to use the delegated AuthenticationManager
		// to process authentication requests.
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints)
				throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}

		// * This method tells the AuthorizationServerConfigurerAdapter to use our self-defined client details service to
		// * authenticate clients with.
		@Override
		public void configure(ClientDetailsServiceConfigurer clients)
				throws Exception {
			clients.withClientDetails(clientDetailsService());
		}
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