package com.productCatalog.application.entities;

import java.util.ArrayList;
import java.util.List;

public class Product {
	private int id;
	private String name;
	private double price;
	private List<Category> categories;

	public Product() {
		this.categories = new ArrayList<>();
	}

	public Product(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.categories = new ArrayList<>();
	}

	public Product(String name, Double price) {
		this.name = name;
		this.price = price;
		this.categories = new ArrayList<>();
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

	public void setPrice(double price) {
		this.price = price;
	}

	public void addCategory(Category category) {
		categories.add(category);
	}

	public List<Category> getCategories() {
		return categories;
	}
}