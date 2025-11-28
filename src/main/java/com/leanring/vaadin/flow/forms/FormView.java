package com.leanring.vaadin.flow.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;

/// Form view demonstrating reusable form components.
/// Shows three forms: PersonForm (record), PersonEntityForm (class), ContactForm (simple).
///
/// @see PersonForm
/// @see PersonEntityForm
/// @see ContactForm
@Route(value = "form", layout = MainLayout.class)
@PageTitle("Forms | Vaadin Guild")
public class FormView extends VerticalLayout {

  private final PersonForm personForm = new PersonForm();
  private final PersonEntityForm entityForm = new PersonEntityForm();
  private final ContactForm contactForm = new ContactForm();

  /// Constructor builds the view with all form examples
  public FormView() {
    configureFormHandlers();
    buildLayout();
  }

  /// Configures save/submit handlers for all forms
  private void configureFormHandlers() {
    personForm.setSaveHandler(person -> {
      showNotification(
        "✅ Record saved: %s (%s)".formatted(person.name(), person.email()),
        NotificationVariant.LUMO_SUCCESS
      );
    });

    entityForm.setSaveHandler(entity -> {
      var addr = entity.getAddress();
      showNotification(
        "✅ Entity saved: %s in %s, %s".formatted(
          entity.getName(),
          addr.getCity(),
          addr.getStreet()
        ),
        NotificationVariant.LUMO_SUCCESS
      );
    });

    contactForm.setSubmitHandler((email, message) -> {
      showNotification(
        "✅ Message sent from: %s".formatted(email),
        NotificationVariant.LUMO_SUCCESS
      );
    });
  }

  /// Builds the layout with forms in sections
  private void buildLayout() {
    add(
      new H2("Form Components Demo"),
      new Paragraph("Comparing Record vs Class binding, and nested object binding.")
    );

    // Row 1: Record-based form vs Entity-based form
    var row1 = new HorizontalLayout();
    row1.setWidthFull();
    row1.setSpacing(true);

    var personSection = createSection(
      "PersonForm (Record)",
      "Uses Person record. Binder reads only, manual object creation on save.",
      personForm
    );

    var entitySection = createSection(
      "PersonEntityForm (Class + Nested)",
      "Uses PersonEntity class with nested Address. Binder reads AND writes directly.",
      entityForm
    );

    row1.add(personSection, entitySection);

    // Row 2: Simple contact form
    var contactSection = createSection(
      "ContactForm (Simple)",
      "Minimal form without Binder - just fields and manual handling.",
      contactForm
    );
    contactSection.setMaxWidth("50%");

    add(new Hr(), row1, new Hr(), contactSection);

    setPadding(true);
    setSpacing(true);
  }

  /// Creates a section with header, description, and form
  private VerticalLayout createSection(String title, String description, Component form) {
    var section = new VerticalLayout();
    section.setPadding(false);
    section.add(new H3(title), new Paragraph(description), form);
    return section;
  }

  /// Shows a notification with the given variant
  /// @param message Message to display
  /// @param variant Notification style variant
  private void showNotification(String message, NotificationVariant variant) {
    var notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(variant);
  }
}