//package com.bel.asp.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import com.bel.asp.repo.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Configuration
//@RequiredArgsConstructor
////@EnableGlobalMethodSecurity(
////		  prePostEnabled = true, 
////		  securedEnabled = true, 
////		  jsr250Enabled = true)EnableMethodSecurity
//@EnableMethodSecurity( 
//		prePostEnabled = true, 
//		securedEnabled = true, 
//		jsr250Enabled = true
//)
//public class ApplicationConfig
////extends GlobalMethodSecurityConfiguration 
//{
//    
//    @Autowired
//    private final UserRepository userRepo;
//
//    @Bean
//    public UserDetailsService userDetailService() {
//        return username -> userRepo.findByEmail(username)
//        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//    @Bean
//    public AuthenticationProvider authProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//      return config.getAuthenticationManager();
//    }
////    public String userDetailServic() {
////    	
//////    	return username = userRepo.findByEmail(username)
////        return username -> userRepo.findByEmail(username)
////        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
////    }
//}
