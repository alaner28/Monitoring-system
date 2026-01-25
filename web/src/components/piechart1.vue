<template>
  <div class="panel">
    <div class="chart" ref="piechart" id="demoDiv"></div>
    <div class="panel-footer"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'piechart-1',
  data() {
    return {
      chartData: [
          {
              eventType: "禁区",
              eventCount : 29
          },
          {
              eventType: "挥手",
              eventCount : 44
          },
          {
              eventType: "摔倒",
              eventCount : 23
          },
          {
              eventType: "明火",
              eventCount : 11
          },
          {
              eventType: "吸烟",
              eventCount : 13
          },
          {
              eventType: "打拳",
              eventCount : 14
          }
      ],
      intervalId: null, // 用于保存定时器ID
    };
  },
  mounted() {
    // 初始化图表
    this.initChart();

    //测试使用，一会删了
    // this.updateChart(this.chartData);

    this.fetchChartData();

    this.$bus.$on('alarm',()=>{
      console.log('piechart1接收到报警信息，准备更新');
      
      this.fetchChartData();
    })

    // 监听窗口大小调整事件
    window.addEventListener("resize", () => {
      this.mychart.resize();
    });
  },
  beforeDestroy(){
      this.$bus.$off('alarm')
  },
  methods: {
    // 初始化图表
    initChart() {
      this.mychart = echarts.init(this.$refs.piechart);

      // 设置初始的图表选项 (可以是空数据，稍后通过 axios 填充)
      this.mychart.setOption({
        title: {
          text: '近一周危险行为',
          textStyle: {
            color: 'white',
            fontSize: '1.5rem',
          },
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          top: '5%',
          right: '5%', // 图例放在右侧
          orient: 'vertical', // 竖直排列
          textStyle: {
            color: 'white',
            fontSize: '1.3rem', // 调整图例字体大小
          },
        },
        series: [
          {
            name: '危险警报',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['35%', '55%'], // 将饼图向左移动，设置靠左
            avoidLabelOverlap: false,
            padAngle: 6,
            itemStyle: {
              borderRadius: 10,
            },
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '2rem',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: false,
            },
            data: [], // 初始时为空，后续会通过 axios 数据填充
          },
        ],
      });
    },



    // 获取图表数据
    async fetchChartData() {

      var config = {
        method: 'get',
        url: '/api/data-visualization/v1/statistics/weekly',
        headers: { 
            'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
        }
      };

      // 使用箭头函数以保持 this 的上下文
      this.$axios(config)
        .then(response => {
          const res = response.data;
          if (res.code == '200') {
            console.log('获取本周数据成功', res.data);
            this.chartData = res.data;

            // 更新图表
            this.updateChart(this.chartData);
          }
        })
        .catch(error => {
          console.log(error);
        });
    
    },

    // 更新图表
    updateChart(data) {
      const chartData = data.map(item => ({
        value: item.eventCount, // 设定 y 轴的值
        name: item.eventType // 图例和 x 轴的名称
      }));

      this.mychart.setOption({
            series: [
              {
                data: chartData, // 将转换后的数据更新到图表中
              },
            ],
            legend: {
              data: data.map(item => item.eventType), // 更新图例为 eventType
            },
      });
    }

  },
};
</script>

<style scoped>
#demoDiv {
  width: 100%;
  height: 100%;
}
</style>
