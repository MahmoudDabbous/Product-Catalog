package com.productCatalog.products.dto;

import java.util.ArrayList;
import java.util.List;

import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;
import com.productCatalog.categories.dto.CategoryResponse;

public class ProductResponse {
	private int id;
	private String name;
	private double price;
	private List<CategoryResponse> categories;

	public ProductResponse() {
	}

	public ProductResponse(int id, String name, int price, List<CategoryResponse> categories) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.categories = categories;
	}

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.price = product.getPrice();
		if (!product.getCategories().isEmpty()) {
			this.categories = new ArrayList<>();
			for (Category category : product.getCategories()) {
				this.categories.add(new CategoryResponse(category));
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<CategoryResponse> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryResponse> categories) {
		this.categories = categories;
	}
}
