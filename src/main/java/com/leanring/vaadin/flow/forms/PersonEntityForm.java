package com.leanring.vaadin.flow.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/// Form for PersonEntity demonstrating class-based binding with bindInstanceFields().
///
/// Uses @PropertyId for nested properties:
/// - Direct properties: field name matches property (name, email, phone)
/// - Nested properties: @PropertyId("address.street") maps to getAddress().getStreet()
///
/// Binder can both READ and WRITE because PersonEntity has setters.
@Slf4j
public class PersonEntityForm extends FormLayout {

  private final Binder<PersonEntity> binder = new BeanValidationBinder<>(PersonEntity.class);

  // Direct properties - field names match PersonEntity properties
  private final TextField name = new TextField("Name");
  private final EmailField email = new EmailField("Email");
  private final TextField phone = new TextField("Phone");

  // Nested Address properties - use @PropertyId for path mapping
  @PropertyId("address.street")
  private final TextField street = new TextField("Street");

  @PropertyId("address.city")
  private final TextField city = new TextField("City");

  @PropertyId("address.postalCode")
  private final TextField postalCode = new TextField("Postal Code");

  private final Button saveButton = new Button("Save");
  private final Button clearButton = new Button("Clear");

  private PersonEntity currentEntity = new PersonEntity();
  private Consumer<PersonEntity> saveHandler;

  /// Constructor initializes the form
  public PersonEntityForm() {
    configureFields();
    configureBinder();
    configureButtons();
    buildLayout();
    setEntity(new PersonEntity());  // Initialize with fresh entity
  }

  /// Configures field placeholders
  private void configureFields() {
    name.setPlaceholder("Enter full name");
    name.setRequiredIndicatorVisible(true);

    email.setPlaceholder("user@example.com");
    email.setRequiredIndicatorVisible(true);

    phone.setPlaceholder("+27 XX XXX XXXX");

    street.setPlaceholder("123 Main Street");
    street.setRequiredIndicatorVisible(true);

    city.setPlaceholder("Cape Town");
    city.setRequiredIndicatorVisible(true);

    postalCode.setPlaceholder("8001");
  }

  /// Binds all fields automatically using bindInstanceFields()
  /// - Direct fields: name→getName/setName, email→getEmail/setEmail, etc.
  /// - @PropertyId fields: street→getAddress().getStreet()/setStreet(), etc.
  private void configureBinder() {
    binder
      .forField(street)
      .bind(
        entity -> {
          var address = entity.getAddress();
          return address != null ? address.getStreet() : null;
        },
        (entity, value) -> ensureAddress(entity).setStreet(value)
      );

    binder
      .forField(city)
      .bind(
        entity -> {
          var address = entity.getAddress();
          return address != null ? address.getCity() : null;
        },
        (entity, value) -> ensureAddress(entity).setCity(value)
      );

    binder
      .forField(postalCode)
      .bind(
        entity -> {
          var address = entity.getAddress();
          return address != null ? address.getPostalCode() : null;
        },
        (entity, value) -> ensureAddress(entity).setPostalCode(value)
      );

    // Remaining direct properties (name, email, phone) match field names
    binder.bindInstanceFields(this);
  }

  /// Configures button behavior
  private void configureButtons() {
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(e -> handleSave());

    clearButton.addClickListener(e -> setEntity(new PersonEntity()));
  }

  /// Builds the form layout with sections
  private void buildLayout() {
    setResponsiveSteps(
      new ResponsiveStep("0", 1),
      new ResponsiveStep("500px", 2)
    );

    // Person details
    add(name, email, phone);

    // Address section header
    var addressHeader = new H4("📍 Address");
    add(addressHeader);
    setColspan(addressHeader, 2);

    // Address fields
    add(street, city, postalCode);

    // Buttons
    var buttons = new HorizontalLayout(saveButton, clearButton);
    buttons.setSpacing(true);
    add(buttons);
    setColspan(buttons, 2);
  }

  /// Sets the handler called when form is saved
  public void setSaveHandler(Consumer<PersonEntity> handler) {
    this.saveHandler = handler;
  }

  /// Handles save - validates and notifies handler
  private void handleSave() {
    // With setBean(), values are already in the entity
    // Just validate and notify
    if (binder.validate().isOk()) {
      logEntityDetails();
      if (saveHandler != null) {
        saveHandler.accept(currentEntity);
      }
      // Optionally start fresh after save
      setEntity(new PersonEntity());
    }
  }

  /// Logs the current entity details to demonstrate data binding
  private void logEntityDetails() {
    var addr = currentEntity.getAddress();
    log.info(
      "Saved PersonEntity: name='{}', email='{}', phone='{}', street='{}', city='{}', postal='{}'",
      currentEntity.getName(),
      currentEntity.getEmail(),
      currentEntity.getPhone(),
      addr != null ? addr.getStreet() : null,
      addr != null ? addr.getCity() : null,
      addr != null ? addr.getPostalCode() : null
    );
  }

  /// Sets an entity for editing (unbuffered - changes write directly)
  /// @param entity Entity to edit, or new PersonEntity() for fresh form
  public void setEntity(PersonEntity entity) {
    this.currentEntity = entity;
    if (entity.getAddress() == null) {
      entity.setAddress(new Address());
    }
    // setBean() = unbuffered mode: field changes write directly to entity
    binder.setBean(currentEntity);
  }

  private Address ensureAddress(PersonEntity entity) {
    if (entity.getAddress() == null) {
      entity.setAddress(new Address());
    }
    return entity.getAddress();
  }

  /// Returns the binder
  public Binder<PersonEntity> getBinder() {
    return binder;
  }
}
