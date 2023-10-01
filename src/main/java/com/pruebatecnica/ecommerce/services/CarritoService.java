package com.pruebatecnica.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.pruebatecnica.ecommerce.model.Cart;

@Service
public class CarritoService {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Long TTLSeconds = 10L;
    
    private List<Cart> carts = new ArrayList<>();
    

	public Cart newCart() {
		Cart c = new Cart();
		this.carts.add(c);
		
		// AÑADIR TEMPORIZADOR PARA EL OBJETO
        scheduler.schedule(() -> this.deleteCart(c.getId()), this.TTLSeconds, TimeUnit.SECONDS);
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
