package com.leanring.vaadin.flow.services.catalog.boundary;

import com.leanring.vaadin.flow.services.catalog.control.ProductService;
import com.leanring.vaadin.flow.services.catalog.entity.Category;
import com.leanring.vaadin.flow.services.catalog.entity.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import java.math.BigDecimal;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;

/// Product view demonstrating BCE pattern with CRUD operations.
@Route(value = "products", layout = MainLayout.class)
@PageTitle("Products | Vaadin Guild")
public class ProductView extends VerticalLayout {

  private final ProductService service;
  private final Grid<Product> grid = new Grid<>(Product.class, false);
  private final ListDataProvider<Product> dataProvider;

  /// Constructor initializes the catalog view with dependency injection
  /// @param service Product service for business operations
  public ProductView(ProductService service) {
    this.service = service;
    this.dataProvider = new ListDataProvider<>(service.findAll());

    setSizeFull();
    setPadding(false);

    add(
      createHeader(),
      createToolbar(),
      createGridLayout()
    );
  }

  /// Creates the header section with title and statistics
  /// @return HorizontalLayout with header content
  private HorizontalLayout createHeader() {
    var title = new H2("🛒 Product Catalog (BCE Demo)");
    title.getStyle().set("margin", "0");

    var stats = new Span("Total Products: " + service.findAll().size());
    stats.getStyle()
      .set("color", "var(--lumo-secondary-text-color)")
      .set("font-size", "var(--lumo-font-size-s)");

    var header = new HorizontalLayout(title, stats);
    header.setWidthFull();
    header.setAlignItems(FlexComponent.Alignment.BASELINE);
    header.setPadding(true);
    header.getStyle().set("background", "var(--lumo-contrast-5pct)");

    return header;
  }

  /// Creates toolbar with filters and action buttons
  /// @return HorizontalLayout with toolbar components
  private HorizontalLayout createToolbar() {
    var searchField = new TextField();
    searchField.setPlaceholder("🔍 Search products...");
    searchField.setWidth("300px");
    searchField.setValueChangeMode(ValueChangeMode.LAZY);
    searchField.addValueChangeListener(e -> applyFilters(e.getValue(), null));

    var categoryFilter = new ComboBox<Category>("Category");
    categoryFilter.setItems(Category.values());
    categoryFilter.setItemLabelGenerator(Category::getDisplayName);
    categoryFilter.setPlaceholder("All categories");
    categoryFilter.setClearButtonVisible(true);
    categoryFilter.addValueChangeListener(e -> applyFilters(searchField.getValue(), e.getValue()));

    var addButton = new Button("+ Add Product", e -> openProductDialog(null));
    addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    var toolbar = new HorizontalLayout(searchField, categoryFilter, addButton);
    toolbar.setWidthFull();
    toolbar.setPadding(true);
    toolbar.setAlignItems(FlexComponent.Alignment.END);
    toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

    return toolbar;
  }

  /// Creates grid layout with product data
  /// @return VerticalLayout containing the grid
  private VerticalLayout createGridLayout() {
    grid.setDataProvider(dataProvider);
    grid.setSizeFull();

    // Configure columns with custom renderers
    grid.addColumn(Product::id)
      .setHeader("ID")
      .setWidth("80px")
      .setFlexGrow(0);

    grid.addColumn(Product::name)
      .setHeader("Product Name")
      .setSortable(true)
      .setAutoWidth(true);

    grid.addColumn(product -> product.category().getDisplayName())
      .setHeader("Category")
      .setSortable(true)
      .setAutoWidth(true);

    grid.addColumn(Product::getFormattedPrice)
      .setHeader("Price")
      .setSortable(true)
      .setAutoWidth(true);

    // Custom renderer for stock status with colored badges
    grid.addColumn(new ComponentRenderer<>(product -> {
      var badge = new Span(product.getStockStatus());
      badge.getElement().getThemeList().add("badge");
      if (product.stock() == 0) {
        badge.getStyle().set("color", "var(--lumo-error-color)");
      } else if (product.stock() < 10) {
        badge.getStyle().set("color", "var(--lumo-warning-color)");
      } else {
        badge.getStyle().set("color", "var(--lumo-success-color)");
      }
      return badge;
    }))
      .setHeader("Stock")
      .setAutoWidth(true);

    grid.addColumn(product -> product.launchDate() != null ? product.launchDate().toString() : "N/A")
      .setHeader("Launch Date")
      .setAutoWidth(true);

    // Actions column with edit and delete buttons
    grid.addComponentColumn(this::createActionButtons)
      .setHeader("Actions")
      .setAutoWidth(true)
      .setFlexGrow(0);

    var layout = new VerticalLayout(grid);
    layout.setSizeFull();
    layout.setPadding(false);

    return layout;
  }

  /// Creates action buttons for grid rows
  /// @param product Product to edit, or null for new product
  private void openProductDialog(Product product) {
    var dialog = new Dialog();
    dialog.setHeaderTitle(product == null ? "➕ Add Product" : "✏️ Edit Product");
    dialog.setWidth("600px");

    var nameField = new TextField("Product Name");
    nameField.setRequiredIndicatorVisible(true);
    nameField.setWidthFull();

    var categoryCombo = new ComboBox<Category>("Category");
    categoryCombo.setItems(Category.values());
    categoryCombo.setItemLabelGenerator(Category::getDisplayName);
    categoryCombo.setRequiredIndicatorVisible(true);
    categoryCombo.setWidthFull();

    var priceField = new NumberField("Price (ZAR)");
    priceField.setRequiredIndicatorVisible(true);
    priceField.setMin(0.01);
    priceField.setStep(0.01);
    priceField.setWidthFull();

    var stockField = new IntegerField("Stock Quantity");
    stockField.setMin(0);
    stockField.setValue(0);
    stockField.setWidthFull();

    var launchDatePicker = new DatePicker("Launch Date");
    launchDatePicker.setWidthFull();

    var descriptionArea = new TextArea("Description");
    descriptionArea.setMaxLength(500);
    descriptionArea.setWidthFull();

    var formLayout = new FormLayout(
      nameField,
      categoryCombo,
      priceField,
      stockField,
      launchDatePicker,
      descriptionArea
    );
    formLayout.setResponsiveSteps(
      new FormLayout.ResponsiveStep("0", 1),
      new FormLayout.ResponsiveStep("500px", 2)
    );
    formLayout.setColspan(descriptionArea, 2);

    var binder = new BeanValidationBinder<>(Product.class);
    binder.forField(nameField).asRequired("Name is required")
      .bind(Product::name, null);
    binder.forField(categoryCombo).asRequired("Category is required")
      .bind(Product::category, null);
    binder.forField(priceField)
      .asRequired("Price is required")
      .withConverter(
        value -> value != null ? BigDecimal.valueOf(value) : BigDecimal.ZERO,
        BigDecimal::doubleValue,
        "Enter a valid price"
      )
      .bind(Product::price, null);
    binder.forField(stockField)
      .asRequired("Stock quantity is required")
      .withValidator(value -> value != null && value >= 0, "Stock must be zero or greater")
      .bind(Product::stock, null);
    binder.forField(launchDatePicker).bind(Product::launchDate, null);
    binder.forField(descriptionArea).bind(Product::description, null);

    if (product != null) {
      binder.readBean(product);
    } else {
      nameField.clear();
      categoryCombo.clear();
      priceField.clear();
      stockField.setValue(0);
      launchDatePicker.clear();
      descriptionArea.clear();
    }

    dialog.add(formLayout);

    var cancelBtn = new Button("Cancel", e -> dialog.close());
    var saveBtn = new Button("Save", e -> {
      if (binder.validate().isOk()) {
        var updated = new Product(
          product != null ? product.id() : null,
          nameField.getValue(),
          categoryCombo.getValue(),
          BigDecimal.valueOf(priceField.getValue() != null ? priceField.getValue() : 0d),
          stockField.getValue() != null ? stockField.getValue() : 0,
          launchDatePicker.getValue(),
          descriptionArea.getValue()
        );
        saveProduct(product, updated);
        dialog.close();
      } else {
        showNotification("❌ Please fix validation errors", NotificationVariant.LUMO_ERROR);
      }
    });
    saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    var footer = new HorizontalLayout(cancelBtn, saveBtn);
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    footer.setWidthFull();

    dialog.getFooter().add(footer);
    dialog.open();
  }

  /// Saves product (create or update)
  /// @param original Original product (null for new)
  /// @param updated Updated product data
  private void saveProduct(Product original, Product updated) {
    try {
      if (original == null) {
        service.save(updated);
        showNotification("✅ Product added successfully", NotificationVariant.LUMO_SUCCESS);
      } else {
        service.update(updated);
        showNotification("✅ Product updated successfully", NotificationVariant.LUMO_SUCCESS);
      }
      refreshGrid();
    } catch (Exception e) {
      showNotification("❌ Error: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
    }
  }

  /// Shows delete confirmation dialog
  /// @param product Product to delete
  private void confirmDelete(Product product) {
    var dialog = new Dialog();
    dialog.setHeaderTitle("⚠️ Confirm Delete");

    dialog.add(new Paragraph(
      "Are you sure you want to delete '%s'?".formatted(product.name())
    ));

    var cancelBtn = new Button("Cancel", e -> dialog.close());
    var deleteBtn = new Button("Delete", e -> {
      service.delete(product.id());
      refreshGrid();
      showNotification("✅ Product deleted", NotificationVariant.LUMO_SUCCESS);
      dialog.close();
    });
    deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

    var footer = new HorizontalLayout(cancelBtn, deleteBtn);
    footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    footer.setWidthFull();

    dialog.getFooter().add(footer);
    dialog.open();
  }

  /// Applies search and category filters
  /// @param searchTerm Search text (nullable)
  /// @param category Category filter (nullable)
  private void applyFilters(String searchTerm, Category category) {
    dataProvider.setFilter(product -> {
      boolean matchesSearch = searchTerm == null || searchTerm.isBlank() ||
        product.name().toLowerCase().contains(searchTerm.toLowerCase()) ||
        (product.description() != null && product.description().toLowerCase().contains(searchTerm.toLowerCase()));

      boolean matchesCategory = category == null || product.category() == category;

      return matchesSearch && matchesCategory;
    });
  }

  /// Refreshes grid data from service
  private void refreshGrid() {
    dataProvider.getItems().clear();
    dataProvider.getItems().addAll(service.findAll());
    dataProvider.refreshAll();
  }

  /// Creates action buttons for a grid row
  /// @param product The product for this row
  /// @return HorizontalLayout with edit and delete buttons
  private HorizontalLayout createActionButtons(Product product) {
    var editBtn = new Button("Edit", e -> openProductDialog(product));
    editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

    var deleteBtn = new Button("Delete", e -> confirmDelete(product));
    deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

    return new HorizontalLayout(editBtn, deleteBtn);
  }

  /// Shows notification with variant
  /// @param message Notification message
  /// @param variant Notification style variant
  private void showNotification(String message, NotificationVariant variant) {
    var notification = Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(variant);
  }
}