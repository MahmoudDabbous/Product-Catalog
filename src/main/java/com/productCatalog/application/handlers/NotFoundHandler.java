package com.productCatalog.application.handlers;

import java.io.IOException;

import com.productCatalog.application.controllers.BaseController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/error-not-found")
public class NotFoundHandler extends BaseController {
	private static final long serialVersionUID = 2099110004372370100L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "resource not found");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "resource not found");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "resource not found");
	}
}
