package com.productCatalog.categories.controllers;

import java.io.IOException;
import java.util.List;

import com.productCatalog.application.controllers.BaseController;
import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;
import com.productCatalog.application.exceptions.NotFoundException;
import com.productCatalog.categories.dto.CategoryRequest;
import com.productCatalog.categories.dto.CategoryResponse;
import com.productCatalog.categories.mappers.CategoryMapper;
import com.productCatalog.categories.services.CategoryService;
import com.productCatalog.products.services.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/categories/*")
public class CategoryController extends BaseController {
	private static final long serialVersionUID = 9138161665561926695L;
	private CategoryService categoryService;
	private ProductService productService;

	@Override
	public void init() {
		categoryService = (CategoryService) getServletContext().getAttribute(CategoryService.class.getName());
		productService = (ProductService) getServletContext().getAttribute(ProductService.class.getName());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		LOGGER.info("GET: " + req.getRequestURI());

		if (pathInfo == null || pathInfo.equals("/")) {
			List<Category> categories = categoryService.findAll();
			List<CategoryResponse> responses = CategoryMapper.toListOfResponses(categories);
			sendJsonResponse(resp, responses);
		} else if (pathInfo.matches("^/\\d+$")) {
			int categoryId = Integer.parseInt(pathInfo.substring(1));
			Category category = categoryService.find(categoryId);
			CategoryResponse response = CategoryMapper.toResponse(category);
			sendJsonResponse(resp, response);
		} else if (pathInfo.matches("^/\\d+/products$")) {
			List<Integer> ids = extractIdsFromPath(pathInfo, 1);
			int categoryId = ids.get(0);
			Category category = categoryService.findProductsByCategoryId(categoryId);
			CategoryResponse response = CategoryMapper.toResponseWithProducts(category, category.getProducts());
			sendJsonResponse(resp, response);
		} else {
			throw new NotFoundException("resource not found");

		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		LOGGER.info("POST: " + req.getRequestURI());

		if (pathInfo.matches("^/\\d+$")) {
			CategoryRequest categoryRequest = (CategoryRequest) parseRequest(req.getReader(), CategoryRequest.class);
			LOGGER.info(">>> Creating New Category");
			Category category = categoryService.createCategory(CategoryMapper.toEntity(categoryRequest));
			CategoryResponse response = CategoryMapper.toResponseWithProducts(category, category.getProducts());
			sendJsonResponse(resp, response);
		} else {
			throw new NotFoundException("resource not found");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("DELETE: " + req.getRequestURI());
		String pathInfo = req.getPathInfo();
		if (pathInfo.matches("^/\\d+$")) {
			int categoryId = Integer.parseInt(pathInfo.substring(1));
			LOGGER.info(">>> Deleting category with ID: " + categoryId);
			categoryService.deleteCategory(categoryId);
			LOGGER.info("<<< Category deleted successfully.");
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} else if (pathInfo.matches("^/\\d+/products/\\d+$")) {
			LOGGER.info(">>> Removing product to category");
			List<Integer> pathSegments = extractIdsFromPath(pathInfo, 1, 3);
			int categoryId = pathSegments.get(0);
			int productId = pathSegments.get(1);
			Product product = productService.find(productId);
			categoryService.removeProductFromCategory(product, categoryId);
			LOGGER.info(">>> Product Removed from category successfully");
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} else {
			throw new NotFoundException("resource not found");
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("PATCH: " + req.getRequestURI());
		String pathInfo = req.getPathInfo();

		if (pathInfo.matches("^/\\d+$")) {
			int id = Integer.parseInt(pathInfo.substring(1));
			CategoryRequest categoryRequest = (CategoryRequest) parseRequest(req.getReader(), CategoryRequest.class);
			Category updateCategory = categoryService.updateCategory(id, CategoryMapper.toEntity(categoryRequest));
			sendJsonResponse(resp, CategoryMapper.toResponse(updateCategory));
			resp.setStatus(HttpServletResponse.SC_OK);
		} else if (pathInfo.matches("^/\\d+/products/\\d+$")) {
			LOGGER.info(">>> Adding product to category");
			List<Integer> pathSegments = extractIdsFromPath(pathInfo, 1, 3);
			int categoryId = pathSegments.get(0);
			int productId = pathSegments.get(1);
			Product product = productService.find(productId);
			Category category = categoryService.addProductToCategory(product, categoryId);
			CategoryResponse response = CategoryMapper.toResponseWithProducts(category, category.getProducts());
			sendJsonResponse(resp, response);
		} else {
			throw new NotFoundException("resource not found");
		}

	}

}
