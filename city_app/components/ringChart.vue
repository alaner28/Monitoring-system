<template>
	<view class="charts-box" ref="chart">
	    <qiun-data-charts
	    		class="mychart"
	      type="ring"
	      :opts="opts"
	      :chartData="chartData"
	      :canvas2d="true"
	      canvasId="WuTrqYCMnPwHxXPfqJkQyBBqgUGzcZuk"
	    />
	</view>
		
      
</template>
  
  <script>
  export default {
    data() {
      return {
        chartData:{},
        result:[],
        opts: {
          rotate: false,
          rotateLock: false,
          color: ["#1890FF","#91CB74","#FAC858","#EE6666","#73C0DE","#3CA272","#FC8452","#9A60B4","#ea7ccc"],
          padding: [5,5,5,5],
          dataLabel: true,
          enableScroll: false,
          legend: {
            show: true,
            position: "right",
            lineHeight: 25
          },
          title: {
            name: "",
            fontSize: 15,
            color: "#666666"
          },
          subtitle: {
            name: "",
            fontSize: 25,
            color: "#7cb5ec"
          },
          extra: {
            ring: {
              ringWidth: 30,
              activeOpacity: 0.5,
              activeRadius: 10,
              offsetAngle: 0,
              labelWidth: 15,
              border: true,
              borderWidth: 2,
              borderColor: "white"
            }
          }
        }
      };
    },
    mounted() {

      this.$nextTick(() => {
		  setTimeout(() => {
			this.getServerData();
		  }, 800); // 延迟 800ms 确保元素渲染完毕
		});
		// this.getServerData();
		// this.$nextTick(() => {
		//         this.getServerData();
		//     });
    },
    onShow(){

      this.getServerData();
	  // this.$nextTick(() => {
	  //         this.getServerData();
	  //     });
    },
    methods: {
      async getData() {
        await uni.$http.get("/api/v1/alarm/realtime")
        .then(res => {
          if(res.data.code !== "00000") {
            uni.showToast({
              title: "获取图表数据失败",
              duration: 1500,
              icon: "none",
            })
          }
          else {
			  console.log('环形图',res.data.data)
            const temp = [
              {'name':res.data.data.alarmCaseTypeTotalList[0].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[0].total},
              {'name':res.data.data.alarmCaseTypeTotalList[1].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[1].total},
              {'name':res.data.data.alarmCaseTypeTotalList[2].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[2].total},
              {'name':res.data.data.alarmCaseTypeTotalList[3].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[3].total},
              {'name':res.data.data.alarmCaseTypeTotalList[4].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[4].total},
              {'name':res.data.data.alarmCaseTypeTotalList[5].caseTypeName,'value':res.data.data.alarmCaseTypeTotalList[5].total},
            ]
            this.result = temp;
          }
        })
      },
      async getServerData() {
        let res = {
          series:[
            {
              data:[],
            }
          ]
        }
        await this.getData();
        res.series[0].data = this.result;
        // console.log('唤醒',res);
        setTimeout(() => {
          this.chartData = JSON.parse(JSON.stringify(res));
		  console.log('chart',this.chartData)
        }, 500);
		
      },
    }
  };
  </script>
  
 <style scoped>
   .charts-box {
     width: 100%;
     height: 100%; /* 确保有足够的高度来渲染图表 */
   }

 </style>

    