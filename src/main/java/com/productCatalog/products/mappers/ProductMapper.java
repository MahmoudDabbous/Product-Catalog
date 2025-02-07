package com.productCatalog.products.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;
import com.productCatalog.categories.dto.CategoryResponse;
import com.productCatalog.categories.mappers.CategoryMapper;
import com.productCatalog.products.dto.ProductRequest;
import com.productCatalog.products.dto.ProductResponse;

public class ProductMapper {

	public static ProductResponse toResponse(Product product) {
		if (product == null) {
			return null;
		}
		return new ProductResponse(product);
	}

	public static List<ProductResponse> toListOfResponses(List<Product> products) {
		if (products == null) {
			return new ArrayList<>();
		}
		return products.stream().map(ProductMapper::toResponse).collect(Collectors.toList());
	}

	public static Product toEntity(ProductResponse response) {
		if (response == null) {
			return null;
		}
		Product product = new Product(response.getId(), response.getName(), response.getPrice());

		if (response.getCategories() != null) {
			for (CategoryResponse categoryResponse : response.getCategories()) {
				Category category = CategoryMapper.toEntity(categoryResponse);
				if (category != null) {
					product.addCategory(category);
				}
			}
		}
		return product;
	}

	public static Product toEntity(ProductRequest request) {
		if (request == null) {
			return null;
		}
		return new Product(request.getName(), request.getPrice());
	}

	public static List<Product> toListOfEntities(List<ProductResponse> responses) {
		if (responses == null) {
			return new ArrayList<>();
		}
		return responses.stream().map(ProductMapper::toEntity).collect(Collectors.toList());
	}

}