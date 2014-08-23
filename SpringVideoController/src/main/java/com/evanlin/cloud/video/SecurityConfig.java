package com.evanlin.cloud.video;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.evanlin.cloud.video.client.VideoSvcApi;

@Configuration
//Setup Spring Security to intercept incoming requests to the Controllers
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final AuthenticationSuccessHandler NO_REDIRECT_SUCCESS_HANDLER = new AuthenticationSuccessHandler() {
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			response.setStatus(HttpStatus.SC_OK);
		}
	};

	
	private static final LogoutSuccessHandler JSON_LOGOUT_SUCCESS_HANDLER = new LogoutSuccessHandler() {
		@Override
		public void onLogoutSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write("{}");
		}
	};

	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.requestCache().requestCache(new NullRequestCache());
		// clients login configuration.
		http.formLogin()
			.loginProcessingUrl(VideoSvcApi.LOGIN_PATH)
			.successHandler(NO_REDIRECT_SUCCESS_HANDLER) //no redirect after login
			.permitAll(); // Allow everyone to access the login URL
		
		// Make sure that clients can logout too!!
		http.logout()
			.logoutUrl(VideoSvcApi.LOGOUT_PATH)			//no redirect after logout
			.logoutSuccessHandler(JSON_LOGOUT_SUCCESS_HANDLER)
			.permitAll();
		
		// Require clients to login and have an account with the "user" role
		// in order to access /video
		// http.authorizeRequests().antMatchers("/video").hasRole("user");
		
		// Require clients to login and have an account with the "user" role
		// in order to send a POST request to /video
		// http.authorizeRequests().antMatchers(HttpMethod.POST, "/video").hasRole("user");
		
		// We force clients to authenticate before accessing ANY URLs 
		// other than the login and lougout that we have configured above.
		//http.authorizeRequests().anyRequest().authenticated();
		http.authorizeRequests()
        .antMatchers("/video/search/**").hasRole("RULE_ADMIN") // #6
        .anyRequest().authenticated();
	}
	
	
	// Account setting
	@Autowired
	protected void registerAuthentication(
			final AuthenticationManagerBuilder auth) throws Exception {
		
		// This example creates a simple in-memory UserDetailService that
		// is provided by Spring
		auth.inMemoryAuthentication()
				// User 1 : kkdai is admin/user
				.withUser("kkdai")
				.password("1234")
				.roles("RULE_ADMIN","RULE_USER")
				.and()
				// User 2 : test is user
				.withUser("test")
				.password("1234")
				.roles("RULE_USER");
	}

}
