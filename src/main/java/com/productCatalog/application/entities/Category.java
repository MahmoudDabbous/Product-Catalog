package com.productCatalog.application.entities;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private int id;
	private String name;
	private List<Product> products;

	public Category() {
		this.products = new ArrayList<>();
	}

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
		this.products = new ArrayList<>();
	}

	public Category(String name) {
		this.name = name;
		this.products = new ArrayList<>();
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
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}