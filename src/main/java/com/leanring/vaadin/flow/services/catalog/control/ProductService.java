package com.leanring.vaadin.flow.services.catalog.control;

import com.leanring.vaadin.flow.services.catalog.entity.Category;
import com.leanring.vaadin.flow.services.catalog.entity.Product;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/// Product service managing catalog operations.
/// Implements CRUD operations and business logic for products.
/// Uses ConcurrentHashMap for thread-safe in-memory storage.
@Service
public class ProductService {

  private final Map<Long, Product> products = new ConcurrentHashMap<>();

  /// Constructor initializes service with sample data
  public ProductService() {
    initializeSampleData();
  }

  /// Retrieves all products
  /// @return List of all products
  public List<Product> findAll() {
    return new ArrayList<>(products.values());
  }

  /// Finds products by category
  /// @param category Category to filter by
  /// @return List of products in specified category
  public List<Product> findByCategory(Category category) {
    return products.values().stream()
      .filter(p -> p.category() == category)
      .collect(Collectors.toList());
  }

  /// Searches products by name
  /// @param searchTerm Search term (case-insensitive)
  /// @return List of matching products
  public List<Product> search(String searchTerm) {
    var term = searchTerm.toLowerCase().trim();
    return products.values().stream()
      .filter(p -> p.name().toLowerCase().contains(term) ||
                   (p.description() != null && p.description().toLowerCase().contains(term)))
      .collect(Collectors.toList());
  }

  /// Finds product by ID
  /// @param id Product ID
  /// @return Optional containing product if found
  public Optional<Product> findById(Long id) {
    return Optional.ofNullable(products.get(id));
  }

  /// Saves a new product
  /// @param product Product to save (without ID)
  /// @return Saved product with generated ID
  public Product save(Product product) {
    var newProduct = Product.create(
      product.name(),
      product.category(),
      product.price(),
      product.stock(),
      product.launchDate(),
      product.description()
    );
    products.put(newProduct.id(), newProduct);
    return newProduct;
  }

  /// Updates an existing product
  /// @param product Product with updated values
  /// @return Updated product
  /// @throws IllegalArgumentException if product ID not found
  public Product update(Product product) {
    if (!products.containsKey(product.id())) {
      throw new IllegalArgumentException("Product not found: " + product.id());
    }
    products.put(product.id(), product);
    return product;
  }

  /// Deletes a product by ID
  /// @param id Product ID to delete
  /// @return true if deleted, false if not found
  public boolean delete(Long id) {
    return products.remove(id) != null;
  }

  /// Adjusts product stock
  /// @param id Product ID
  /// @param adjustment Stock adjustment (positive to add, negative to subtract)
  /// @return Updated product
  /// @throws IllegalArgumentException if product not found or invalid adjustment
  public Product adjustStock(Long id, int adjustment) {
    var product = findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

    var newStock = product.stock() + adjustment;
    if (newStock < 0) {
      throw new IllegalArgumentException("Insufficient stock");
    }

    var updated = new Product(
      product.id(),
      product.name(),
      product.category(),
      product.price(),
      newStock,
      product.launchDate(),
      product.description()
    );

    return update(updated);
  }

  /// Gets total inventory value
  /// @return Total value of all products in stock
  public BigDecimal getTotalInventoryValue() {
    return products.values().stream()
      .map(p -> p.price().multiply(BigDecimal.valueOf(p.stock())))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /// Gets products with low stock
  /// @param threshold Stock threshold (default 10)
  /// @return List of products with stock below threshold
  public List<Product> getLowStockProducts(int threshold) {
    return products.values().stream()
      .filter(p -> p.stock() < threshold && p.stock() > 0)
      .sorted(Comparator.comparingInt(Product::stock))
      .collect(Collectors.toList());
  }

  /// Initializes sample product data
  private void initializeSampleData() {
    save(new Product(null, "Dell XPS 15 Laptop", Category.ELECTRONICS,
      new BigDecimal("25999.99"), 15, LocalDate.of(2024, 1, 15),
      "High-performance laptop with 16GB RAM and 512GB SSD"));

    save(new Product(null, "Samsung Galaxy S24", Category.ELECTRONICS,
      new BigDecimal("18999.00"), 8, LocalDate.of(2024, 2, 1),
      "Latest smartphone with 5G connectivity"));

    save(new Product(null, "Levi's Jeans - Classic Fit", Category.CLOTHING,
      new BigDecimal("899.99"), 45, LocalDate.of(2023, 9, 10),
      "Comfortable denim jeans in classic blue"));

    save(new Product(null, "Nike Air Max Sneakers", Category.CLOTHING,
      new BigDecimal("1899.00"), 20, LocalDate.of(2024, 3, 5),
      "Stylish and comfortable running shoes"));

    save(new Product(null, "Arabica Coffee Beans 1kg", Category.FOOD,
      new BigDecimal("299.99"), 0, LocalDate.of(2024, 1, 1),
      "Premium roasted coffee beans from Ethiopia"));

    save(new Product(null, "The Pragmatic Programmer", Category.BOOKS,
      new BigDecimal("599.00"), 30, LocalDate.of(2023, 6, 20),
      "Essential reading for software developers"));

    save(new Product(null, "Clean Code by Robert Martin", Category.BOOKS,
      new BigDecimal("549.00"), 25, LocalDate.of(2023, 8, 12),
      "Guide to writing maintainable code"));

    save(new Product(null, "Wilson Tennis Racket", Category.SPORTS,
      new BigDecimal("1299.00"), 12, LocalDate.of(2024, 2, 14),
      "Professional-grade tennis racket"));

    save(new Product(null, "Garden Tool Set", Category.HOME,
      new BigDecimal("799.99"), 18, LocalDate.of(2024, 3, 1),
      "Complete 10-piece gardening tool set"));

    save(new Product(null, "Smart LED Bulb Pack", Category.HOME,
      new BigDecimal("449.00"), 5, LocalDate.of(2024, 1, 20),
      "WiFi-enabled color-changing LED bulbs (4-pack)"));
  }
}