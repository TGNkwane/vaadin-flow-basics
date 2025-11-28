/**
 * ### three-demo.js
 *
 * Browser-side helper registered globally so Flow views can trigger Three.js rendering
 * with `getElement().executeJs("window.renderThree($0, $1)", containerId, jsonPayload)`.
 *
 * Data flow reference:
 * 1. Java view injects a Spring service, serialises DTOs (e.g., via Jackson), and calls
 *    this function with both the target element id and the JSON payload.
 * 2. This module parses the data (if provided) and builds the Three.js scene.
 * 3. Any charts/maps library can follow the same pattern—replace the Three.js code and
 *    expose the function under `window` so Flow can call it.
 *
 * Ensure the module is imported via `@JsModule("./three-demo.js")` and the dependency
 * is declared with `@NpmPackage`. Re-run `mvn vaadin:prepare-frontend` after changes so
 * the bundle includes the new assets.
 */
import * as THREE from 'three';

window.renderThree = (elementId) => {
  const container = document.getElementById(elementId);
  if (!container) {
    console.warn('Three.js container not found for id', elementId);
    return;
  }

  const width = container.clientWidth || 600;
  const height = 400;

  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setSize(width, height);

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
