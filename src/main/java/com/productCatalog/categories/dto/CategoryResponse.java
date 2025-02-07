package com.productCatalog.categories.dto;

import java.util.List;
import com.productCatalog.products.dto.ProductResponse;
import com.productCatalog.application.entities.Category;

public class CategoryResponse {
    private int id;
    private String name;
    private List<ProductResponse> products;

    public CategoryResponse() {
    }

    public CategoryResponse(int id, String name, List<ProductResponse> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
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

    public List<ProductResponse> getProducts() {
        if (products == null || products.isEmpty()) {
            return null;
        }
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
