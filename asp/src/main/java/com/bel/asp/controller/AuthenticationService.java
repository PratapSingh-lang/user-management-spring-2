package com.bel.asp.controller;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bel.asp.config.JwtService;
import com.bel.asp.entity.Token;
import com.bel.asp.entity.User;
import com.bel.asp.enums.TokenType;
import com.bel.asp.repo.TokenRepository;
import com.bel.asp.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenRepository tokenRepository;
    
    public User register(RegisterRequest request) {
        User user = User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
        User savedUser = userRepo.save(user);
        String jwtToken = jwtService.generateToken(user);
        
        saveUserToken(savedUser, jwtToken);
//        return AuthenticationResponse.builder().token(jwtToken).build();
        return savedUser;
    }

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userRepo.findByEmail(request.getEmail()).orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		String  referesh_token = jwtService.generateRefershToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder().token(jwtToken)
				.user_id(user.getId())
				.referesh_token(referesh_token)
				.build();
	}
    
    
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
      }
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
          return;
        validUserTokens.forEach(token -> {
          token.setExpired(true);
          token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
      }
    
    public List<User> getAllUsers() {
    	// TODO Auto-generated method stub
    	return userRepo.findAll();
    }

	public Object validate(AuthenticationRequest request1) {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateNewAccessToken(String email) {
		 User user = userRepo.findByEmail(email).orElseThrow();
  		String jwtToken = jwtService.generateToken(user);

  		revokeAllUserTokens(user);
  		saveUserToken(user, jwtToken);
		
  		return jwtToken;
	}

	public void deleteUser() {
		tokenRepository.deleteAll();
		userRepo.deleteAll();
		
	}
    
}