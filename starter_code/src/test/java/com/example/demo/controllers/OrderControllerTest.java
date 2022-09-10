package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
	@InjectMocks
	OrderController orderController;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	OrderRepository orderRepository;
	
	User userTest;
	Cart cartTest;
	Item itemTest;
	ModifyCartRequest modifyCartRequest;
	public static final String USER_NAME = "username";
	public static final String PASS_WORD = "password";
	
	@BeforeEach
	public void beforeEach() {
		userTest = new User();
		userTest.setId(1L);
		userTest.setUsername(USER_NAME);
		userTest.setPassword(PASS_WORD);
		
		cartTest = new Cart();
		cartTest.setId(1L);
		cartTest.setTotal(BigDecimal.TEN);
		
		userTest.setCart(cartTest);
		
		itemTest = new Item();
		itemTest.setId(1L);
		itemTest.setName("ItemTest");
		itemTest.setPrice(BigDecimal.TEN);
		itemTest.setDescription("Description of ItemTest");
		
		List<Item> items = new ArrayList<>();
		items.add(itemTest);
		
		cartTest.setUser(userTest);
		cartTest.setItems(items);
		
	}
	
	@Test
	void submitWhenUserNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		ResponseEntity<UserOrder> response = orderController.submit("testData");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(null,response.getBody());
	}
	
	@Test
	void submitSuccess() {
		UserOrder order = UserOrder.createFromCart(userTest.getCart());
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(userTest);
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(new UserOrder());
		ResponseEntity<UserOrder> response = orderController.submit("testData");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(order.getId(), response.getBody().getId());
		assertEquals(order.getTotal(), response.getBody().getTotal());
		assertEquals(order.getItems().size(),response.getBody().getItems().size() );
		assertEquals(order.getUser().getUsername(), response.getBody().getUser().getUsername());
		assertEquals(order.getUser().getPassword(), response.getBody().getUser().getPassword());
	}
	
	@Test
	void getOrdersForUserSuccess() {
		List<UserOrder> listResult = new ArrayList<UserOrder>();
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(userTest);
		Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(listResult);
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hi");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(listResult, response.getBody());
		assertEquals(listResult.size(), response.getBody().size());
	}
	
	@Test
	void getOrdersForUserWhenUserOrderIsNull() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(userTest);
		Mockito.when(orderRepository.findByUser(Mockito.any())).thenReturn(null);
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hi");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(null, response.getBody());
	}
	
	@Test
	void getOrdersForUserWhenUserNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testData");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
}
