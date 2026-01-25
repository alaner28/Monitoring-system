<template>
  <div class="body1">
    <barchart1></barchart1>
    <piechart1></piechart1>
    <alarmlist></alarmlist>
  </div>
</template>

<script>
import barchart1 from './barchart1.vue';
import piechart1 from './piechart1.vue';
import alarmlist from './alarmlist.vue';
    export default {
        name:'body-1',
        components:{
            barchart1,
            piechart1,
            alarmlist
        }
    }
</script>

<style>
    .body1 {
        /* width: 25rem; */
        width: 27%;
        /* height: 18.75rem; */
        /* background-color: pink; */
        /* display: flex;
        flex-direction: column;
        align-items: center; */
        /* margin-left: 30px; */
        padding-left: 1.875rem;
        padding-bottom: 1.5rem;
    }

</style>