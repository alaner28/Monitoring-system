<template>
  <div ref="sceneContainer" class="scene-container"></div>
</template>

<script>
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

export default {
  name: 'ModelViewer',
  data() {
    return {
      camera: null,
      renderer: null,
      scene: null,
      controls: null
    };
  },
  mounted() {
    this.initThree();
    window.addEventListener('resize', this.onWindowResize, false);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.onWindowResize, false);
    if (this.renderer) {
      this.renderer.dispose();
    }
  },
  methods: {
    initThree() {
      const container = this.$refs.sceneContainer;

      if (!container) {
        console.error('Container element not found!');
        return;
      }

      // Initialize scene
      const scene = new THREE.Scene();
      this.scene = scene;

      // Initialize camera
      const camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
      camera.position.set(0, 3.5, 5);
      this.camera = camera;

      // Initialize renderer
      const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
      renderer.setSize(container.clientWidth, container.clientHeight);
      renderer.setPixelRatio(window.devicePixelRatio);
      renderer.setClearColor(0x000000, 0);
      renderer.outputEncoding = THREE.sRGBEncoding;
      renderer.gammaFactor = 2.2;
      container.appendChild(renderer.domElement);
      this.renderer = renderer;

      // Add lighting to the scene
      const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
      directionalLight.position.set(10, 10, 10).normalize();
      scene.add(directionalLight);

      const ambientLight = new THREE.AmbientLight(0x404040, 25);
      scene.add(ambientLight);

      const pointLight = new THREE.PointLight(0xffffff, 1);
      pointLight.position.set(0, 3, 3);
      scene.add(pointLight);

      // Load model using GLTFLoader
      const loader = new GLTFLoader();
      loader.load('../../../../static/back/back/AI1.gltf', (gltf) => {
        const model = gltf.scene;
        model.traverse((child) => {
          if (child.isMesh) {
            child.material.needsUpdate = true;
            child.material.roughness = 0.5;
            child.material.metalness = 0.1;
            child.material.color.convertSRGBToLinear();
          }
        });
        model.position.set(0, 0, 0);
        model.scale.set(0.7, 0.7, 0.7);
        scene.add(model);
        renderer.render(scene, camera);
      }, undefined, (error) => {
        console.error('An error happened', error);
      });

      // Add orbit controls
      const controls = new OrbitControls(camera, renderer.domElement);
      controls.target.set(0, 1.6, 0);
      controls.update();
      this.controls = controls;

      // Animation loop
      const animate = () => {
        requestAnimationFrame(animate);
        controls.update();
        renderer.render(scene, camera);
      };
      animate();
    },
    onWindowResize() {
      const container = this.$refs.sceneContainer;
      const camera = this.camera;
      const renderer = this.renderer;
      if (container && camera && renderer) {
        camera.aspect = container.clientWidth / container.clientHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(container.clientWidth, container.clientHeight);
      }
    }
  }
};
</script>

<style>
.scene-container {
  width: 100%;
  height: 70%;
  position: absolute;
  z-index: 10;
  top: 15%;
  right: 0;
  overflow: hidden;
  background-color: transparent;
  
  /* background-color: white; */
}
</style>
