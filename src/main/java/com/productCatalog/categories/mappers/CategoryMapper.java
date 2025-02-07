package com.productCatalog.categories.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;
import com.productCatalog.categories.dto.CategoryRequest;
import com.productCatalog.categories.dto.CategoryResponse;
import com.productCatalog.products.dto.ProductResponse;
import com.productCatalog.products.mappers.ProductMapper;

public class CategoryMapper {

	public static CategoryResponse toResponse(Category category) {
		if (category == null) {
			return null;
		}
		return new CategoryResponse(category.getId(), category.getName(), null);
	}

	public static CategoryResponse toResponseWithProducts(Category category, List<Product> products) {
		if (category == null) {
			return null;
		}
		List<ProductResponse> productResponses = null;
		if (products != null && !products.isEmpty()) {
			productResponses = products.stream().map(ProductMapper::toResponse).collect(Collectors.toList());
		}
		return new CategoryResponse(category.getId(), category.getName(), productResponses);
	}

	public static List<CategoryResponse> toListOfResponses(List<Category> categories) {
		if (categories == null) {
			return new ArrayList<>();
		}
		return categories.stream().map(CategoryMapper::toResponse).collect(Collectors.toList());
	}

	public static Category toEntity(CategoryRequest request) {
		if (request == null) {
			return null;
		}
		return new Category(request.getName());
	}

	public static Category toEntity(CategoryResponse response) {
		if (response == null) {
			return null;
		}
		return new Category(response.getId(), response.getName());
	}

}