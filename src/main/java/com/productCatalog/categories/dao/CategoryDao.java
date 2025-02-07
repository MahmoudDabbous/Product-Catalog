package com.productCatalog.categories.dao;

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

public class CategoryDao {
	private DatabaseConnection dbConnection;

	public CategoryDao(DatabaseConnection connection) {
		dbConnection = connection;
	}

	public Optional<Category> find(int id) {
		String sql = "SELECT c.* FROM categories c  WHERE C.ID = ?";
		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			Category category = null;
			while (rs.next()) {
				if (category == null) {
					category = new Category(rs.getInt("id"), rs.getString("name"));
				}
			}
			return Optional.ofNullable(category);
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching category with ID: " + id, e);
		}
	}

	public List<Category> findAll() {
		List<Category> categories = new ArrayList<>();
		Map<Integer, Category> categoryMap = new HashMap<>();

		String sql = "SELECT c.* FROM categories c ";
		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int categoryId = rs.getInt("id");
				Category category = categoryMap.get(categoryId);
				if (category == null) {
					category = new Category(categoryId, rs.getString("name"));
					categoryMap.put(categoryId, category);
					categories.add(category);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all categories", e);
		}
		return categories;
	}

	public void remove(int id) {
		String sql = "DELETE FROM categories WHERE id = ?";
		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error deleting category with ID: " + id, e);
		}
	}

	public int add(Category category) {
		String sql = "INSERT INTO categories (name) VALUES (?)";
		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, category.getName());
			int id = stmt.executeUpdate();
			return id;
		} catch (SQLException e) {
			throw new RuntimeException("Error adding category: " + category.getName(), e);
		}
	}

	public List<Product> findProductsByCategoryId(int categoryId) {
		List<Product> products = new ArrayList<>();
		String sql = "SELECT p.* FROM products p " + "JOIN product_category pc ON p.id = pc.product_id "
				+ "WHERE pc.category_id = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, categoryId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Product product = new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
				products.add(product);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching products by Category ID", e);
		}
		return products;
	}

	public void addProductToCategory(int productId, int categoryId) {
		String sql = "INSERT INTO product_category (product_id, category_id) VALUES (?, ?)";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, productId);
			stmt.setInt(2, categoryId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(
					"Error adding product with ID: " + productId + " to Category with ID: " + categoryId, e);
		}
	}

	public void removeProductFromCategory(int productId, int categoryId) {
		String sql = "DELETE FROM product_category WHERE product_id = ? AND category_id = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, productId);
			stmt.setInt(2, categoryId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(
					"Error adding product with ID: " + productId + " to Category with ID " + categoryId, e);
		}
	}

	public boolean isProductInCategory(int productId, int categoryId) {
		String sql = "SELECT 1 FROM product_category WHERE product_id = ? AND category_id = ?";

		try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, productId);
			stmt.setInt(2, categoryId);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException(
					"Error search for a product with ID: " + productId + " in a Category with ID " + categoryId, e);
		}
	}
	
	public void updateCategory(Category category) {
		String updateCategorySql = "UPDATE categories SET name = ? WHERE id = ?";

		try (Connection conn = dbConnection.getConnection();
				PreparedStatement updateStmt = conn.prepareStatement(updateCategorySql);) {
			updateStmt.setString(1, category.getName());
			updateStmt.setInt(2, category.getId());
			updateStmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error updating product with ID: " + category.getId(), e);
		}
	}

}
