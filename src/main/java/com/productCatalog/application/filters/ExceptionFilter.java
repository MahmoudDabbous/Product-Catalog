package com.productCatalog.application.filters;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.productCatalog.application.dto.ErrorResponse;
import com.productCatalog.application.exceptions.BadRequestException;
import com.productCatalog.application.exceptions.NotFoundException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class ExceptionFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(ExceptionFilter.class.getName());
	private final Gson gson = new Gson();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		try {
			chain.doFilter(request, response);
		} catch (NumberFormatException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
		} catch (NotFoundException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
		} catch (BadRequestException e) {
			sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e.getErrors());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unhandled exception: ", e);
			handleException((HttpServletResponse) response, e);
		}
	}

	private void handleException(HttpServletResponse response, Exception e) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		String message = "500: Server error occurred";

		if (e instanceof IllegalArgumentException) {
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			message = e.getMessage();
		}

		response.setStatus(statusCode);
		ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
		response.getWriter().write(gson.toJson(errorResponse));
	}

	protected void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(statusCode);

		ErrorResponse errorResponse = new ErrorResponse(statusCode, message);
		try {
			String jsonError = gson.toJson(errorResponse);
			LOGGER.info("Sending error response: " + jsonError);
			resp.getWriter().write(jsonError);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing error response: {0}", e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void sendErrorResponse(HttpServletResponse resp, int statusCode) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(statusCode);
		LOGGER.info("Sending error response with status code: " + statusCode);
	}

	protected void sendErrorResponse(HttpServletResponse resp, int statusCode, List<String> errors) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(statusCode);

		ErrorResponse errorResponse = new ErrorResponse(statusCode, errors);
		try {
			String jsonError = gson.toJson(errorResponse);
			LOGGER.info("Sending error response: " + jsonError);
			resp.getWriter().write(jsonError);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing error response: {0}", e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void sendErrorResponse(HttpServletResponse resp, int statusCode, String message, List<String> errors) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.setStatus(statusCode);

		ErrorResponse errorResponse = new ErrorResponse(statusCode, message, errors);
		try {
			String jsonError = gson.toJson(errorResponse);
			LOGGER.info("Sending error response: " + jsonError);
			resp.getWriter().write(jsonError);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing error response: {0}", e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
