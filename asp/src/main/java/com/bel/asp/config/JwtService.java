package com.bel.asp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String SECRET_KEY = "50655368566D597133743677397A24432646294A404E635166546A576E5A7234";

    
    public String generateRefershToken(UserDetails userDetails) {
  
    	return Jwts
  	        .builder()
  	        .setSubject(userDetails.getUsername())
  	        .setIssuedAt(new Date(System.currentTimeMillis()))
  	        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
  	        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
  	        .compact();
    }
    
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  
  
  
  public String generateToken(UserDetails userDetails) {
	  
//	  Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//	  String username = userDetails.getUsername();
	
	  Map<String, Collection<? extends GrantedAuthority>> claims = new HashMap<>();
//	  claims.put(username, authorities);
	  
	  claims.put(userDetails.getUsername(), userDetails.getAuthorities());
	  
//	  return generateToken(new HashMap<>().put(userDetails.getUsername()
//	  , userDetails.getAuthorities()), userDetails);
//    return generateToken(new HashMap<>(), userDetails);
	  
	  return generateToken(claims, userDetails);
  }

  public String generateToken(
//      Map<String, Object> extraClaims,
		  Map<String, Collection<? extends GrantedAuthority>> extraClaims,
      UserDetails userDetails
  ) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
//        .setClaims(userDetails.getAuthorities())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
