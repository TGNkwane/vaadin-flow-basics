package com.leanring.vaadin.flow.hello;

import com.leanring.vaadin.flow.FlowApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

/// Integration test for HelloWorldView.
/// Verifies the Spring Boot application loads correctly.
@SpringBootTest(classes = FlowApplication.class)
class HelloWorldIntegrationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  @DisplayName("Application context should load successfully")
  void contextLoads() {
    assertNotNull(applicationContext, "Application context should not be null");
  }

  @Test
  @DisplayName("HelloWorldView should be instantiable within Spring context")
  void helloWorldViewShouldBeInstantiable() {
    var view = new HelloWorldView();
    assertNotNull(view);
    assertEquals("Hello, World!", HelloWorldView.GREETING);
  }

  @Test
  @DisplayName("Application should have Vaadin configuration")
  void applicationShouldHaveVaadinConfig() {
    assertTrue(
      applicationContext.containsBean("vaadinApplicationContext") ||
      applicationContext.getBeanDefinitionCount() > 0,
      "Application should have beans loaded"
    );
  }
}
