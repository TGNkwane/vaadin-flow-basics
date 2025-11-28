package com.leanring.vaadin.flow.js;

import com.leanring.vaadin.flow.shell.MainLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/// ### ThreeDemoView
///
/// Demonstrates how to embed an npm-based Three.js visualisation inside a Flow route.
/// The server-side view can still access Spring-managed services — fetch your data
/// (e.g., statistics or chart points) inside the constructor, then hand the response
/// to the browser through `executeJs` or `callJsFunction`.
///
/// ```java
/// public ThreeDemoView(AnalyticsService service, ObjectMapper mapper) {
///   var payload = mapper.writeValueAsString(service.fetchLatestMetrics());
///   getElement().executeJs("window.renderThree($0, $1)", CONTAINER_ID, payload);
/// }
/// ```
///
/// The JavaScript module declared with `@JsModule` handles rendering and can be
/// swapped for any other library (Chart.js, Mapbox, etc.).
@Route(value = "three-demo", layout = MainLayout.class)
@PageTitle("Three.js Demo")
@NpmPackage(value = "three", version = "0.170.0")
@JsModule("./three-demo.js")
public class ThreeDemoView extends Div {

  private static final String CONTAINER_ID = "three-container";

  public ThreeDemoView() {
    setId(CONTAINER_ID);
    setHeight("400px");
    setWidth("100%");

    addAttachListener(event -> {
      var ui = event.getUI();
      /// UI.access guarantees the Vaadin session lock while invoking the
      /// browser helper. Replace `renderThree` with any exported function that
      /// accepts the server data you want to plot or animate.
      ui.access(() -> getElement().executeJs(
        "window.renderThree && window.renderThree($0)",
        CONTAINER_ID
      ));
    });
  }
}
