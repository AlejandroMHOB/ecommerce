package com.pruebatecnica.ecommerce.model;

import lombok.Data;

@Data
public class Product {
	private Long id;
	private String description;
	private Long amount;
	
	public Product(Long i, String d, Long a) {
		this.id = i;
		this.description = d;
		this.amount = a;
	}
}
