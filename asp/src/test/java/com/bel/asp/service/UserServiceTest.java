//package com.bel.asp.service;
//
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import com.bel.asp.entity.User;
//import com.bel.asp.repo.UserRepository;
//
//@SpringBootTest
//public class UserServiceTest {
//
//	@Autowired
//	private UserService userService;
//	@Mock
//	private UserRepository userRepository;
//	@Test
//	void getUserByIdTest() {
//		List<User> userList = new ArrayList<User>();
//		User user = new User();
//		user.setId((long) 1);
//		user.setUsername("Bel");
//		user.setFirst_name("SBU");
//		user.setLast_name("SSU");
//		userList.add(user);
//		User user1 = new User();
//		user1.setId((long) 2);
//		user1.setUsername("Bel");
//		user1.setFirst_name("SBU");
//		user1.setLast_name("SSU");
//		userList.add(user1);
//		Mockito.when(this.userRepository.findAll()).thenReturn(userList);
//		Mockito.when(userRepository.findById((long) 1)).thenReturn(new User());
//	}
//	
//}
