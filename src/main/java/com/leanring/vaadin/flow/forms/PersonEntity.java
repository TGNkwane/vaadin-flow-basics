package com.leanring.vaadin.flow.forms;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/// PersonEntity - mutable class with nested Address.
/// Demonstrates traditional Java bean pattern for form binding.
///
/// Unlike records:
/// - Has setters binder can write directly to the object
/// - Can have nested objects nested property binding works
/// - Mutable same instance can be updated
public class PersonEntity {

  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Must be a valid email address")
  private String email;

  @Size(min = 10, max = 15, message = "Phone must be 10-15 characters")
  private String phone;

  @Valid  // Enables validation on nested object
  private Address address = new Address();

  public PersonEntity() {}

  public PersonEntity(String name, String email, String phone) {
    this.name = name;
    this.email = email;
    this.phone = phone;
  }

  // Getters and setters
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public Address getAddress() { return address; }
  public void setAddress(Address address) { this.address = address; }
}
