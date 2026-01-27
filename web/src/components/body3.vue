<template>
    <div class="body3">
      <monitor></monitor>
      <people></people>
      <weather></weather>
    </div>
  </template>
  
  <script setup lang="ts">
  import monitor from './monitor.vue';
  import people from './people.vue';
  import weather from './weather.vue';
</script>
  
  <style scoped>
      .body3 {
          /* width: 25rem; */
          width: 27%;
          /* height: 18.75rem; */
          /* background-color: pink; */
          /* display: flex;
          flex-direction: column;
          align-items: center; */
          /* margin-left: 30px; */
          padding-left: .625rem;
          margin-right: 2rem;
      }
  
  </style>