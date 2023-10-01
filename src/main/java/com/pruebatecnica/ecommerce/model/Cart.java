package com.pruebatecnica.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Cart {
	
    private static Long contadorId = 0L;
	private Long id;
	private List<Product> products;
	
	public Cart() {
		this.id = contadorId++;
		this.products = new ArrayList<Product>();
	}
}
