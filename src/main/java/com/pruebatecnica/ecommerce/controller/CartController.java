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
import com.pruebatecnica.ecommerce.services.CartsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	CartsService cartsService;

	
	@Operation(summary = "Crear carrito", description = "Crea un nuevo carrito vacío donde se podrán añadir productos", responses = {
			@ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
			@ApiResponse(responseCode = "500", description = "Ocurrió algún problema al realizar la operación"), })
	@PostMapping("")
	public ResponseEntity<Cart> getNewCart() {
		log.info("Creando un nuevo carrito...");
		try {
			Cart carrito = this.cartsService.newCart();
			return new ResponseEntity<Cart>(carrito, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Obtener carrito", description = "Devuelve la información completa de un carrito especificado a partir de su ID, incluyendo el listado de productos", responses = {
			@ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
			@ApiResponse(responseCode = "404", description = "No se encontró ningún carrito con el ID especificado"),
			@ApiResponse(responseCode = "500", description = "Ocurrió algún problema al realizar la operación"), })
	@GetMapping("/{id}")
	public ResponseEntity<Cart> getCart(@PathVariable(value = "id") Long id) {
		log.info("Obteniendo información del carrito con id '{}'...", id);
		try {
			Optional<Cart> carrito = this.cartsService.getCart(id);
			if (carrito.isEmpty()) {
				return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Cart>(carrito.orElse(null), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Operation(summary = "Eliminar carrito", description = "Elimina un carrito, en caso de que este exista", responses = {
			@ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
			@ApiResponse(responseCode = "404", description = "No se encontró ningún carrito con el ID especificado"),
			@ApiResponse(responseCode = "500", description = "Ocurrió algún problema al realizar la operación"), })
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteCart(@PathVariable(value = "id") Long id) {
		log.info("Eliminando el carrito con id '{}'...", id);
		try {
			Boolean eliminado = this.cartsService.deleteCart(id);
			if (eliminado) {
				return new ResponseEntity<Boolean>(HttpStatus.OK);
			} else {
				return new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Añadir productos a un carrito", description = "Recibe un listado de productos y, en caso de que el carrito exista, los añade al mismo", responses = {
			@ApiResponse(responseCode = "200", description = "Operación realizada con éxito"),
			@ApiResponse(responseCode = "404", description = "No se encontró ningún carrito con el ID especificado"),
			@ApiResponse(responseCode = "500", description = "Ocurrió algún problema al realizar la operación"), })
	@PostMapping("/{cartId}/add-products")
	public ResponseEntity<Cart> addProductsToCart(@PathVariable(value = "cartId") Long cartId,
			@RequestBody List<Product> productsToAdd) {
		log.info("Añadiendo productos al carrito con id '{}'...", cartId);
		try {
			Cart carrito = this.cartsService.addProducts(cartId, productsToAdd);
			if (carrito == null) {
				return new ResponseEntity<Cart>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Cart>(carrito, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<Cart>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
