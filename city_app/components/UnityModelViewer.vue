<template>
  <div ref="unityContainer" class="unity-container"></div>
</template>

<script>
export default {
  name: 'UnityModelViewer',
  mounted() {
    this.loadUnity();
  },
  methods: {
    loadUnity() {
      const buildUrl = '../assets/UnityWebGL/Build'; // 指定Unity构建文件的路径（使用相对路径或绝对路径）
      const loaderUrl = `${buildUrl}/boy.loader.js`;
      
      const config = {
        dataUrl: `${buildUrl}/boy.data`,
        frameworkUrl: `${buildUrl}/boy.framework.js`,
        codeUrl: `${buildUrl}/boy.wasm`,
        companyName: 'Your Company',
        productName: 'Your Product',
        productVersion: '1.0',
      };

      const script = document.createElement('script');
      script.src = loaderUrl;
      script.onload = () => {
        if (typeof createUnityInstance === 'undefined') {
          console.error('createUnityInstance is not defined. Check if the loader script is loaded correctly.');
          return;
        }
        createUnityInstance(this.$refs.unityContainer, config, (progress) => {
          console.log(`Unity progress: ${progress * 100}%`);
        }).then((unityInstance) => {
          console.log('Unity loaded successfully');
        }).catch((message) => {
          console.error(message);
        });
      };
      script.onerror = () => {
        console.error('Failed to load the Unity loader script.');
      };
      document.body.appendChild(script);
    }
  }
};
</script>

<style>
.unity-container {
  width: 100%;
  height: 85%;
  position: relative;
}
</style>
