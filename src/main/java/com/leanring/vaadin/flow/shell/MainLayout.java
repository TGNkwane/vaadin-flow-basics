package com.leanring.vaadin.flow.shell;

import com.leanring.vaadin.flow.basic.BasicComponentsView;
import com.leanring.vaadin.flow.js.ThreeDemoView;
import com.leanring.vaadin.flow.services.catalog.boundary.ProductView;
import com.leanring.vaadin.flow.dialog.DialogView;
import com.leanring.vaadin.flow.forms.FormView;
import com.leanring.vaadin.flow.grid.GridView;
import com.leanring.vaadin.flow.layouts.LayoutsView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/// Main application layout providing navigation shell.
/// Uses AppLayout with drawer for navigation between demo views.
public class MainLayout extends AppLayout {

  /// Constructor initializes the app layout with navigation
  public MainLayout() {
    createHeader();
    createDrawer();
  }

  /// Creates the application header with logo and toggle
  private void createHeader() {
    var toggle = new DrawerToggle();
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    var logo = new H1("🚀 Vaadin Guild Playground");
    logo.addClassNames(
      LumoUtility.FontSize.LARGE,
      LumoUtility.Margin.NONE,
      "app-logo"
    );

    var header = new HorizontalLayout(toggle, logo);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setWidthFull();
    header.addClassNames(
      LumoUtility.Padding.Vertical.NONE,
      LumoUtility.Padding.Horizontal.MEDIUM,
      "app-header"
    );

    addToNavbar(header);
  }

  /// Creates the navigation drawer with links to all views
  private void createDrawer() {
    var menu = new VerticalLayout(
      createNavLink("🏠 Home", HomeView.class),
      createSectionHeader("Basic Demos"),
      createNavLink("👀 Basic Components", BasicComponentsView.class),
      createNavLink("💬 Dialog Demo", DialogView.class),
      createNavLink("📝 Layouts Demo", LayoutsView.class),
      createNavLink("📋 Forms Demo", FormView.class),
      createNavLink("📋 Grid Demo", GridView.class),
      createSectionHeader("Intermediate"),
      createNavLink("🛒 Product Catalog (BCE)", ProductView.class),
      createNavLink("🧊 Three.js Demo", ThreeDemoView.class)
    );

    menu.setSizeFull();
    menu.setPadding(false);
    menu.setSpacing(false);

    addToDrawer(menu);
  }

  /// Creates a navigation link
  /// @param text Link text with emoji
  /// @param view View class to navigate to
  /// @return Configured RouterLink
  private RouterLink createNavLink(String text, Class<?> view) {
    var link = new RouterLink(text, (Class) view);
    link.addClassNames(
      LumoUtility.Display.BLOCK,
      LumoUtility.Padding.SMALL,
      LumoUtility.TextColor.BODY,
      "nav-link"
    );

    return link;
  }

  /// Creates a section header in the navigation
  /// @param text Section header text
  /// @return Configured Span as section header
  private Span createSectionHeader(String text) {
    var header = new Span(text);
    header.addClassNames(
      LumoUtility.FontSize.SMALL,
      LumoUtility.FontWeight.SEMIBOLD,
      LumoUtility.TextColor.SECONDARY,
      LumoUtility.Padding.Top.MEDIUM,
      LumoUtility.Padding.Bottom.XSMALL,
      LumoUtility.Padding.Horizontal.MEDIUM
    );
    return header;
  }
}