<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
  // import visual from './pages/visual.vue';
  export default {
    name: 'App',
    data() {
      return {
        showdetail:false
      }
    },
    mounted(){
    }
  }
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
