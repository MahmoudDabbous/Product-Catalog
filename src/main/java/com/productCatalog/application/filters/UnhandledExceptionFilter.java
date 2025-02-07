package com.productCatalog.application.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.productCatalog.application.dto.ErrorResponse;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class UnhandledExceptionFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(UnhandledExceptionFilter.class.getName());
	private final Gson gson = new Gson();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
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
}
