<template>
  <div class="no">
        <div class="no-hd">
            <ul>
                <li>{{ lastmonth }}</li>
                <li>{{ lastyear }}</li>
            </ul>
            </div>
            <div class="no-bd">
            <ul>
                <li>近30天危险警报</li>
                <li>近一年危险警报</li>
            </ul>
        </div>
        <map1></map1>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import map1 from './map1.vue';
import axios from 'axios';
import { useAlarmStore } from '@/stores/alarm';
import { storeToRefs } from 'pinia';

const alarmStore = useAlarmStore();
const { getMonthStatistics, getYearStatistics } = storeToRefs(alarmStore);

const lastmonth = computed(() => {
  let cnt = 0;
  for(const item in getMonthStatistics.value) {
    cnt += getMonthStatistics.value[item].eventCount;
  }
  return cnt;
});

const lastyear = computed(() => {
  let cnt = 0;
  for(const item in getYearStatistics.value) {
    cnt += getYearStatistics.value[item].eventCount;
  }
  return cnt;
});

// 处理事件总线的回调函数
let alarmHandler: (() => void) | null = null;

// 更新计数
const updateCount = (): void => {
  // 从Pinia获取数据，不需要API调用
  // 数据已经在store中，通过computed自动更新
};

onMounted(() => {
  updateCount();
  
  // 获取事件总线实例
  const bus = (window as any).$bus;
  if(bus) {
    alarmHandler = () => {
      console.log('body2接收到报警信息，准备更新');
      updateCount();
    };
    bus.$on('alarm', alarmHandler);
  }
});

onUnmounted(() => {
  // 移除事件监听
  const bus = (window as any).$bus;
  if(bus && alarmHandler) {
    bus.$off('alarm', alarmHandler);
  }
});
</script>

<style>
@font-face {
  font-family: electronicFont;
  src: url(../../public/assets/font/DS-DIGIT.TTF);
}
.no {
  background: rgba(101, 132, 226, 0.1);
  padding: .9375rem;
  /* width: 49rem; */
  width: 40%;
}
.no .no-hd {
  position: relative;
  border: 1px solid rgba(25, 186, 139, 0.17);
}
.no .no-hd::before {
  content: "";
  position: absolute;
  width: 1.875rem;
  height: .625rem;
  border-top: 2px solid #02a6b5;
  border-left: 2px solid #02a6b5;
  top: 0;
  left: 0;
}
.no .no-hd::after {
  content: "";
  position: absolute;
  width: 1.875rem;
  height: .625rem;
  border-bottom: 2px solid #02a6b5;
  border-right: 2px solid #02a6b5;
  right: 0;
  bottom: 0;
}
.no .no-hd ul {
  display: flex;
}
.no .no-hd ul li {
    list-style: none;
  position: relative;
  flex: 1;
  text-align: center;
  height: 5rem;
  line-height: 5rem;
  font-size: 2.625rem;
  color: #ffeb7b;
  padding: 1.25rem 0;
  /* font-family: electronicFont; */
  
   /* font-family: 'Roboto', sans-serif ; */
   /* 使用数字字体 */
   font-family: 'electronicFont'; 
    /* color: #B0E0E6; 设置文字颜色为浅蓝色 */
    font-size: 4rem; /*设置字体大小*/
    letter-spacing: 0.1rem; /*设置字母间距，模拟数字的显示效果*/
  /* font-weight: bold; */
}
.no .no-hd ul li:first-child::after {
  content: "";
  position: absolute;
  height: 50%;
  width: 5px;
  background: rgba(255, 255, 255, 0.2);
  right: 0;
  top: 25%;
}
.no .no-bd ul {
  display: flex;
}
.no .no-bd ul li {
    list-style: none;
  flex: 1;
  height: 2.5rem;
  line-height: 2.5rem;
  text-align: center;
  font-size: 1.125rem;
  color: rgba(255, 255, 255, 0.7);
  padding-top: .625rem;
  
}
</style>