package com.productCatalog.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.productCatalog.application.db.DatabaseConnection;
import com.productCatalog.categories.dao.CategoryDao;
import com.productCatalog.categories.services.CategoryService;
import com.productCatalog.products.dao.ProductDao;
import com.productCatalog.products.services.ProductService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Application implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		LOGGER.info("> Initializing application context...");

		DatabaseConnection connection = DatabaseConnection.getInstance();
		CategoryDao categoryDao = new CategoryDao(connection);
		ProductDao productDao = new ProductDao(connection);

		LOGGER.info(">> Initializing CategoryService...");
		CategoryService categoryService = new CategoryService(categoryDao);
		LOGGER.info(">> Initializing ProductService...");
		ProductService productService = new ProductService(productDao);

		context.setAttribute(CategoryService.class.getName(), categoryService);
		LOGGER.info("<< CategoryService Initialized.");

		context.setAttribute(ProductService.class.getName(), productService);
		LOGGER.info("<< ProductService Initialized.");

		LOGGER.info("< Application context initializing.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		LOGGER.info("> Destroying the application context...");
		LOGGER.info(">> Shutting Database Driver down...");

		try {
			DatabaseConnection.getInstance().shutdown();
			LOGGER.info("<< Database driver deregistered successfully.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error shutting down database driver.", e);
		}

		try {
			LOGGER.info(">> finding and shutting down AbandonedConnectionCleanupThread.");
			Class<?> abandonCleanupConnection = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
			abandonCleanupConnection.getMethod("checkedShutdown").invoke(null);
			LOGGER.info("<< AbandonedConnectionCleanupThread has been shutdown.");
		} catch (ClassNotFoundException e) {
			LOGGER.info("<< AbandonedConnectionCleanupThread not found.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error shutting down AbandonedConnectionCleanupThread", e);
		}
		LOGGER.info("< Application context destroyed.");
	}
}
