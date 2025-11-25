package com.leanring.vaadin.flow.layouts;

import com.leanring.vaadin.flow.shell.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/// Layouts view showcasing different layout containers.
/// Demonstrates VerticalLayout, HorizontalLayout, SplitLayout, and responsive patterns.
@Route(value = "layouts", layout = MainLayout.class)
@PageTitle("Layouts | Vaadin Guild")
public class LayoutsView extends VerticalLayout {

  /// Constructor initializes the layouts demo
  public LayoutsView() {
    configureLayout();
    buildExamples();
  }

  /// Configures the main layout properties
  private void configureLayout() {
    setSizeFull();
    setPadding(true);
    setSpacing(true);
  }

  /// Builds and adds all layout examples
  private void buildExamples() {
    add(
      buildColumnExample(),
      buildRowExample(),
      buildSplitExample(),
      buildResponsiveExample()
    );
  }

  /// Demonstrates VerticalLayout stacking components vertically.
  /// @return Details panel with column layout example
  private Details buildColumnExample() {
    var column = new VerticalLayout(
      new Paragraph("Step 1 – Capture applicant details"),
      new Paragraph("Step 2 – Review supporting documents"),
      new Paragraph("Step 3 – Approve or request changes")
    );
    column.setPadding(true);
    column.setSpacing(true);
    column.addClassName("layout-bordered");
    column.setWidthFull();

    var details = new Details(new H2("VerticalLayout – onboarding wizard"), column);
    details.setWidthFull();
    details.setOpened(true);
    return details;
  }

  /// Demonstrates HorizontalLayout with alignment controls.
  /// @return Details panel with row layout example
  private Details buildRowExample() {
    var row = new HorizontalLayout(
      new Paragraph("Backlog"),
      new Paragraph("In progress"),
      new Paragraph("Ready to ship")
    );
    row.setWidthFull();
    row.setAlignItems(FlexComponent.Alignment.CENTER);
    row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    row.addClassName("layout-dashed");

    var details = new Details(new H2("HorizontalLayout – workflow swimlane"), row);
    details.setWidthFull();
    details.setOpened(true);
    return details;
  }

  /// Demonstrates SplitLayout with resizable panes.
  /// @return Details panel with split layout example
  private Details buildSplitExample() {
    var editor = new VerticalLayout(
      new Paragraph("Editor"),
      new Paragraph("Simulate form inputs here")
    );
    editor.setSpacing(false);
    editor.setPadding(false);

    var preview = new VerticalLayout(
      new Paragraph("Live preview"),
      new Paragraph("Resize the splitter to see responsive behavior.")
    );
    preview.setSpacing(false);
    preview.setPadding(false);

    var split = new SplitLayout(editor, preview);
    split.setHeight(300, Unit.PIXELS);
    split.setSplitterPosition(35);

    var details = new Details(new H2("SplitLayout – editor vs preview"), split);
    details.setWidthFull();
    details.setOpened(true);
    return details;
  }

  /// Demonstrates responsive layout with fixed and flexible sections.
  /// @return Details panel with flex grow example
  private Details buildResponsiveExample() {
    var intro = new Paragraph(
      "HorizontalLayout lets you mix fixed and flexible sections using setFlexGrow."
    );

    var dashboard = new HorizontalLayout();
    dashboard.setWidthFull();
    dashboard.setSpacing(true);
    dashboard.addClassName("layout-bordered");

    // Fixed-width metrics panel
    var metrics = new VerticalLayout(new Paragraph("KPI: 1 250 orders"));
    metrics.setWidth(150, Unit.PIXELS);
    metrics.addClassName("panel-primary");

    // Flexible timeline (grows to fill space)
    var timeline = new Paragraph("Timeline widget stretches because flex grow is set to 1.");
    timeline.addClassName("no-margin");

    // Fixed-width activity panel
    var activity = new VerticalLayout(new Paragraph("Activity feed"));
    activity.setWidth(200, Unit.PIXELS);
    activity.addClassName("panel-success");

    dashboard.add(metrics, timeline, activity);
    dashboard.setFlexGrow(0, metrics);
    dashboard.setFlexGrow(1, timeline);
    dashboard.setFlexGrow(0, activity);

    var content = new VerticalLayout(intro, dashboard);
    content.setSpacing(true);
    content.setPadding(true);

    var details = new Details(new H2("HorizontalLayout – mixing fixed and flexible panels"), content);
    details.setWidthFull();
    details.setOpened(true);
    return details;
  }
}