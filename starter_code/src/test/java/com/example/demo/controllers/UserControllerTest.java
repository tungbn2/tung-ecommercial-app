package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	@InjectMocks
	UserController userController;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	CartRepository cartRepository;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	User userTest;
	Cart cart;
	CreateUserRequest request;
	public static final String USER_NAME = "username";
	public static final String PASS_WORD = "password";
	
	@BeforeEach
	public void beforeEach() {
		userTest = new User();
		userTest.setId(1L);
		userTest.setUsername(USER_NAME);
		userTest.setPassword(PASS_WORD);
		
		cart = new Cart();
		
		request = new CreateUserRequest();
		request.setUsername("username");
		
	}
	
	@Test
	void findByIdSucces() {
		Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(userTest));
		ResponseEntity<User> response = userController.findById(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userTest.getId(), response.getBody().getId());
		assertEquals(userTest.getUsername(), response.getBody().getUsername());
		assertEquals(userTest.getPassword(), response.getBody().getPassword());
		assertEquals(userTest.getCart(), response.getBody().getCart());
	}
	
	@Test
	void findByUserNameSuccess() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(userTest);
		ResponseEntity<User> response = userController.findByUserName("testData");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(userTest.getId(), response.getBody().getId());
		assertEquals(userTest.getUsername(), response.getBody().getUsername());
		assertEquals(userTest.getPassword(), response.getBody().getPassword());
		assertEquals(userTest.getCart(), response.getBody().getCart());
	}
	
	@Test
	void findByUserNameWhenUserNotExits() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		ResponseEntity<User> response = userController.findByUserName("testData");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null, response.getBody());
	}
	
	@Test
	void createUserWhenPasswordLengthLessThan7() {
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);
		request.setPassword("123456");
		ResponseEntity<User> response = userController.createUser(request);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void createUserWhenInvalidConfirmPassword() {
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);
		request.setPassword("12345678");
		request.setConfirmPassword("87654321");
		ResponseEntity<User> response = userController.createUser(request);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	void createUserSuccess() {
		User user = new User();
		user.setUsername(request.getUsername());
		Cart cart = new Cart();
		user.setCart(cart);
		
		request.setPassword("987654321");
		request.setConfirmPassword("987654321");
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(userTest);
		ResponseEntity<User> response = userController.createUser(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(user.getId(), response.getBody().getId());
		assertEquals(user.getUsername(), response.getBody().getUsername());
	}
}
