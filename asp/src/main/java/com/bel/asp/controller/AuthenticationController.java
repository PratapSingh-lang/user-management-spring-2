package com.bel.asp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bel.asp.config.JwtService;
import com.bel.asp.entity.User;
import com.bel.asp.repo.TokenRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    
    @DeleteMapping("/delete/all")
    public String deleteUser() {
    	service.deleteUser();
    	return "All User Deleted";
    }
    
    @GetMapping("/token/referesh")
    public  ResponseEntity<?> refereshToken(
    		HttpServletRequest request,
			HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{
    	
    	 final String authHeader = request.getHeader("Authorization");
         final String refereshToken;
         final String email;
//         boolean isTokenValid = tokenRepository.findByToken(jwtToken)
//                 .map(t -> !t.isExpired() && !t.isRevoked())
//                 .orElse(false);
         if(authHeader != null && authHeader.startsWith("Bearer "))
		 {
        	 try {
        	refereshToken = authHeader.substring(7);
        	 email = jwtService.extractUsername(refereshToken);
        	 
        	String newAccessToken =  service.generateNewAccessToken(email);
        	
        	Map<String, String> tokens = new HashMap<>();
        	tokens.put("access_token", newAccessToken);
        	tokens.put("referesh_token", refereshToken);
        	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        	 return new ResponseEntity<>(tokens, HttpStatus.OK);
//        	 return ResponseEntity.ok(service.authenticate(request));
        	 }catch (Exception exception) {
        		 response.setHeader("error",exception.getMessage());
        		 Map<String, String> error = new HashMap<>();
        		 error.put("error_message", exception.getMessage());
        		 response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        		 new ObjectMapper().writeValue(response.getOutputStream(), error);
        		 return new ResponseEntity<>(
        				 response
            			 , HttpStatus.NOT_FOUND);
        	 }
        }else {
//        	throw new RuntimeException("Referesh token is missing");
        	 return new ResponseEntity<>(new RuntimeException("Referesh token is missing")
        			 , HttpStatus.NOT_FOUND);
        }
        	
    }
    
    
    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
    		@RequestBody AuthenticationRequest request
    		,HttpServletResponse httpServletResponse
    		,HttpServletRequest httpServletRequest
    		, @RequestParam("captcha") String captchaInput
    		) {
    	
    	int savedCaptcha = (int) httpServletRequest.getSession().getAttribute("captcha");
        // Convert the input value to integer
        int inputCaptcha = 0;
        try {
            inputCaptcha = Integer.parseInt(captchaInput);
        } catch (NumberFormatException ex) {
            // Handle invalid input
            return new ResponseEntity<>("Invalid input", HttpStatus.BAD_REQUEST);
        }
        // Check if the input value matches the saved CAPTCHA result
        if (savedCaptcha == inputCaptcha) {
//            return new ResponseEntity<>("Captcha is valid", HttpStatus.OK);
            return ResponseEntity.ok(service.authenticate(request));
        } else {
            return new ResponseEntity<>("Captcha is invalid", HttpStatus.BAD_REQUEST);
        }
    	
//        return ResponseEntity.ok(service.authenticate(request));
    }
   
    
//    @PostMapping("/validateToken")
//    public ResponseEntity<AuthenticationResponse> validateToken(
//    		@RequestBody AuthenticationRequest request1
//    		,HttpServletResponse httpServletResponse) 
//    
//    {
//        return ResponseEntity.ok(service.validate(request1));
//    }
    
    
    @GetMapping("/getusername")
    public String getusername(
    		@RequestParam("token") String token
    		) {
        return 
        		jwtService.extractUsername(token);
    }
    
//    @GetMapping("/getusername")
//    public boolean validtoken(
//    		@RequestParam("token") String token
//    		) {
//    	
//    	var isTokenValid = tokenRepository.findByToken(token)
//              .map(t -> !t.isExpired() && !t.isRevoked())
//              .orElse(false);
////      
//      if (jwtService.isTokenValid(token, userDetails)
//      		&& isTokenValid) {
//    	  
//      }
//        return 
//        		jwtService.extractUsername(token);
//    }
    
    
}
