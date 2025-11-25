package com.leanring.vaadin.flow.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;

/// Dialog view demonstrating modal dialogs and notifications.
/// Showcases different dialog types and notification variants.
@Route(value = "dialog", layout = MainLayout.class)
@PageTitle("Dialogs | Vaadin Guild")
public class DialogView extends VerticalLayout {

  /// Constructor initializes the dialog demo UI
  public DialogView() {
    add(
      new H2("Dialog & Notification Demo"),
      createDialogSection(),
      createNotificationSection()
    );

    setPadding(true);
    setSpacing(true);
  }

  /// Creates dialog demonstration section
  /// @return VerticalLayout with dialog buttons
  private VerticalLayout createDialogSection() {
    var section = new VerticalLayout();
    section.add(new H3("💬 Dialogs"));

    var simpleDialogBtn = new Button("Simple Dialog", e -> showSimpleDialog());
    var confirmDialogBtn = new Button("Confirmation Dialog", e -> showConfirmDialog());
    var formDialogBtn = new Button("Form Dialog", e -> showFormDialog());

    section.add(new HorizontalLayout(simpleDialogBtn, confirmDialogBtn, formDialogBtn));
    return section;
  }

  /// Creates notification demonstration section
  /// @return VerticalLayout with notification buttons
  private VerticalLayout createNotificationSection() {
    var section = new VerticalLayout();
    section.add(new H3("🔔 Notifications"));

    var successBtn = new Button("Success", e -> showNotification("Success!", NotificationVariant.LUMO_SUCCESS));
    successBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

    var errorBtn = new Button("Error", e -> showNotification("Error occurred!", NotificationVariant.LUMO_ERROR));
    errorBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

    var warningBtn = new Button("Warning", e -> showNotification("Warning message", NotificationVariant.LUMO_WARNING));
    warningBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

    section.add(new HorizontalLayout(successBtn, errorBtn, warningBtn));
    return section;
  }

  /// Shows a simple informational dialog
  private void showSimpleDialog() {
    var dialog = new Dialog();
    dialog.setHeaderTitle("Information");

    dialog.add(new Paragraph("This is a simple dialog with information."));

    var closeBtn = new Button("Close", e -> dialog.close());
    closeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    dialog.getFooter().add(closeBtn);
    dialog.open();
  }

  /// Shows a confirmation dialog with action buttons
  private void showConfirmDialog() {
    var dialog = new Dialog();
    dialog.setHeaderTitle("⚠️ Confirm Action");

    dialog.add(new Paragraph("Are you sure you want to proceed with this action?"));

    var cancelBtn = new Button("Cancel", e -> dialog.close());
    var confirmBtn = new Button("Confirm", e -> {
      showNotification("✅ Action confirmed!", NotificationVariant.LUMO_SUCCESS);
      dialog.close();
    });
    confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

    var footer = new HorizontalLayout(cancelBtn, confirmBtn);
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    footer.setWidthFull();

    dialog.getFooter().add(footer);
    dialog.open();
  }

  /// Shows a dialog with form elements
  private void showFormDialog() {
    var dialog = new Dialog();
    dialog.setHeaderTitle("📝 Quick Form");

    var layout = new VerticalLayout();
    var nameField = new com.vaadin.flow.component.textfield.TextField("Name");
    var emailField = new com.vaadin.flow.component.textfield.EmailField("Email");

    layout.add(nameField, emailField);
    dialog.add(layout);

    var cancelBtn = new Button("Cancel", e -> dialog.close());
    var submitBtn = new Button("Submit", e -> {
      if (!nameField.isEmpty() && !emailField.isEmpty()) {
        showNotification("✅ Form submitted!", NotificationVariant.LUMO_SUCCESS);
        dialog.close();
      } else {
        showNotification("❌ Please fill all fields", NotificationVariant.LUMO_ERROR);
      }
    });
    submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    var footer = new HorizontalLayout(cancelBtn, submitBtn);
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    footer.setWidthFull();

    dialog.getFooter().add(footer);
    dialog.open();
  }

  /// Shows a notification with specified variant
  /// @param message Message to display
  /// @param variant Notification style variant
  private void showNotification(String message, NotificationVariant variant) {
    var notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(variant);
  }
}