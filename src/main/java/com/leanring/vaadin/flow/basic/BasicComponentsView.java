package com.leanring.vaadin.flow.basic;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;

/// Basic Vaadin view demonstrating core components and event handling.
/// Showcases TextField, Button with variants, and dynamic content updates.
@Route(value = "basic", layout = MainLayout.class)
@PageTitle("Basic Components | Vaadin Guild")
public class BasicComponentsView extends VerticalLayout {

  private final TextField nameField = new TextField("Your name");
  private final Button greetButton = new Button("Say Hi");
  private final Span messageSpan = new Span(); // usually an inline element

  /// Constructor initializes the greeting demo UI
  public BasicComponentsView() {
    configureComponents();
    configureEventHandlers();
    buildLayout();
  }

  /// Configures component properties and styling
  private void configureComponents() {
    nameField.setPlaceholder("Enter your name...");
    nameField.setWidth("300px");

    greetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    messageSpan.addClassName("greeting-message");
  }

  /// Sets up event handlers for user interactions
  private void configureEventHandlers() {
    greetButton.addClickListener(e -> {
      var name = nameField.getValue();
      messageSpan.setText((name == null || name.isBlank()) ? "👋 Hello there!" : "Hello %s 👋".formatted(name));
    });

    // Allow Enter key to trigger greeting
    nameField.addValueChangeListener(e -> {
      if (!e.getValue().isBlank()) {
        greetButton.click();
      }
    });
  }

  /// Builds and adds components to the layout
  private void buildLayout() {
    add(new H2("Basic Components"), nameField, greetButton, messageSpan);
    setPadding(true);
    setSpacing(true);
  }
}