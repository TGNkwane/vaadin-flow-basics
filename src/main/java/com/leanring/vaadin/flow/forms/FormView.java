package com.leanring.vaadin.flow.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;

/// Form view demonstrating data binding and validation.
/// Showcases Binder, FormLayout, and validation feedback.
///
/// @see Binder
/// @see FormLayout
@Route(value = "form", layout = MainLayout.class)
@PageTitle("Forms | Vaadin Guild")
public class FormView extends VerticalLayout {

  private final Binder<Person> binder = new BeanValidationBinder<>(Person.class);
  private final TextField nameField = new TextField("Name");
  private final EmailField emailField = new EmailField("Email");
  private final TextField phoneField = new TextField("Phone");

  /// Constructor initializes the form with validation and binding
  public FormView() {
    configureFields();
    configureBinder();
    buildLayout();
    resetForm();
  }

  /// Configures form field properties
  private void configureFields() {
    nameField.setPlaceholder("Enter full name");
    nameField.setRequiredIndicatorVisible(true);

    emailField.setPlaceholder("user@example.com");
    emailField.setRequiredIndicatorVisible(true);

    phoneField.setPlaceholder("+27 XX XXX XXXX");
  }

  /// Configures binder with field bindings
  private void configureBinder() {
    binder.forField(nameField).bind("name");
    binder.forField(emailField).bind("email");
    binder.forField(phoneField).bind("phone");
  }

  /// Builds and configures the layout
  private void buildLayout() {
    var saveButton = new Button("Save", e -> handleSave());
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    var clearButton = new Button("Clear", e -> resetForm());

    var formLayout = new FormLayout(
      nameField,
      emailField,
      phoneField
    );
    formLayout.setResponsiveSteps(
      new FormLayout.ResponsiveStep("0", 1),
      new FormLayout.ResponsiveStep("500px", 2)
    );

    add(
      new H2("Form Validation Demo"),
      formLayout,
      new VerticalLayout(saveButton, clearButton)
    );

    setPadding(true);
    setMaxWidth("800px");
  }

  /// Handles form submission with validation
  private void handleSave() {
    if (binder.validate().isOk()) {
      var person = new Person(
        nameField.getValue(),
        emailField.getValue(),
        phoneField.getValue()
      );
      showSuccessNotification("✅ Saved: %s (%s)".formatted(person.name(), person.email()));
      resetForm();
    } else {
      showErrorNotification("❌ Please fix validation errors");
    }
  }

  /// Resets form fields and clears validation state
  private void resetForm() {
      binder.readBean(new Person("", "", ""));
  }

  /// Shows success notification
  /// @param message Success message to display
  private void showSuccessNotification(String message) {
    var notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  /// Shows error notification
  /// @param message Error message to display
  private void showErrorNotification(String message) {
    var notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}