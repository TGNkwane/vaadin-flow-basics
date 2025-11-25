package com.leanring.vaadin.flow.services.catalog.entity;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

/// Product record representing a catalog item.
/// Demonstrates Java records with validation and computed values.
///
/// @param id Unique product identifier
/// @param name Product name (required, 3-100 chars)
/// @param category Product category (required)
/// @param price Product price (required, positive)
/// @param stock Current stock quantity (non-negative)
/// @param launchDate Product launch date
/// @param description Product description
public record Product(
  Long id,

  @NotBlank(message = "Product name is required")
  @Size(min = 3, max = 100, message = "Name must be 3-100 characters")
  String name,

  @NotNull(message = "Category is required")
  Category category,

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.01", message = "Price must be positive")
  BigDecimal price,

  @Min(value = 0, message = "Stock cannot be negative")
  int stock,

  LocalDate launchDate,

  @Size(max = 500, message = "Description max 500 characters")
  String description
) {
  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

  /// Creates a new product with auto-generated ID
  /// @param name Product name
  /// @param category Product category
  /// @param price Product price
  /// @param stock Stock quantity
  /// @param launchDate Launch date
  /// @param description Product description
  /// @return New Product with generated ID
  public static Product create(String name, Category category, BigDecimal price,
                                int stock, LocalDate launchDate, String description) {
    return new Product(ID_GENERATOR.getAndIncrement(), name, category,
                       price, stock, launchDate, description);
  }

  /// Checks if product is in stock
  /// @return true if stock > 0
  public boolean isInStock() {
    return stock > 0;
  }

  /// Gets stock status as display string
  /// @return Formatted stock status
  public String getStockStatus() {
    return switch (stock) {
      case 0 -> "❌ Out of Stock";
      case int s when s < 10 -> "⚠️ Low Stock (" + s + ")";
      default -> "✅ In Stock (" + stock + ")";
    };
  }

  /// Gets formatted price with currency
  /// @return Price string with ZAR currency
  public String getFormattedPrice() {
    return "R " + price.toString();
  }
}