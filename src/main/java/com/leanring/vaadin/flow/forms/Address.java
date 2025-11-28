package com.leanring.vaadin.flow.forms;

import jakarta.validation.constraints.NotBlank;

/// Address class - demonstrates a nested object for binding.
/// Mutable class with getters/setters for full binder support.
public class Address {

  @NotBlank(message = "Street is required")
  private String street;

  @NotBlank(message = "City is required")
  private String city;

  private String postalCode;

  public Address() {}

  public Address(String street, String city, String postalCode) {
    this.street = street;
    this.city = city;
    this.postalCode = postalCode;
  }

  // Getters and setters for binder
  public String getStreet() { return street; }
  public void setStreet(String street) { this.street = street; }

  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }

  public String getPostalCode() { return postalCode; }
  public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
}
