package com.productCatalog.application.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.productCatalog.application.db.DatabaseConnection;
import com.productCatalog.application.exceptions.BadRequestException;
import com.productCatalog.application.validation.Validatable;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

public class BaseController extends HttpServlet {
	protected final Logger LOGGER;
	private static final long serialVersionUID = -4035559489965516824L;
	private final Gson gson;

	public BaseController() {
		super();
		this.gson = new Gson();
		this.LOGGER = Logger.getLogger(this.getClass().getName());
	}

	protected <T> Object parseRequest(BufferedReader reader, Class<T> entity) throws IOException {
		JsonElement jsonElement = JsonParser.parseReader(reader);
		try {
			Object parsedObject;
			if (jsonElement.isJsonArray()) {
				Type listType = TypeToken.getParameterized(List.class, entity).getType();
				List<T> parsedList = gson.fromJson(jsonElement, listType);
				parsedObject = parsedList;
			} else {
				parsedObject = gson.fromJson(jsonElement, entity);
			}

			List<String> errors = new ArrayList<>();

			if (parsedObject instanceof List) {
				throw new JsonSyntaxException("Invalid JSON format");
			} else if (parsedObject instanceof Validatable) {
				errors.addAll(((Validatable) parsedObject).validate());
			}

			if (!errors.isEmpty()) {
				throw new BadRequestException(errors);
			}

			return parsedObject;
		} catch (JsonSyntaxException e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	protected List<Integer> extractIdsFromPath(String path, int... at) throws NumberFormatException {
		String[] pathParts = path.split("/");
		List<Integer> ids = new ArrayList<>();

		for (int index : at) {
			if (index < 0 || index >= pathParts.length) {
				LOGGER.log(Level.WARNING, "Index {0} is out of range for path: {1}", new Object[] { index, path });
				throw new IllegalArgumentException("Index " + index + " is out of range for path: " + path);
			}
			try {
				ids.add(Integer.parseInt(pathParts[index]));
			} catch (NumberFormatException e) {
				LOGGER.log(Level.WARNING, "Invalid ID at index {0} in path: {1}", new Object[] { index, path });
				throw new BadRequestException("Invalid ID at index in path: " + path);
			}
		}

		return ids;
	}

	protected void sendJsonResponse(HttpServletResponse resp, Object data) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		try {
			String jsonResponse = gson.toJson(data);
			LOGGER.info("Sending JSON response: " + jsonResponse);
			resp.getWriter().write(jsonResponse);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing JSON response: {0}", e.getMessage());
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Closing database connection.");
		DatabaseConnection.getInstance().closeConnection();
		super.destroy();
	}
}
