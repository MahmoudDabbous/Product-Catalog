package com.productCatalog.products.services;

import java.util.List;
import java.util.Optional;

import com.productCatalog.application.entities.Product;
import com.productCatalog.application.exceptions.BadRequestException;
import com.productCatalog.application.exceptions.NotFoundException;
import com.productCatalog.products.dao.ProductDao;

public class ProductService {
	private ProductDao productDao;

	public ProductService(ProductDao productDao) {
		this.productDao = productDao;
	}

	public Product find(int id) {
		Optional<Product> product = productDao.find(id);
		if (product.isEmpty())
			throw new NotFoundException("Product of ID " + id + "not found");
		return product.get();
	}

	public List<Product> findAll() {
		return productDao.findAll();
	}

	public Product createProduct(Product product) {
		if (product.getName() == null || product.getName().trim().isEmpty()) {
			throw new BadRequestException("Product name cannot be empty");
		}
		if (product.getPrice() <= 0) {
			throw new BadRequestException("Product price must be positive");
		}
		int id = productDao.add(product);
		product.setId(id);
		return product;
	}

	public void deleteProduct(int id) {
		Product product = find(id);
		productDao.remove(product.getId());
	}
	
	public Product updateProduct(int id, Product product) {
		find(id);
		if (product.getName() == null || product.getName().trim().isEmpty()) {
			throw new BadRequestException("Product name cannot be empty");
		}
		if (product.getPrice() <= 0) {
			throw new BadRequestException("Product price must be positive");
		}
		product.setId(id);
		productDao.updateProduct(product);
		return product;
	}
}
