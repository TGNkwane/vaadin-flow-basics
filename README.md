# Vaadin Flow Basics — Tutorial & Architecture Guide

This tutorial packages the lessons from the project into a narrative that mirrors the Vaadin documentation deep dives. It explains how to run the application, how the layers fit together, trade-offs in architectural choices, and when Vaadin Flow is the right tool for the job.

> **Sources**: Summaries draw from the official Vaadin docs (Spring Boot, architecture layers/monoliths/microservices, project structure, presentation and application layers, server push) plus community discussions and Vaadin pricing FAQs (all links provided inline).

## 1. Tech Stack at a Glance

- **Java**: 25 (preview enabled via `--enable-preview`) configured in `pom.xml`.
- **Spring Boot**: 3.5.7 (from the starter parent).
- **Vaadin Flow**: 24.9.4 (`vaadin-spring-boot-starter`).
- **Persistence**: Spring Data JPA with H2 runtime database.
- **Utilities**: Jakarta Bean Validation, Lombok, Spring Boot Actuator & DevTools.
- **Frontend assets**: Managed by Vaadin (Flow components, generated frontend bundle).

Project layout follows the Vaadin convention with Java sources under `src/main/java`, Vaadin frontend assets in `frontend`, and theme assets (CSS) under `frontend/themes/<theme-name>/`.

### References
- [Vaadin Spring Boot integration (BOM guidance)](https://vaadin.com/docs/latest/flow/integrations/spring/spring-boot)
- [Spring Boot 3.5 documentation](https://docs.spring.io/spring-boot/index.html)

## 2. Why Vaadin Flow?

Vaadin Flow lets you build full-stack web apps entirely in Java while the framework handles the HTML/CSS/JS in the browser. That means:

- **Single-language stack** – Java developers can stay productive without switching to TypeScript/React for the view layer.
- **Strong UX out of the box** – Component styling is consistent and accessible, so you can focus on business logic first and polish visuals later.
- **Spring Boot integration** – Flow apps are Spring Boot apps; anything you can do in Spring Boot applies here too.

### References
- [Vaadin Flow Overview](https://vaadin.com/flow)
- [Vaadin & Spring Boot Deep Dive](https://vaadin.com/docs/latest/building-apps/deep-dives/spring-boot)

## 3. How the UI Boots: index.html and the Outlet

Vaadin Flow renders views to a single `<div id="outlet">` inside `frontend/index.html`. @frontend/index.html#1-24

- If you remove or customize `index.html`, Vaadin regenerates a default shell during build (see [Modify the Bootstrap Page](https://vaadin.com/docs/latest/flow/advanced/modifying-the-bootstrap-page)).
- The generated `index.ts` wires client routing and hands unmatched routes back to Flow.
- Flow uses WebSockets (via Atmosphere) for server push when available; it falls back to long polling otherwise ([Server Push docs](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push)).

### References
- [Modifying the Bootstrap Page](https://vaadin.com/docs/latest/flow/advanced/modifying-the-bootstrap-page)
- [Server Push Overview](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push)

## 4. Conceptual Layers in Vaadin Apps

Vaadin recommends thinking in **presentation** and **application** layers rather than “frontend/backend” ([Layers deep dive](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/layers)):

- **Presentation layer**: Vaadin views/components. They can run partly on the server (Flow) and partly in the browser (web components/Hilla).
- **Application layer**: Services, domain logic, repositories, integrations. Treated as an API that the presentation layer calls ([Application layer deep dive](https://vaadin.com/docs/latest/building-apps/deep-dives/application-layer)).

In the demo, `MainLayout` provides the UI shell and navigation. @src/main/java/com/leanring/vaadin/flow/shell/MainLayout.java#1-105

### References
- [Architecture Layers](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/layers)
- [Application Layer Deep Dive](https://vaadin.com/docs/latest/building-apps/deep-dives/application-layer)

## 5. Presentation Layer Patterns

Vaadin’s presentation layer can leverage:

- **Server push** with `@Push` on the AppShell when you need real-time updates ([Server Push guide](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push)).
- **Background threads** to update the UI safely (using UI access locks) and **CompletableFuture callbacks** for long-running tasks ([Threads](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push/threads) & [Futures](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push/futures)).
- **Two-way binding** via Binder as shown in `PersonEntityForm` handling nested properties like `address.street`. @src/main/java/com/leanring/vaadin/flow/forms/PersonEntityForm.java#20-181

### References
- [Server Push Overview](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push)
- [Using Threads Safely](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push/threads)
- [CompletableFuture Callbacks](https://vaadin.com/docs/latest/building-apps/deep-dives/presentation-layer/server-push/futures)

## 6. Application Layer Domain Logic

- Treat services and repositories as the public API for your UI. Changes behind the boundary do not break the presentation layer as long as the API stays stable.
- Flow integrates seamlessly with Spring’s DI and transaction management, so you can structure your services exactly as you would in a Spring Boot backend.

This project’s `services.catalog` package showcases the Boundary-Control-Entity style for the application layer.

### References
- [Application Layer Deep Dive](https://vaadin.com/docs/latest/building-apps/deep-dives/application-layer)

## 7. Project Structure Overview

```
vaadin-flow-basics/
├── pom.xml
├── frontend/
│   └── index.html (UI outlet)
├── src/main/java/com/leanring/vaadin/flow/
│   ├── FlowApplication.java (Spring Boot entry)
│   ├── AppShell.java (theme, shell config)
│   ├── shell/MainLayout.java (navigation)
│   ├── forms/... (binder demos)
│   ├── basic/... (component basics)
│   ├── grid/... (data grid demo)
│   └── services/catalog/... (application-layer example)
└── src/main/resources/
    └── application.properties
```

**Single-module** setup keeps everything in one Maven module, ideal for smaller teams and quick iteration ([Single Module guide](https://vaadin.com/docs/latest/building-apps/deep-dives/project-structure/single-module)).

### Where styling and frontend assets live

- `frontend/index.html` — application shell and router outlet.
- `frontend/generated/` — Vaadin-generated JS bundles (don’t edit manually).
- `frontend/themes/guild/` — theme assets, including `styles.css` and component-specific styles.
- Additional global CSS can be placed in the theme folder and referenced via `@Theme("guild")` configuration (`AppShell`).

### When to split into multi-modules?

If your app grows, consider segregating views, services, entities, utilities into separate Maven modules to mirror architectural boundaries ([Multi-module guide](https://vaadin.com/docs/latest/building-apps/deep-dives/project-structure/multi-module)). Modules compile independently, helping enforce boundaries and enabling selective reuse.

### References
- [Single-Module Project Structure](https://vaadin.com/docs/latest/building-apps/deep-dives/project-structure/single-module)
- [Multi-Module Project Structure](https://vaadin.com/docs/latest/building-apps/deep-dives/project-structure/multi-module)

## 8. Deployment Architectures: Monolith vs Microservices

| Scenario | Monolith ([doc](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/monoliths)) | Microservices ([doc](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/microservices)) |
| --- | --- | --- |
| **Definition** | Single executable, function-call boundaries, optional modular monolith | Collection of independently deployable services communicating over APIs |
| **Pros** | Simplicity, transactional integrity, less runtime overhead | Targeted scalability, technology flexibility, resilience, independent deployments |
| **Cons** | Scaling whole app, team coordination, complex refactoring | Operational complexity, network overhead, testing, cross-service debugging |
| **When to choose** | Greenfield projects, small teams, strong transactional workflows, rapid iteration | Large organizations, varying scalability needs, polyglot stacks, independent team autonomy |

Vaadin fits both models: run as a single Spring Boot app or serve as a dedicated/aggregating UI in a microservice landscape using Spring Cloud.

### References
- [Monolithic Architectures](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/monoliths)
- [Microservices Architectures](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/microservices)

## 9. Using Third-Party JavaScript Libraries

Vaadin’s build tooling is npm-based, so you can bring in any package from the npm ecosystem (for example, `three`, `chart.js`, or custom design-system bundles) and pair it with Flow views.

**Typical integration workflow**

1. **Declare the dependency**
   - Annotate a configuration class with `@NpmPackage(value = "three", version = "0.170.0")`, or
   - Run `npm install three` / `pnpm add three` from the project root so it lands in `package.json`.
2. **Author a JavaScript module** in `frontend/` that imports the package and exposes the features you need (e.g., registers a custom element or exports helper functions).
3. **Link the module to your Flow view** using `@JsModule("./three-demo.js")` (path relative to `frontend/`).
4. **Bridge between Java & JS** via `Element.callJsFunction(...)`, events, or a small web component API. Remember to perform UI updates inside `UI.access()` when called from background threads.

Because Vaadin already handles bundling, the Vaadin Maven plugin will install and include the npm package automatically during `vaadin:prepare-frontend` or the dev-server startup.

**Example: Rendering a Three.js scene from a Flow view**

```java
// src/main/java/.../ThreeDemoView.java
@Route(value = "three-demo", layout = MainLayout.class)
@NpmPackage(value = "three", version = "0.170.0")
@JsModule("./three-demo.js")
public class ThreeDemoView extends Div {

  public ThreeDemoView() {
    setId("three-container");
    setHeight("400px");
    setWidth("100%");

    // Delegate rendering to the JS helper once the component is attached
    addAttachListener(event -> event.getUI().access(() ->
      getElement().callJsFunction("renderThree", getId().orElse("three-container"))
    ));
  }
}
```

```javascript
// frontend/three-demo.js
import * as THREE from 'three';

window.renderThree = (elementId) => {
  const container = document.getElementById(elementId);
  if (!container) {
    console.warn('Three.js container not found');
    return;
  }

  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(75, container.clientWidth / 400, 0.1, 1000);
  const renderer = new THREE.WebGLRenderer();
  renderer.setSize(container.clientWidth, 400);
  container.innerHTML = '';
  container.appendChild(renderer.domElement);

  const geometry = new THREE.BoxGeometry();
  const material = new THREE.MeshNormalMaterial();
  const cube = new THREE.Mesh(geometry, material);
  scene.add(cube);

  camera.position.z = 3;

  const animate = () => {
    cube.rotation.x += 0.01;
    cube.rotation.y += 0.01;
    renderer.render(scene, camera);
    requestAnimationFrame(animate);
  };

  animate();
};
```

### References
- [Using npm packages for static assets](https://vaadin.com/docs/latest/styling/advanced/npm-packages)
- [Creating & using custom web components](https://vaadin.com/docs/latest/flow/create-ui/web-components)
- [npm/pnpm configuration in Vaadin projects](https://vaadin.com/docs/latest/configuration/npm-pnpm)

## 10. Running the Demo

Once the architectural concepts are clear, run the project to see the pieces in action:

1. Ensure you have **Java 25 (preview)** and **Maven 3.9+** installed to match the `pom.xml` configuration.
2. From the project root, install frontend dependencies and build the bundle once:
   ```bash
   mvn -Pproduction vaadin:prepare-frontend
   ```
   For iterative development you can skip the production profile and let Vaadin’s dev-server handle assets.
3. Start the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
4. Open `http://localhost:8080` to explore the demo views (forms, grids, layouts, catalog example).

The entry point is `FlowApplication` with the standard `SpringApplication.run(...)` bootstrap. @src/main/java/com/leanring/vaadin/flow/FlowApplication.java#1-13

Vaadin’s `AppShell` configures the global theme (`guild`) and is the right place to enable server push, add meta tags, or inject global resources. @src/main/java/com/leanring/vaadin/flow/AppShell.java#1-10

### References
- [Building Vaadin Apps with Spring Boot](https://vaadin.com/docs/latest/building-apps/deep-dives/spring-boot)
- [Vaadin Maven Plugin](https://vaadin.com/docs/latest/flow/tooling/maven-plugin)

## 11. Pros & Cons of Vaadin Flow vs Frontend Framework Stacks

**Pros**

1. Java-first development, leveraging existing backend expertise.
2. Rich component set with consistent design (Lumo) and accessibility baked in.
3. Strong enterprise integration: security, DI, transactions, Spring ecosystem.
4. Productivity for internal tools and business apps where UI custom design is secondary.

**Cons / Reasons teams still choose React/Angular**

1. **Community size** – React/Angular ecosystems are massive with abundant tutorials & third-party components. Vaadin’s community is smaller and more specialized.
2. **Custom design freedom** – Frontend frameworks offer granular control and alignment with modern design systems; Vaadin abstracts much of the HTML/CSS which can be limiting for pixel-perfect consumer UIs.
3. **Hiring** – Java developers can learn Vaadin quickly, but front-end specialists may prefer TypeScript-based stacks.
4. **SSR vs CSR preferences** – Some teams want client-side rendering for offline support or heavy client-side logic.

Use Vaadin when your priority is delivering business workflows quickly with robust backend integration. Opt for a traditional frontend + REST/GraphQL backend when you need a bespoke UX, rely heavily on frontend state management, or want to tap into the wider JS ecosystem.

### References
- [Java Web Framework Comparison](https://vaadin.com/comparison)

## 12. Licensing & Limitations of the Free Plan

- Vaadin Flow, Hilla, and core components are Apache 2.0 – free for commercial use ([pricing FAQ](https://vaadin.com/pricing)).
- Premium features (Charts, Dashboard, Maps, CRUD, TestBench, Collaboration Engine, Acceleration Kits, long-term support) require paid tiers (Plus/Pro/Team/Enterprise).
- The free plan covers most internal app needs; budget for licenses when you need advanced data visualization, UI testing automation, or enterprise support.

### References
- [Vaadin Pricing & FAQ](https://vaadin.com/pricing)
- [Vaadin Pricing FAQ](https://vaadin.com/pricing/faq)

## 13. Community & Ecosystem Considerations

- Vaadin has been around since 2000s, but adoption is niche compared to mainstream JS frameworks.
- Community assistance exists (Forum, Discord, StackOverflow) yet less saturation of tutorials and open-source add-ons.
- Onboarding new hires is easier for Java developers than for frontend-first engineers.
- Vaadin provides long-term support releases and modernization toolkits for legacy Vaadin 7/8 or Swing apps (Enterprise tier).

### References
- [Vaadin Community Hub](https://vaadin.com/community)
- [Vaadin Modernization Toolkit Overview](https://vaadin.com/modernization)
- [Vaadin Pricing FAQ](https://vaadin.com/pricing/faq)

## 14. Pros & Cons Summary

| Aspect | Benefits | Trade-offs |
| --- | --- | --- |
| Developer Experience | Single Java stack, strong tooling; binder simplifies forms | Less control over raw HTML/JS; must learn Vaadin component APIs |
| Productivity | Quickly prototype internal UIs; built-in theme | Custom styling may need Vaadin theming knowledge |
| Architecture | Aligns with Spring Boot; supports modular monolith or microservices | Requires server resources for UI; not purely client-side |
| Licensing | Free core, commercial-friendly | Advanced features locked behind subscriptions |
| Community | Official docs thorough; enterprise support available | Smaller community than React/Angular |

### References
- [Vaadin & Spring Boot Deep Dive](https://vaadin.com/docs/latest/building-apps/deep-dives/spring-boot)
- [Monolithic Architectures](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/monoliths)
- [Microservices Architectures](https://vaadin.com/docs/latest/building-apps/deep-dives/architecture/microservices)
- [Vaadin Pricing FAQ](https://vaadin.com/pricing/faq)
