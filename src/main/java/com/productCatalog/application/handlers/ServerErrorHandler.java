package com.productCatalog.application.handlers;

import java.io.IOException;

import com.productCatalog.application.controllers.BaseController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/error-server-error")
public class ServerErrorHandler extends BaseController {
	private static final long serialVersionUID = 7790092669782088244L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: internal server error");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: internal server error");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500: internal server error");
	}

}
