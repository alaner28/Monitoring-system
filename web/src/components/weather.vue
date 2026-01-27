<template>
  <div class="panel">
      <div id="demoDiv">
          <div class="temperature">
              <div class="inhouse">
                  <img src="../../public/assets/weather/inweather.png" alt="室内温度">
                  <span>室内温度</span>
                  <h3>23 °C</h3>
              </div>
              <div class="outhouse">
                  <img src="../../public/assets/weather/outerweather.png" alt="室外温度">
                  <span>室外温度</span>
                  <h3>{{ temperature }} °C</h3>
              </div>
          </div>

          <div class="weather">
              <div class="day" v-for="(item, index) in day" :key="index">
                  <h4>{{ item.date.substring(5) }}</h4>
                  <img :src="getimg(item.dayweather)">
                  <span>{{ item.nighttemp }} ~ {{ item.daytemp }} °C</span>
              </div>
          </div>
      </div>

      <div class="panel-footer"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import axios from 'axios';
import { useAlarmStore } from '@/stores/alarm';
import { storeToRefs } from 'pinia';

// 定义天气数据接口
interface WeatherDay {
  date: string;
  dayweather: string;
  daytemp: string;
  nighttemp: string;
}

interface RealWeatherData {
  temperature: number;
  weather: string;
}

const alarmStore = useAlarmStore();
const { getFutureWeather, getRealTimeWeather } = storeToRefs(alarmStore);

// 温度
const temperature = computed(() => getRealTimeWeather.value.temperature);
// 实时天气情况
const weather = computed(() => getRealTimeWeather.value.weather);
// 预测三天的数据
const day = computed(() => getFutureWeather.value);

const getimg = (state: string): string => {
    // console.log(state);
    if (state === '多云') {
        return require('../../public/assets/weather/cloud.png');
    } else if (state === '小雨') {
        return require('../../public/assets/weather/smallrain.png');
    } else if(state === '中雨'){
      return require('../../public/assets/weather/midrain.png')
    } else if(state === '大雨'){
      return require('../../public/assets/weather/bigrain.png')
    } else if(state === '阴'){
      return require('../../public/assets/weather/overcastsky.png')
    } else if(state === '暴雨'){
      return require('../../public/assets/weather/rainstorm.png')
    } else if(state === '雷阵雨'){
      return require('../../public/assets/weather/thundershower.png')
    } else {
      //晴
      return require('../../public/assets/weather/sun.png')
    }
};

// 从Pinia获取天气数据
const fetchWeatherData = (): void => {
  // 直接从Pinia获取数据，不需要API调用
  // 数据已经通过其他地方存储在store中
};

let intervalId: number | null = null;

// 每分钟获取一次天气数据
const startFetching = (): void => {
    intervalId = window.setInterval(() => {
        fetchWeatherData();
    }, 60000); // 每60000ms（1分钟）请求一次数据
};

onMounted(() => {
    fetchWeatherData(); // 从Pinia获取数据
    startFetching(); // 开始定时获取数据
    
    // 监听天气数据变化
    watch([getFutureWeather, getRealTimeWeather], ([newFuture, newReal]) => {
        // 数据变化时自动更新，由于使用了computed，视图会自动响应
    }, { deep: true });
});

// 清理定时器
onUnmounted(() => {
    if(intervalId) {
        clearInterval(intervalId);
    }
});
</script>

<style scoped>
#demoDiv {
  width: 100%;
  height: 100%;
  color: white;
}

h2 {
  color: white;
}

.temperature span {
  font-size: 2rem;
  color: white;
  margin-left: 0.5rem;
}

.temperature {
  width: 30rem;
  height: 8rem;
  display: flex;
}

.temperature div {
  width: 50%;
  height: 80%;
  padding-top: 1rem;
}

.inhouse h3 {
  color: #4B9DD1;
}

.outhouse h3 {
  color: #A04157;
}

.temperature img {
  width: 2rem;
  height: 2rem;
}

h3 {
  margin-top: 0.8rem;
  font-size: 2rem;
}

.weather {
  margin-top: 0.8rem;
  width: 100%;
  display: flex;
  color: white;
}

.day {
  flex: 1;
  border-right: 0.25rem solid skyblue;
  font-size: 1.6rem;
}

.day:nth-child(3) {
  border-right: none;
}

.day img {
  display: block;
  width: 2.5rem;
  height: 2.5rem;
  margin: 0 auto;
  margin-bottom: 0.3rem;
}

h4 {
  font-size: 1.7rem;
  margin-bottom: 0.5rem;
}
</style>
