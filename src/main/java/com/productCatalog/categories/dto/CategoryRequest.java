package com.productCatalog.categories.dto;

import java.util.ArrayList;
import java.util.List;

import com.productCatalog.application.validation.Validatable;

public class CategoryRequest implements Validatable {

	private String name;

	public CategoryRequest(String name) {
		this.name = name;
	}

	public CategoryRequest() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<String> validate() {
		List<String> errors = new ArrayList<>();

		if (name == null || name.trim().isEmpty()) {
			errors.add("Name is required");
		} else {
			if (name.length() < 2 || name.length() > 50) {
				errors.add("Name must be between 2 and 50 characters");
			}
		}

		return errors;
	}
}