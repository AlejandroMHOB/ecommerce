package com.pruebatecnica.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pruebatecnica.ecommerce.model.Cart;

@Service
public class CartsService {
	private List<Cart> carts = new ArrayList<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@Value("${carts.ttl-minutes}")
    private Long TTLMinutes;
    

	public Cart newCart() {
		Cart c = new Cart();
		this.carts.add(c);
		
		// AÑADIR TEMPORIZADOR PARA EL OBJETO
        scheduler.schedule(() -> this.deleteCart(c.getId()), this.TTLMinutes, TimeUnit.MINUTES);
		return c;
	}

	public Optional<Cart> getCart(Long id) {
		return this.carts.stream().filter(cart -> cart.getId() == id).findFirst();
	}

	public Boolean deleteCart(Long id) {
		// BUSCAR SI EXISTE ALGUN CARRITO CON EL ID ESPECIFICADO...
		Optional<Cart> carritoAEliminar = this.carts.stream().filter(carrito -> carrito.getId() == id).findFirst();

		// SI EL CARRITO EXISTE...
		if (carritoAEliminar.isPresent()) {
			this.carts.remove(carritoAEliminar.get());
			return true;
		}

		return false;
	}

}
