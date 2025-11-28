package com.leanring.vaadin.flow.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/// Form for Person RECORD demonstrating record-specific binding.
///
/// Key points for RECORDS:
/// - Immutable: no setters, can't use setBean() (throws exception)
/// - Use readBean() or readRecord() to load data into fields
/// - Use writeRecord() to create NEW record from field values
/// - Field names must match record component names for bindInstanceFields()
///
/// Binding mode: BUFFERED (manual read/write)
/// - Changes stay in fields until explicit save
/// - On save: writeRecord() creates new Person instance
@Slf4j
public class PersonForm extends FormLayout {

  private final Binder<Person> binder = new BeanValidationBinder<>(Person.class);

  // Field names MUST match Person record components: name, email, phone
  private final TextField name = new TextField("Name");
  private final EmailField email = new EmailField("Email");
  private final TextField phone = new TextField("Phone");

  private final Button saveButton = new Button("Save");
  private final Button clearButton = new Button("Clear");

  private Consumer<Person> saveHandler;

  /// Constructor initializes the form
  public PersonForm() {
    configureFields();
    configureBinder();
    configureButtons();
    buildLayout();
    clear(); // Initialize with empty record
  }

  /// Configures field placeholders and required indicators
  private void configureFields() {
    name.setPlaceholder("Enter full name");
    name.setRequiredIndicatorVisible(true);

    email.setPlaceholder("user@example.com");
    email.setRequiredIndicatorVisible(true);

    phone.setPlaceholder("+27 XX XXX XXXX");
  }

  /// Binds fields by matching names to record components
  private void configureBinder() {
    // Auto-binds: name→name(), email→email(), phone→phone()
    binder.bindInstanceFields(this);
    log.debug("PersonForm binder configured with {} fields", binder.getFields().count());
  }

  /// Configures button click handlers
  private void configureButtons() {
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(e -> handleSave());

    clearButton.addClickListener(e -> clear());
  }

  /// Builds responsive form layout
  private void buildLayout() {
    setResponsiveSteps(
      new ResponsiveStep("0", 1),
      new ResponsiveStep("500px", 2)
    );

    add(name, email, phone);

    var buttons = new HorizontalLayout(saveButton, clearButton);
    buttons.setSpacing(true);
    add(buttons);
    setColspan(buttons, 2);
  }

  /// Sets the handler called on successful save
  public void setSaveHandler(Consumer<Person> handler) {
    this.saveHandler = handler;
  }

  /// Handles save - uses writeRecord() to create new Person
  private void handleSave() {
    try {
      // writeRecord() validates and creates NEW record from field values
      Person person = binder.writeRecord();
      
      log.info("Person record saved: {} ({})", person.name(), person.email());
      
      if (saveHandler != null) {
        saveHandler.accept(person);
      }
      clear();
    } catch (ValidationException e) {
      log.warn("Validation failed: {}", e.getValidationErrors());
    }
  }

  /// Clears form with empty record
  public void clear() {
    binder.readBean(new Person("", "", ""));
  }

  /// Loads existing Person for viewing/editing
  public void setPerson(Person person) {
    binder.readBean(person);
  }

  /// Returns binder for external validation
  public Binder<Person> getBinder() {
    return binder;
  }
}
