package com.productCatalog.products.controllers;

import java.io.IOException;
import java.util.List;
import com.productCatalog.application.controllers.BaseController;
import com.productCatalog.application.entities.Product;
import com.productCatalog.application.exceptions.BadRequestException;
import com.productCatalog.application.exceptions.NotFoundException;
import com.productCatalog.products.dto.ProductRequest;
import com.productCatalog.products.dto.ProductResponse;
import com.productCatalog.products.mappers.ProductMapper;
import com.productCatalog.products.services.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products/*")
public class ProductController extends BaseController {
	private static final long serialVersionUID = -7441586583999593966L;
	private ProductService productService;

	@Override
	public void init() {
		productService = (ProductService) getServletContext().getAttribute(ProductService.class.getName());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		LOGGER.info("GET: " + req.getRequestURI());

		try {
			if (pathInfo == null || pathInfo.equals("/")) {
				LOGGER.info(">>> Retrieving all products.");
				List<Product> products = productService.findAll();
				List<ProductResponse> productResponses = ProductMapper.toListOfResponses(products);
				LOGGER.info("<<< All products retrieved.");
				sendJsonResponse(resp, productResponses);
			} else if (pathInfo.matches("^/\\d+$")) {
				int productId = Integer.parseInt(pathInfo.substring(1));
				LOGGER.info(">>> Retrieving product with ID: " + productId);
				Product product = productService.find(productId);
				ProductResponse productResponse = ProductMapper.toResponse(product);
				LOGGER.info("<<< product with ID: " + productId + " retrieved.");
				sendJsonResponse(resp, productResponse);
			} else {
				sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Resource not found");
			}
		} catch (NumberFormatException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
		} catch (NotFoundException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BadRequestException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("POST: " + req.getRequestURI());
		try {
			ProductRequest productRequest = (ProductRequest) parseRequest(req.getReader(), ProductRequest.class);
			Product product = productService.createProduct(ProductMapper.toEntity(productRequest));
			sendJsonResponse(resp, ProductMapper.toResponse(product));
			resp.setStatus(HttpServletResponse.SC_CREATED);
			LOGGER.info("Product created successfully.");
		} catch (NumberFormatException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
		} catch (NotFoundException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BadRequestException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e.getErrors());
		}

	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("DELETE: " + req.getRequestURI());
		String pathInfo = req.getPathInfo();

		try {
			int id = Integer.parseInt(pathInfo.substring(1));
			LOGGER.info("Deleting product with ID: " + id);
			productService.deleteProduct(id);
			resp.setStatus(HttpServletResponse.SC_OK);
			LOGGER.info("Product deleted successfully.");
		} catch (NumberFormatException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
		} catch (NotFoundException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BadRequestException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("PATCH: " + req.getRequestURI());
		String pathInfo = req.getPathInfo();

		try {
			int id = Integer.parseInt(pathInfo.substring(1));
			ProductRequest productRequest = (ProductRequest) parseRequest(req.getReader(), ProductRequest.class);
			Product updateProduct = productService.updateProduct(id, ProductMapper.toEntity(productRequest));
			sendJsonResponse(resp, ProductMapper.toResponse(updateProduct));
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (NumberFormatException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
		} catch (NotFoundException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BadRequestException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e.getErrors());
		}
	}

}
