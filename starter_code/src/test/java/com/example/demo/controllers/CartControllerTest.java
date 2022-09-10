package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
	@InjectMocks
	CartController cartController;

	@Mock
	UserRepository userRepository;
	
	@Mock
	ItemRepository itemRepository;
	
	@Mock
	CartRepository cartRepository;
	
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

		modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setQuantity(3);
		
		
	}

	@Test
	void addToCartWhenUserNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
		ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void addToCartWhenItemNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void addToCartWhenTotalIsNull() {
		cartTest.setTotal(null);
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(itemTest));
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cartTest);
		ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(cartTest, responseEntity.getBody());
		assertEquals(new BigDecimal(30), responseEntity.getBody().getTotal());
		
	}
	
	@Test
	void addToCartWhenSuccess() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(itemTest));
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cartTest);
		ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(cartTest.getId(), responseEntity.getBody().getId());
		assertEquals(cartTest.getTotal(), responseEntity.getBody().getTotal());
		assertEquals(cartTest.getUser().getUsername(), responseEntity.getBody().getUser().getUsername());
		assertEquals(cartTest.getUser().getPassword(), responseEntity.getBody().getUser().getPassword());
		assertEquals(cartTest.getUser().getId(), responseEntity.getBody().getUser().getId());
		assertEquals(cartTest.getItems().size(), responseEntity.getBody().getItems().size());
		assertEquals(new BigDecimal(40), responseEntity.getBody().getTotal());
	}
	
	
	@Test
	void removeFromCartWhenUserNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
		ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void removeFromCartWhenItemNotExist() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	void removeFromCartWhenTotalIsNull() {
		cartTest.setTotal(null);
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(itemTest));
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cartTest);
		ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(0, responseEntity.getBody().getItems().size());
	}
	
	@Test
	void removeFromCartSuccess() {
		Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(userTest);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(itemTest));
		Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cartTest);
		ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(0, responseEntity.getBody().getItems().size());
	}
}
