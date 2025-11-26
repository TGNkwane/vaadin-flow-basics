package com.leanring.vaadin.flow.hello;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/// Standalone Hello World view - the "simplest" possible Vaadin page.
/// Not part of the main layout, demonstrating a minimal route setup.
///
@Route("hello-world")
@PageTitle("Hello World")
public class HelloWorldView extends VerticalLayout {

  public static final String GREETING = "Hello, World!";
  public static final String DESCRIPTION = "This is the simplest Vaadin Flow page.";

  /// Constructor builds the minimal Hello World UI
  public HelloWorldView() {
    add(
      new H1(GREETING),
      new Paragraph(DESCRIPTION)
    );
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
  }
}
