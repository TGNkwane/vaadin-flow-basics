package com.leanring.vaadin.flow.shell;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/// Home view - landing page for the Vaadin Guild Playground.
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | Vaadin Guild")
public class HomeView extends VerticalLayout {

  public HomeView() {
    add(
      new H1("Welcome to Vaadin Guild Playground"),
      new Paragraph("""
        This demo application showcases Vaadin Flow capabilities using Java 25.
        Navigate through the menu to explore different components and patterns.
        """),
      createTechStack()
    );
    setMaxWidth("800px");
    setPadding(true);
  }

  private VerticalLayout createTechStack() {
    var section = new VerticalLayout();
    section.setPadding(false);

    var callToAction = new Paragraph("Use the menu on the left to explore each demo!");
    callToAction.getStyle()
      .set("color", "var(--lumo-primary-color)")
      .set("font-weight", "bold");

    section.add(
      new H2("Tech Stack"),
      new UnorderedList(
        new ListItem("Java 25 - Latest features including records and pattern matching"),
        new ListItem("Vaadin Flow 24 LTS - Server-side UI framework"),
        new ListItem("Spring Boot 3.3+ - Application framework"),
        new ListItem("Maven - Build and dependency management")
      ),
      new Hr(),
      callToAction
    );

    return section;
  }
}