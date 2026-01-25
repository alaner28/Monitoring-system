<template>
  <div id="demoDiv" class="map">
        <div class="map1"></div>
        <div class="map2"></div>
        <div class="map3"></div>
        <div class="chart" ref="map"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {mapData} from '../china'
export default {
    name:'map-1',
    mounted(){
      
      // console.log('mapdata',mapData);
      
        // 1. 实例化对象
        let myChart = echarts.init(this.$refs.map);
        echarts.registerMap('chinaMap',mapData)

        
        let option = {
          tooltip: {
            trigger: "item",
            formatter: function(params, /*ticket, callback*/) {
              if (params.seriesType == "effectScatter") {
                console.log(2);
                
                return "线路：" + params.data.name + "" + params.data.value[2];
              } else if (params.seriesType == "lines") {
                console.log(1);
                
                return (
                  params.data.fromName +
                  ">" +
                  params.data.toName +
                  "<br />" +
                  params.data.value
                );
              } else {
                // console.log(params);
                console.log(3);
                
                return params.name;
              }
            }
          },

            geo: {
              type:'map',

              map: 'chinaMap',
              label: {
                  emphasis: {
                  show: true,
                  color: "#fff"
                  }
              },
              //平移
              roam: true,
              //   放大我们的地图
              zoom: 1.1,
              itemStyle: {
                  normal: {
                  areaColor: "rgba(43, 196, 243, 0.42)",
                  borderColor: "rgba(43, 196, 243, 1)",
                  borderWidth: 1
                  },
                  emphasis: {
                  areaColor: "#2B91B7"
                  }
              }
            },
            series:[
              {
                // 设置涟漪效果的散点图
                type:'effectScatter',
                coordinateSystem:'geo',
                data:[
                  {
                    name:'西安市',
                    value:[
                        108.95,
                        34.26
                    ]
                  },
                  {
                    name:'北京市',
                    value:[
                        116.407431,
                        39.91405
                    ]
                  },
                  {
                    name:'上海市',
                    value:[
                      121.475366,
                      31.235929
                    ]
                  },
                  {
                    name:'广州市',
                    value:[
                      113.270279,
                      23.134804
                    ]
                  }
                ],
                // 设置涟漪效果
                rippleEffect:{
                  number:3,//波纹数量
                  scale:2.9,//波纹大小
                },
                itemStyle:{
                  color:'white'
                },
                label:{
                  show:true,
                  position:'bottom',
                  formatter:'{b}',
                  color:'white'
                }
              }
            ]
        };
        myChart.setOption(option);
        window.addEventListener("resize", function() {
            myChart.resize();
        });
    }
}
</script>

<style scoped>
#demoDiv {
    /* width: 49rem;
    height: 49rem; */
    width: 100%;
    height: 80%;
}

.map {
  position: relative;
  height: 100%;
}
.map .chart {
  position: absolute;
  top: -1.5rem;
  left: -1rem;
  z-index: 5;
  height: 47.5rem;
  width: 47.5rem;
}
.map .map1,
.map .map2,
.map .map3 {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -60%);
  /* width: 64%;
  height: 64%; */
  width: 28rem;
  height: 28rem;
  background: url('../../public/assets/map.png') no-repeat;
  background-size: 100% 100%;
  opacity: 0.4;
}
.map .map2 {
  width: 35rem;
  height: 35rem;
  /* width: 78%;
  height: 78%; */
  transform: translate(-10%, -60%);
  background-image: url('../../public/assets/lbx.png');
  opacity: 0.7;
  animation: rotate 15s linear infinite;
  z-index: 2;
}
.map .map3 {
  width: 30rem;
  height: 30.5rem;
  /* width: 73%;
  height: 66%; */
  /* transform: translate(15%, 0%); */
  background-image: url('../../public/assets/jt.png');
  animation: rotate1 10s linear infinite;
}
@keyframes rotate {
  from {
    transform: translate(-50%, -57%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -57%) rotate(360deg);
  }
}
@keyframes rotate1 {
  from {
    transform: translate(-50%, -59%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -59%) rotate(-360deg);
  }
}
@media screen and (max-width: 1024px) {
  html {
    font-size: 42px !important;
  }
}
@media screen and (min-width: 1920) {
  html {
    font-size: 80px !important;
  }
}
</style>