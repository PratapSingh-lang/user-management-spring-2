package com.bel.asp.config;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bel.asp.repo.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.bel.nms.auth.jwt.JWTTokenValidatorFilter;

//import com.bel.nms.auth.jwt.authConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;

//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request, 
//                                    @NonNull HttpServletResponse response, 
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//    	super.doFilter(request, response, filterChain);

	/*
	 * RestTemplate restTemplate = new RestTemplate(); RequestEntity<EndUserCsr>
	 * endUserRequest = RequestEntity
	 * .post(URI.create("https://localhost:9090/esign/ca/generateEndUserDSC"))
	 * .accept(MediaType.APPLICATION_JSON).body(endUserCsr);
	 * 
	 * ResponseEntity<String> endUserCertificate =
	 * customRestTemplateConfiguration.restTemplate().exchange(endUserRequest,
	 * String.class); return endUserCertificate.getBody();
	 */

//                final String authHeader = request.getHeader("Authorization");
//              
//                final String jwtToken;
//                final String email;
//      		 if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//                   filterChain.doFilter(request, response);
//                   return;
//               }
//               jwtToken = authHeader.substring(7);
//               email = jwtService.extractUsername(jwtToken);
//               if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                
//            	   
//            	   
//            	   
//            	   
//            	   //repo---> entity
////            	   entity--> bean me set 
//            	   
//            	   UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//                   
//                   var isTokenValid = tokenRepository.findByToken(jwtToken)
//                           .map(t -> !t.isExpired() && !t.isRevoked())
//                           .orElse(false);
//                   
//                   if (jwtService.isTokenValid(jwtToken, userDetails)
//                   		&& isTokenValid
//                   		) {
//                       UsernamePasswordAuthenticationToken authToken 
//                       		= new UsernamePasswordAuthenticationToken(
//                           userDetails,
//                           null,
//                           userDetails.getAuthorities()
//                       );
//                       authToken.setDetails(
//                           new WebAuthenticationDetailsSource().buildDetails(request)
//                       );
//                       SecurityContextHolder.getContext().setAuthentication(authToken);
//                     }
//                   }
////                sepFunction(authHeader,filterChain, request,response);
//                
//               
////                super.doFilter(request, response, filterChain);
//                    filterChain.doFilter(request, response);
////                    super.doFilter(request, response);
////                    super.doFilter(request, response, filterChain);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

//		String jwt = parseJwt(request);
//		logger.info("Token === "+jwt);

		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String email;
		boolean isTokenValid = false;
		if (authHeader == null || !authHeader.startsWith("Bearer ")
				|| request.getServletPath().equals("/api/v1/auth/token/referesh")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwtToken = authHeader.substring(7);
		if (jwtToken != null) {
			try {
				email = jwtService.extractUsername(jwtToken);
				if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(email);

					 isTokenValid = tokenRepository.findByToken(jwtToken)
							.map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
					
					
					
					if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			} catch (ExpiredJwtException e) {
				// e.printStackTrace();
				Claims claims = e.getClaims();
				
				if(!isTokenValid) {
					response.setHeader("error","user loggoed out , needs to login again");
		       		 Map<String, String> error = new HashMap<>();
		       		 error.put("error_message", "user loggoed out , needs to login again");
		       		 response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		       		 new ObjectMapper().writeValue(response.getOutputStream(), error);
					throw new RuntimeException("user needs to login again");
				}
				
				
				System.out.println("Token Expired " + String.valueOf(claims.get(
//							authConstants.JWT_CLAIM_USERNAME
						"userName")));
				response.setHeader("error",e.getMessage());
       		 Map<String, String> error = new HashMap<>();
       		 error.put("error_message", e.getMessage());
       		 response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
       		 new ObjectMapper().writeValue(response.getOutputStream(), error);
       		 
       		
//       		 response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
////       		 return new ResponseEntity<>(
//       				 throw new RuntimeException("token expired");
//           			 , HttpStatus.NOT_FOUND);
//				response.set
//				try {
//
//					logger.info("inside try block");
////						response = verifyRefreshToken(request, response, claims);
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					// e1.printStackTrace();
//					
//				}
			} catch (Exception e) {
				
				
				
				logger.error("JWT claims string is empty: {}", e.getMessage());
				// throw new AuthenticationServiceException("Authentication failed");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

//	private HttpServletResponse verifyRefreshToken(HttpServletRequest request, HttpServletResponse response,Claims claims) {
//		String username = String.valueOf(claims.get(authConstants.JWT_CLAIM_USERNAME));
//		String authToken = parseJwt(request);
//		String newRefreshToken = refreshTokenUtil.validateAndGenerateRefreshToken(authToken);
//		if(newRefreshToken!=null)
//		{
//		Authentication auth = new UsernamePasswordAuthenticationToken(username, null);
//		SecurityContextHolder.getContext().setAuthentication(auth);
//		String newjwt = jwtUtils.generateJwtToken(username);
//		response.setHeader(authConstants.AUTHORIZATION, newjwt);
//		response.setHeader(authConstants.REFRESHTOKEN, newRefreshToken);
//		response.setHeader(authConstants.TOKEN_CHANGED, "true");
//		return response;
//		}else
//		{
//			throw new AuthenticationServiceException("Refresh Token Expired");
//		}
//		
//	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(
//				authConstants.AUTHORIZATION
				"Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		} else
			return null;
	}

	private void validateJWTToken(String jwt) {
		// TODO Auto-generated method stub

	}
}
