package com.leanring.vaadin.flow.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;

import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

/// Simple contact form WITHOUT Binder - demonstrates manual field handling.
///
/// Key points:
/// - No Binder, no bean binding - just raw field values
/// - Manual validation (checking isBlank())
/// - Good for simple forms that don't map to a data class
///
/// When to use this approach:
/// - One-off forms (contact, feedback)
/// - Forms where data doesn't map to a bean/record
/// - Quick prototypes
@Slf4j
public class ContactForm extends FormLayout {

  private final EmailField emailField = new EmailField("Your Email");
  private final TextArea messageArea = new TextArea("Message");
  private final Button sendButton = new Button("Send Message");

  private BiConsumer<String, String> submitHandler;

  /// Constructor builds the simple contact form
  public ContactForm() {
    configureFields();
    buildLayout();
  }

  /// Configures field properties
  private void configureFields() {
    emailField.setPlaceholder("your@email.com");
    emailField.setRequiredIndicatorVisible(true);
    emailField.setWidthFull();

    messageArea.setPlaceholder("Type your message here...");
    messageArea.setMinHeight("100px");
    messageArea.setMaxLength(500);
    messageArea.setWidthFull();

    sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    sendButton.addClickListener(e -> handleSubmit());
  }

  /// Builds the form layout
  private void buildLayout() {
    setResponsiveSteps(new ResponsiveStep("0", 1));
    add(emailField, messageArea, sendButton);
  }

  /// Sets the handler called on form submission
  public void setSubmitHandler(BiConsumer<String, String> handler) {
    this.submitHandler = handler;
  }

  /// Handles form submission with manual validation
  private void handleSubmit() {
    var email = emailField.getValue();
    var message = messageArea.getValue();

    if (email.isBlank() || message.isBlank()) {
      log.debug("Contact form validation failed: email or message blank");
      return;
    }

    log.info("Contact form submitted from: {}", email);
    
    if (submitHandler != null) {
      submitHandler.accept(email, message);
    }
    clear();
  }

  /// Clears the form fields
  public void clear() {
    emailField.clear();
    messageArea.clear();
  }
}
