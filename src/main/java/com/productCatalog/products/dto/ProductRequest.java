package com.productCatalog.products.dto;

import java.util.ArrayList;
import java.util.List;

import com.productCatalog.application.validation.Validatable;

public class ProductRequest implements Validatable {

	private String name;
	private Double price;

	public ProductRequest() {
	}

	public ProductRequest(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public List<String> validate() {
		List<String> errors = new ArrayList<>();

		if (name == null || name.trim().isEmpty()) {
			errors.add("Name is required");
		} else {
			if (name.length() < 2 || name.length() > 254) {
				errors.add("Name must be between 2 and 254 characters");
			}
		}

		if (price == null) {
			errors.add("Price is required");
		} else if (price <= 0) {
			errors.add("Price must be positive");
		} else if (price < 0.01) {
			errors.add("Price must be >= 0.01");
		}

		return errors;
	}
}