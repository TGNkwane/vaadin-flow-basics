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
@Route(value = "hello", layout = MainLayout.class)
@PageTitle("Hello | Vaadin Guild")
public class HelloView extends VerticalLayout {

  /// Constructor initializes the greeting demo UI with interactive components
  public HelloView() {
    TextField nameField = new TextField("Your name");
    nameField.setPlaceholder("Enter your name...");
    nameField.setWidth("300px");

    Button greetButton = new Button("Say Hi");
    greetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Span messageSpan = new Span();
    messageSpan.getStyle()
      .set("font-size", "1.5em")
      .set("color", "var(--lumo-primary-color)");

    // Handle button click to display personalized greeting
    greetButton.addClickListener(e -> {
      var name = nameField.getValue();
      messageSpan.setText(name.isBlank() ? "👋 Hello there!" : "Hello %s 👋".formatted(name));
    });

    // Allow Enter key to trigger greeting
    nameField.addValueChangeListener(e -> {
      if (!e.getValue().isBlank()) {
        greetButton.click();
      }
    });

    add(new H2("Hello Vaadin"), nameField, greetButton, messageSpan);
    setPadding(true);
    setSpacing(true);
  }
}