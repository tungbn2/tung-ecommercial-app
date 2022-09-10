package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
	@InjectMocks
	ItemController itemController;
	
	@Mock
	ItemRepository itemRepository;
	
	List<Item> items;
	Item item;
	
	@BeforeEach
	public void beforeEach() {
		items = new ArrayList<>();
		item = new Item();
		item.setId(1L);
		item.setName("ItemTest");
		item.setPrice(BigDecimal.TEN);
		item.setDescription("Description ItemTest");
		items.add(item);
	}
	
	
	@Test
	void getItemsSucces() {
		Mockito.when(itemRepository.findAll()).thenReturn(items);
		ResponseEntity<List<Item>> responseEntity = itemController.getItems();
		assertEquals(1, responseEntity.getBody().size());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}
	
	@Test
	void getItemByIdWhenResultIsNull() {
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
		ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}
	
	@Test
	void getItemByIdSucess() {
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
		ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
		assertEquals(item, responseEntity.getBody());
		assertEquals(item.hashCode(), responseEntity.getBody().hashCode());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(item.getName(), responseEntity.getBody().getName());
		assertEquals(item.getId(), responseEntity.getBody().getId());
		assertEquals(item.getDescription(), responseEntity.getBody().getDescription());
		assertEquals(item.getPrice(), responseEntity.getBody().getPrice());
	}
	
	@Test
	void getItemByIdSucessWithIdIsNull() {
		item.setId(null);
		Mockito.when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
		ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
		assertEquals(item, responseEntity.getBody());
		assertEquals(item.hashCode(), responseEntity.getBody().hashCode());
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(item.getName(), responseEntity.getBody().getName());
		assertEquals(item.getId(), responseEntity.getBody().getId());
		assertEquals(item.getDescription(), responseEntity.getBody().getDescription());
		assertEquals(item.getPrice(), responseEntity.getBody().getPrice());
	}
	
	@Test
	void getItemsByNameSuccess() {
		Mockito.when(itemRepository.findByName(Mockito.anyString())).thenReturn(items);
		ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("testData");
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(items, responseEntity.getBody());
		assertEquals(items.size(), responseEntity.getBody().size());
	}
	
	@Test
	void getItemsByNameWhenResultIsEmpty() {
		List<Item> listItem = new ArrayList<Item>();
		Mockito.when(itemRepository.findByName(Mockito.anyString())).thenReturn(listItem);
		ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("testData");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}
	
	@Test
	void getItemsByNameWhenResultIsNull() {
		Mockito.when(itemRepository.findByName(Mockito.anyString())).thenReturn(null);
		ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("testData");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}
	
	
}
