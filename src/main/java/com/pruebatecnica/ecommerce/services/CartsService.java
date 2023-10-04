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
import com.pruebatecnica.ecommerce.model.Product;

@Service
public class CartsService {
	private List<Cart> carts = new ArrayList<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Value("${carts.ttl-minutes}")
	private Long TTLMinutes;

	/**
	 * Añade un nuevo carrito al listado.
	 * 
	 * @return Nuevo carrito.
	 */
	public Cart newCart() {
		Cart c = new Cart();
		this.carts.add(c);

		// AÑADIR TEMPORIZADOR PARA EL OBJETO
		scheduler.schedule(() -> this.deleteCart(c.getId()), this.TTLMinutes, TimeUnit.MINUTES);
		return c;
	}

	/**
	 * Devuelve toda la información sobre un carrito a partir de su id en caso de
	 * que este exista en el listado.
	 * 
	 * @return Información del carrito si este existe en el listado.
	 */
	public Optional<Cart> getCart(Long id) {
		return this.carts.stream().filter(cart -> cart.getId() == id).findFirst();
	}

	/**
	 * Elimina un carrito a partir de su id si este existe.
	 * 
	 * @return Boolean - Si se encontró un carrito con ese id y se eliminó, o no.
	 */
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

	/**
	 * Añade los productos del listado al carrito especificado si este existe. Si
	 * alguno de los productos ya está en el carrito incrementa la cantidad del
	 * mismo.
	 * 
	 * @return Carrito despues de realizar las operaciones.
	 */
	public Cart addProducts(Long cartId, List<Product> products) {
		Optional<Cart> carrito = this.carts.stream().filter(cart -> cart.getId() == cartId).findFirst();

		if (carrito.isEmpty()) {
			return null;
		}

		for (Product p : products) {
			if (p.getAmount() > 0) {
				Optional<Product> productoIgual = carrito.get().getProducts().stream()
						.filter(product -> product.getId() == p.getId()).findFirst();
				if (productoIgual.isPresent()) {
					productoIgual.get().setAmount(productoIgual.get().getAmount() + p.getAmount());
				} else if (p.getId() != null) {
					carrito.get().getProducts().add(p);
				}
			}
		}

		return carrito.get();
	}

}
