package com.leanring.vaadin.flow.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/// Person record demonstrating Java records with validation.
/// Uses Jakarta validation annotations for form binding.
///
/// @param name Person's full name (required, 2-50 chars)
/// @param email Person's email address (required, valid format)
/// @param phone Optional phone number (10-15 chars if provided)
public record Person(
  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
  String name,

  @NotBlank(message = "Email is required")
  @Email(message = "Must be a valid email address")
  String email,

  @Size(min = 10, max = 15, message = "Phone must be 10-15 characters")
  String phone
) {
  /// Compact constructor for additional validation
  public Person {
    // Normalize phone number if provided
    if (phone != null) {
      phone = phone.replaceAll("[^0-9+]", "");
    }
  }
}