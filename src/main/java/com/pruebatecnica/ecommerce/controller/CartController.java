package com.pruebatecnica.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pruebatecnica.ecommerce.model.Cart;
import com.pruebatecnica.ecommerce.model.Product;
import com.pruebatecnica.ecommerce.services.CarritoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	CarritoService carritoService;

	@GetMapping("/new")
	public ResponseEntity<Cart> getNewCart() {
		log.info("Creando un nuevo carrito...");
		try {
			Cart carrito = this.carritoService.newCart();
			return new ResponseEntity<Cart>(carrito, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cart> getCart(@PathVariable(value = "id") Long id) {
		log.info("Obteniendo información del carrito con id '{}'...", id);
		try {
			Optional<Cart> carrito = this.carritoService.getCart(id);
			if (carrito.isEmpty()) {
				return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Cart>(carrito.orElse(null), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteCart(@PathVariable(value = "id") Long id) {
		log.info("Eliminando el carrito con id '{}'...", id);
		try {
			Boolean eliminado = this.carritoService.deleteCart(id);
			if (eliminado) {
				return new ResponseEntity<Boolean>(HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{cartId}/add-products")
	public ResponseEntity<Cart> addProductsToCart(@PathVariable(value = "cartId") Long cartId,
			@RequestBody List<Product> productsToAdd) {
		log.info("Añadiendo productos al carrito con id '{}'...", cartId);
		try {
			Optional<Cart> carrito = this.carritoService.getCart(cartId);
			if (carrito.isEmpty()) {
				return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
			} else {
				carrito.get().getProducts().addAll(productsToAdd);

				return new ResponseEntity<Cart>(carrito.orElse(null), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
