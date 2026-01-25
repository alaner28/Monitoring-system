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

<script>
export default {
  name: 'weather-1',
  data() {
      return {
          // 温度
          temperature: 26,
          //实时天气情况
          weather:'晴',
          // 预测三天的数据
          day: [
              {
                  date: '2024-09-24',
                  dayweather: '阴',
                  daytemp: '25',
                  nighttemp: '14'
              },
              {
                  date: '2024-09-25',
                  dayweather: '阴',
                  daytemp: '25',
                  nighttemp: '13'
              },
              {
                  date: '2024-09-26',
                  dayweather: '晴',
                  daytemp: '24',
                  nighttemp: '15'
              }
          ]
      };
  },
  methods: {
      getimg(state) {
          // console.log(state);
          if (state === '多云') {
              return require('../../public/assets/weather/cloud.png');
          } else if (state === '小雨') {
              return require('../../public/assets/weather/smallrain.png');
          }else if(state === '中雨'){
            return require('../../public/assets/weather/midrain.png')
          }else if(state === '大雨'){
            return require('../../public/assets/weather/bigrain.png')
          }else if(state === '阴'){
            return require('../../public/assets/weather/overcastsky.png')
          }else if(state === '暴雨'){
            return require('../../public/assets/weather/rainstorm.png')
          }else if(state === '雷阵雨'){
            return require('../../public/assets/weather/thundershower.png')
          }else{
            //晴
            return require('../../public/assets/weather/sun.png')
          }
      },
      // 异步获取天气数据
      fetchWeatherData() {
        
        let config = {
        method: 'get',
        url: '/api/data-visualization/v1/weather/future?adcode=120000',
        headers: { 
            'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
        }
      };

      // 使用箭头函数以保持 this 的上下文
      this.$axios(config)
        .then(response => {
          const res = response.data;
          if (res.code == '200') {
              console.log('获取未来三天数据成功',res.data);
              this.day = res.data
          }
        })
        .catch(error => {
          console.log(error);
        });

        config = {
        method: 'get',
        url: '/api/data-visualization/v1/weather/real-time?adcode=120000',
        headers: { 
            'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
        }
      };

      // 使用箭头函数以保持 this 的上下文
      this.$axios(config)
        .then(response => {
          const res = response.data;
          if(res.code == '200'){
              // console.log('获取实时温度成功', res.data);
              this.temperature = res.data.temperature
              this.weather = res.data.weather
          }
        })
        .catch(error => {
          console.log(error);
        });

      },
      // 每秒获取一次天气数据
      startFetching() {
          setInterval(() => {
              this.fetchWeatherData();
          }, 60000); // 每60000ms（1分钟）请求一次数据
      }
  },
  mounted() {
      this.fetchWeatherData(); // 初始数据请求
      this.startFetching(); // 开始定时获取数据
  }
};
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
