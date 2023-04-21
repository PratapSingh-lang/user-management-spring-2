package com.bel.asp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.bel.asp.repo.UserRepository;
import com.bel.asp.security.CustomUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
////@EnableGlobalMethodSecurity(
////prePostEnabled = true, 
////securedEnabled = true, 
////jsr250Enabled = true)EnableMethodSecurity
@EnableMethodSecurity( 
prePostEnabled = true, 
securedEnabled = true, 
jsr250Enabled = true
)
public class SecurityConfiguration 
extends WebSecurityConfigurerAdapter
{
	@Autowired
        private  JWTAuthenticationFilter jwtAuthFilter;
//        private final AuthenticationProvider authProvider;
	@Autowired
        private  LogoutHandler logoutHandler;
        
	/*
	 * 
	 *    eyJhbGciOiJIUzI1NiJ9.eyJWaWtyYW1AMTIzIjpbeyJhdXRob3JpdHkiOiJVU0VSIn1dLCJzdWIiOiJWaWtyYW1AMTIzIiwiaWF0IjoxNjgxMzcwMjU2LCJleHAiOjE2ODEzNzAyNzF9.zdYblwh40a0bhB00gEgF0va1lSyHJQlS5VhVlDfK-bQ
	 * 
	 */
	
	
	@Autowired
    private CustomUserDetailService customUserDetailService;
        public static final String[] PUBLIC_URLS = {"/api/v1/auth/**", "/v3/api-docs", "/v2/api-docs",
                "/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html#!/"
                ,"/register"

        };
        
         @Autowired
        private  UserRepository userRepo;

//        @Bean
//        public UserDetailsService userDetailService() {
//            return username -> userRepo.findByEmail(username)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        }
        @Bean
        @Override
    	public AuthenticationManager authenticationManagerBean() throws Exception {
    	    return super.authenticationManagerBean();
    	}
        
    	@Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    	@Override
    	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	        auth.userDetailsService(this.customUserDetailService).passwordEncoder(passwordEncoder());
    	    }
    	
        @Override
    	protected void configure(HttpSecurity http) throws Exception {
    		http.
    				csrf()
    				.disable()
    				.authorizeHttpRequests()
    				.antMatchers(PUBLIC_URLS)
    				.permitAll()
//    				.antMatchers(HttpMethod.GET)
//    				.permitAll()
    				.antMatchers(
//    						"http://localhost:8080/swagger-ui.html"
//    						"http://localhost:8080/swagger-ui.html#!/"
    						"/v3/api-docs",
    						"/v2/api-docs"
    						)
    				.permitAll()
    				.anyRequest()
    				.authenticated()
    				.and()
    				.exceptionHandling()
    				
//    				.authenticationEntryPoint(
//    						this.jwtAuthenticationEntryPoint
//    						)  //handles exception due to authorization
    				.and()
    				.sessionManagement()
    				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    		http.addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
    		.logout()
    		.logoutUrl("/api/v1/auth/logout")
    		.addLogoutHandler(logoutHandler)
    		.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    		;
    	}
    	
        
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                .csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/api/v1/auth/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authenticationProvider(authProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout()
//                .logoutUrl("/api/v1/auth/logout")
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
//            ;
//            return http.build();
//        }
}
