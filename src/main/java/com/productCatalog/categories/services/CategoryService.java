package com.productCatalog.categories.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;
import com.productCatalog.application.exceptions.BadRequestException;
import com.productCatalog.application.exceptions.NotFoundException;
import com.productCatalog.categories.dao.CategoryDao;

public class CategoryService {
	private final CategoryDao categoryDao;

	public CategoryService(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	public Category find(int id) {
		Optional<Category> category = categoryDao.find(id);
		if (category.isEmpty())
			throw new NotFoundException("Category with ID " + id + " not found");

		return category.get();
	}

	public List<Category> findAll() {
		List<Category> categories = categoryDao.findAll();
		if (categories.isEmpty())
			return Collections.emptyList();

		return categories;
	}

	public Category createCategory(Category category) {
		if (category.getName() == null || category.getName().trim().isEmpty()) {
			throw new BadRequestException("Category name cannot be empty");
		}
		int id = categoryDao.add(category);
		category.setId(id);
		return category;
	}

	public void deleteCategory(int id) {
		Category category = find(id);
		categoryDao.remove(category.getId());
	}

	public Category findProductsByCategoryId(int categoryId) {
		Category category = find(categoryId);
		List<Product> products = categoryDao.findProductsByCategoryId(category.getId());
		if (products.isEmpty())
			throw new NotFoundException("There is no Products in the Category with ID " + category.getId());
		category.setProducts(products);
		return category;
	}

	public Category addProductToCategory(Product product, int categoryId) {
		Optional<Category> optionalCategory = categoryDao.find(categoryId);
		if (optionalCategory.isEmpty()) {
			throw new NotFoundException("Category with ID: " + product.getId() + " not found");
		}

		boolean isProductAlreadyInCategory = categoryDao.isProductInCategory(product.getId(), categoryId);
		if (isProductAlreadyInCategory) {
			throw new BadRequestException(
					"the Product with ID: " + product.getId() + " is already in the category wit Id:" + categoryId);
		}

		categoryDao.addProductToCategory(product.getId(), categoryId);
		List<Product> products = categoryDao.findProductsByCategoryId(categoryId);

		Category category = optionalCategory.get();
		category.setProducts(products);

		return category;
	}

	public void removeProductFromCategory(Product product, int categoryId) {
		Optional<Category> optionalCategory = categoryDao.find(categoryId);
		if (optionalCategory.isEmpty()) {
			throw new NotFoundException("Category with ID: " + product.getId() + " not found");
		}

		boolean isProductAlreadyInCategory = categoryDao.isProductInCategory(product.getId(), categoryId);
		if (!isProductAlreadyInCategory) {
			throw new BadRequestException(
					"the Product with ID: " + product.getId() + " is not in the category wit Id:" + categoryId);
		}
		categoryDao.removeProductFromCategory(product.getId(), categoryId);
	}
	
	public Category updateCategory(int id, Category category) {
		find(id);
		if (category.getName() == null || category.getName().trim().isEmpty()) {
			throw new BadRequestException("Product name cannot be empty");
		}
		category.setId(id);
		categoryDao.updateCategory(category);
		return category;
	}

}
