package com.bel.asp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bel.asp.entity.User;
//import com.bel.asp.exceptions.ResourceNotFoundException;
import com.bel.asp.repo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
//	,ResourceNotFoundException 
	{
		// TODO Auto-generated method stub
		User user = this.userRepo.findByEmail(username)
				.orElseThrow(() ->new UsernameNotFoundException("User not found"));
		
		return user;
	}

}
