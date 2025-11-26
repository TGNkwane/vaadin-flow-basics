package com.leanring.vaadin.flow.hello;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/// Unit tests for HelloWorldView.
/// Tests component creation without Spring context.
class HelloWorldViewTest {

  private HelloWorldView view;

  @BeforeEach
  void setUp() {
    view = new HelloWorldView();
  }

  @Test
  @DisplayName("View should be instantiated successfully")
  void viewShouldBeInstantiated() {
    assertNotNull(view, "HelloWorldView should not be null");
  }

  @Test
  @DisplayName("View should contain exactly 2 child components")
  void viewShouldContainTwoComponents() {
    assertEquals(2, view.getComponentCount(), "View should have H1 and Paragraph");
  }

  @Test
  @DisplayName("Greeting constant should be 'Hello, World!'")
  void greetingConstantShouldBeCorrect() {
    assertEquals("Hello, World!", HelloWorldView.GREETING);
  }

  @Test
  @DisplayName("Description constant should be set")
  void descriptionConstantShouldBeSet() {
    assertNotNull(HelloWorldView.DESCRIPTION);
    assertFalse(HelloWorldView.DESCRIPTION.isBlank());
  }

  @Test
  @DisplayName("View should be centered")
  void viewShouldBeCentered() {
    assertEquals(
      com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER,
      view.getAlignItems()
    );
    assertEquals(
      com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER,
      view.getJustifyContentMode()
    );
  }
}
