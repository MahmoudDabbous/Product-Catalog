package com.productCatalog.products.dao;

import com.productCatalog.application.db.DatabaseConnection;
import com.productCatalog.application.entities.Category;
import com.productCatalog.application.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductDao {
	private DatabaseConnection dbConnection;

	public ProductDao(DatabaseConnection connection) {
		dbConnection = connection;
	}

	public Optional<Product> find(int id) {
		String sql = "SELECT p.*, c.id AS category_id, c.name AS category_name " + "FROM products p "
				+ "LEFT JOIN product_category pc ON p.id = pc.product_id "
				+ "LEFT JOIN categories c ON pc.category_id = c.id " + "WHERE p.id = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Product product = null;
			while (rs.next()) {
				if (product == null) {
					product = new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
				}
				int categoryId = rs.getInt("category_id");
				if (!rs.wasNull()) {
					Category category = new Category(categoryId, rs.getString("category_name"));
					product.addCategory(category);
				}
			}
			return Optional.ofNullable(product);
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching product with ID: " + id, e);
		}
	}

	public List<Product> findAll() {
		List<Product> products = new ArrayList<>();
		Map<Integer, Product> productMap = new HashMap<>();
		String sql = "SELECT p.*, c.id AS category_id, c.name AS category_name " + "FROM products p "
				+ "LEFT JOIN product_category pc ON p.id = pc.product_id "
				+ "LEFT JOIN categories c ON pc.category_id = c.id";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("id");
				Product product = productMap.get(productId);
				if (product == null) {
					product = new Product(productId, rs.getString("name"), rs.getDouble("price"));
					productMap.put(productId, product);
					products.add(product);
				}
				int categoryId = rs.getInt("category_id");
				if (!rs.wasNull()) {
					Category category = new Category(categoryId, rs.getString("category_name"));
					product.addCategory(category);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all products", e);
		}
		return products;
	}

	public void remove(int id) {
		String sql = "DELETE FROM products WHERE id = ?";
		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error deleting product with ID: " + id, e);
		}
	}

	public int add(Product product) {
		String productSql = "INSERT INTO products (name, price) VALUES (?, ?)";
		String productCategorySql = "INSERT INTO product_category (product_id, category_id) VALUES (?, ?)";

		try (Connection conn = dbConnection.getConnection()) {
			conn.setAutoCommit(false);

			try (PreparedStatement productStmt = conn.prepareStatement(productSql,
					PreparedStatement.RETURN_GENERATED_KEYS)) {
				productStmt.setString(1, product.getName());
				productStmt.setDouble(2, product.getPrice());
				int id = productStmt.executeUpdate();

				ResultSet rs = productStmt.getGeneratedKeys();
				if (rs.next()) {
					product.setId(rs.getInt(1));
				}

				try (PreparedStatement productCategoryStmt = conn.prepareStatement(productCategorySql)) {
					for (Category category : product.getCategories()) {
						productCategoryStmt.setInt(1, product.getId());
						productCategoryStmt.setInt(2, category.getId());
						productCategoryStmt.addBatch();
					}
					productCategoryStmt.executeBatch();
				}

				conn.commit();
				return id;
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error adding product: " + product.getName(), e);
		}
	}

	public void updateProduct(Product product) {
		String updateProductSql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

		try (Connection conn = dbConnection.getConnection();
				PreparedStatement updateStmt = conn.prepareStatement(updateProductSql);) {
			updateStmt.setString(1, product.getName());
			updateStmt.setDouble(2, product.getPrice());
			updateStmt.setInt(3, product.getId());
			updateStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error updating product with ID: " + product.getId(), e);
		}
	}

}
