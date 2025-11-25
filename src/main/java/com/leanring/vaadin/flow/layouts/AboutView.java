    package com.leanring.vaadin.flow.layouts;

import com.leanring.vaadin.flow.shell.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/// About view demonstrating Vaadin Flow features.
@Route(value = "about", layout = MainLayout.class)
@PageTitle("About | Vaadin Guild")
public class AboutView extends VerticalLayout {

  public AboutView() {
    add(
      new H2("About Vaadin Flow"),
      new Paragraph("""
        Vaadin Flow is a Java framework for building modern web applications.
        It provides a server-side programming model with automatic UI updates.
        """),
      new H3("Key Features"),
      createFeatureList(),
      new Hr(),
      new Paragraph("Built with Java 25 and Vaadin 24 LTS")
    );
    setPadding(true);
  }

  private UnorderedList createFeatureList() {
    return new UnorderedList(
      new ListItem("Server-side rendering with Java"),
      new ListItem("Type-safe component API"),
      new ListItem("Automatic browser communication"),
      new ListItem("Rich component library"),
      new ListItem("Spring Boot integration")
    );
  }
}