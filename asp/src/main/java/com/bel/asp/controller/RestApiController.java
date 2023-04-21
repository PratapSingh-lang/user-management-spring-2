package com.bel.asp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.bel.asp.config.ApplicationConfig;
import com.bel.asp.config.JWTAuthenticationFilter;
import com.bel.asp.repo.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
//@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RestApiController {

	 private final UserRepository userRepo;
	 private final JWTAuthenticationFilter jwtAuthFilter;
//     private final AuthenticationProvider authProvider;
     
//     private final ApplicationConfig applicationConfig;
     
	 @Autowired
		HttpServletResponse response;
     @GetMapping("/interceptor/token")
     public boolean interceptor(
//     		@RequestBody AuthenticationRequest request
     		
     		) {
    	 
    	 System.out.println("Token Auth Called ");
			if(response!=null&&response.getStatus()!=HttpServletResponse.SC_OK)
				return false;
			else
				return true;
    			 
//         return ResponseEntity.ok("this is admin api");
     }
     
	@GetMapping("/api/v1/auth/check")
    public ResponseEntity<?> register(
//    		@RequestBody RegisterRequest request
    		) {
        return ResponseEntity.ok("this is check api");
    }
	@GetMapping("/secured")
    public ResponseEntity<?> authenticate(
//    		@RequestBody AuthenticationRequest request
    		
    		) {
        return ResponseEntity.ok("this is secured api");
    }
	
	@GetMapping("/adminApi")
//	@RolesAllowed("ADMIN")
//	@PreAuthorize("hasRole('ADMIN')")
	@Secured("ADMIN")
    public ResponseEntity<?> admin(
//    		@RequestBody AuthenticationRequest request
    		
    		) {
        return ResponseEntity.ok("this is admin api");
    }
	
	@GetMapping("/userApi")
//	@PreAuthorize("hasRole('USER')")
//	@RolesAllowed("USER")
	@Secured("USER")
	 public ResponseEntity<?> user(
//	    		@RequestBody AuthenticationRequest request
	    		
	    		) {
	        return ResponseEntity.ok("this is user api");
	    }
	
	
	@GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(
//    		@RequestBody RegisterRequest request
    		) {
        return ResponseEntity.ok(userRepo.findAll());
    }
	
//	@PostMapping("/api/v1/auth/jwt")
//    public void JWTAuthenticationFilter(
////    		@RequestBody RegisterRequest request
////    		@NonNull HttpServletRequest request, 
////            @NonNull HttpServletResponse response, 
////            @NonNull FilterChain filterChain
//    		@RequestBody Httpclass httpclass
//    		
//    		
//    		) 
//    				 throws ServletException, IOException
//	{
////		 jwtAuthFilter.doFilter(request, response, filterChain);
//		 jwtAuthFilter.doFilter(httpclass.getRequest(), httpclass.getResponse(), httpclass.getFilterChain());
////        return ResponseEntity.ok(userRepo.findAll());
//    }
	
//	@GetMapping("/api/v1/auth/authenticationProvider")
//	public ResponseEntity<?> authenticationProvider(){
//		UserDetailsService provider = this.applicationConfig
////				.authProvider()
//				.userDetailServic()
//				;
//		return ResponseEntity.ok(provider);
//	}
//	
	
//	 public UserDetailsService userDetailService() {
//	        return username -> userRepo.findByEmail(username)
//	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//	    }
}
