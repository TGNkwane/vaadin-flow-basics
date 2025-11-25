package com.leanring.vaadin.flow.grid;

import com.leanring.vaadin.flow.forms.Person;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.leanring.vaadin.flow.shell.MainLayout;
import java.util.List;

/// Grid view demonstrating Vaadin Grid component capabilities.
/// Showcases filtering, sorting, and custom column configuration.
///
/// @see Grid
/// @see ListDataProvider
@Route(value = "grid", layout = MainLayout.class)
public class GridView extends VerticalLayout {

  private final Grid<Person> grid = new Grid<>(Person.class, false);
  private final ListDataProvider<Person> dataProvider;

  /// Constructor initializes grid with sample data and filtering
  public GridView() {
    // Sample data using Java 25 text blocks and records
    var people = List.of(
      new Person("Tebogo Nkwane", "tebogo@example.co.za", "+27123456789"),
      new Person("Ayanda Zulu", "ayanda@example.com", "+27987654321"),
      new Person("Sipho Mthembu", "sipho@example.co.za", "+27555123456"),
      new Person("Nomvula Dlamini", "nomvula@example.co.za", "+27444987654"),
      new Person("Thabo Mokoena", "thabo@example.com", "+27333678901")
    );

    dataProvider = new ListDataProvider<>(people);
    grid.setDataProvider(dataProvider);

    // Configure grid columns
    grid.addColumn(Person::name)
      .setHeader("Name")
      .setSortable(true)
      .setAutoWidth(true);

    grid.addColumn(Person::email)
      .setHeader("Email")
      .setSortable(true)
      .setAutoWidth(true);

    grid.addColumn(Person::phone)
      .setHeader("Phone")
      .setAutoWidth(true);

    grid.setHeight("400px");

    // Add filter field
    var filterField = createFilterField();

    add(
      new H2("Grid Demo"),
      filterField,
      grid
    );

    setPadding(true);
    setMaxWidth("1000px");
  }

  /// Creates a filter text field with real-time filtering
  /// @return Configured TextField for filtering grid data
  private TextField createFilterField() {
    var filterField = new TextField();
    filterField.setPlaceholder("🔍 Filter by name or email...");
    filterField.setWidth("100%");
    filterField.setValueChangeMode(ValueChangeMode.LAZY);

    filterField.addValueChangeListener(e -> {
      var filterText = e.getValue().toLowerCase().trim();
      dataProvider.setFilter(person ->
        filterText.isEmpty() ||
        person.name().toLowerCase().contains(filterText) ||
        person.email().toLowerCase().contains(filterText)
      );
    });

    return filterField;
  }
}