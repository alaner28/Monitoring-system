<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

// 如果不需要showdetail，可以删除它；或者如果需要但暂时未使用，可以加上注释
const showdetail = ref<boolean>(false)

onMounted(() => {
  // 组件挂载后执行的逻辑
})
</script>

<style>
/* @import url('../static/font/font.css'); */
* {
  margin: 0;
  padding: 0;
}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
  
  /* background-repeat: no-repeat; */
  /* background-size: 100% 120%; */
  /* height: 100%;
  width: 100%; */
}


</style>
