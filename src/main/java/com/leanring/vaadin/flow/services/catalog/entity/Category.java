package com.leanring.vaadin.flow.services.catalog.entity;

/// Product category enumeration.
/// Defines available product categories with display names.
public enum Category {
  ELECTRONICS("Electronics", "💻"),
  CLOTHING("Clothing", "👕"),
  FOOD("Food & Beverage", "🍔"),
  BOOKS("Books", "📚"),
  SPORTS("Sports & Outdoor", "⚽"),
  HOME("Home & Garden", "🏠");

  private final String displayName;
  private final String emoji;

  /// Constructor for category with display name and emoji
  /// @param displayName Human-readable category name
  /// @param emoji Category icon emoji
  Category(String displayName, String emoji) {
    this.displayName = displayName;
    this.emoji = emoji;
  }

  /// Gets the display name with emoji
  /// @return Formatted display name
  public String getDisplayName() {
    return emoji + " " + displayName;
  }

  /// Gets just the text label
  /// @return Category label without emoji
  public String getLabel() {
    return displayName;
  }
}