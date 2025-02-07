package com.productCatalog.application.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
	private static DatabaseConnection instance;
	private String jdbcUrl;
	private String username;
	private String password;
	private String driverClassName;

	private DatabaseConnection() {
		Properties props = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties")) {
			props.load(input);
			this.jdbcUrl = props.getProperty("db.url");
			this.username = props.getProperty("db.user");
			this.password = props.getProperty("db.password");
			this.driverClassName = props.getProperty("db.driver");

			Class.forName(driverClassName);
		} catch (Exception e) {
			throw new RuntimeException("Error reading Database configurations", e);
		}
	}

	public static synchronized DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}

	public Connection getConnection() {
		try {
			return DriverManager.getConnection(jdbcUrl, username, password);
		} catch (Exception e) {
			throw new RuntimeException("Error getting database connection", e);
		}
	}

	public void shutdown() {
	    try {
	        Driver driver = DriverManager.getDriver(jdbcUrl);
	        DriverManager.deregisterDriver(driver);
	    } catch (SQLException e) {
	        throw new RuntimeException("Error deregistering the database driver", e);
	    } catch (Exception e) {
	        throw new RuntimeException("Error shutting down the database", e);
	    }
	}
	
	public void closeConnection() {
		try {
			getConnection().close();
		} catch (SQLException e) {
			throw new RuntimeException("Error closing down the database connection", e);
		}
	}
}