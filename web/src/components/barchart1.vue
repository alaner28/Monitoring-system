<template>
    <div class="panel">
        <div class="chart" ref="barchart" id="demoDiv"></div>
        <div class="panel-footer"></div>
    </div>
</template>

<script>
    import * as echarts from 'echarts';

    export default {
        name: 'bar-chart1',
        data() {
            return {
                mychart: null, // 保存echarts实例
                chartData:[
                    {
                        eventType: "禁区",
                        eventCount : 5
                    },
                    {
                        eventType: "挥手",
                        eventCount : 14
                    },
                    {
                        eventType: "摔倒",
                        eventCount : 8
                    },
                    {
                        eventType: "明火",
                        eventCount : 3
                    },
                    {
                        eventType: "吸烟",
                        eventCount : 5
                    },
                    {
                        eventType: "打拳",
                        eventCount : 11
                    }
                ]
            };
        },
        mounted() {
            this.initChart(); // 初始化图表
            
            //测试，截图使用，一会删了
            // this.updateChart(this.chartData);

            this.fetchData(); // 首次获取数据
            this.$bus.$on('alarm',()=>{
                console.log('barchart1接收到报警信息，准备更新');
                this.fetchData(); // 获取数据
            })
        },
        beforeDestroy(){
            console.log('即将销毁');
            this.$bus.$off('alarm')
        },
        methods: {
            // 初始化图表
            initChart() {
                this.mychart = echarts.init(this.$refs.barchart);
                this.mychart.setOption({
                    title: {
                        text: '今日危险事件统计',
                        textStyle: {
                            color: 'white',
                            fontSize: '1.5rem',
                        },
                    },
                    tooltip: {
                        trigger: 'axis',
                        // trigger:'item',
                        axisPointer: {
                            type: 'cross',
                        },
                    },
                    xAxis: {
                        type: 'category',
                        data: [],
                        axisLabel: {
                            color: 'white',
                            fontSize: '1rem',
                            interval: 0,
                        },
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            color: 'white',
                            fontSize: '1rem',
                        },
                    },
                    series: [
                        {
                            name: ['数量'],
                            type: 'bar',
                            data: [],
                            colorBy: 'data',
                            showBackground: true,
                            itemStyle: {
                                borderRadius: 8,
                            },
                            barWidth: '25rem',
                        },
                    ],
                    
                });

                // 添加窗口大小改变的监听事件
                window.addEventListener('resize', () => {
                    this.mychart.resize();
                });
            },

            // 异步获取数据
            async fetchData() {

                var config = {
                    method: 'get',
                    url: '/api/data-visualization/v1/statistics/day',
                    headers: { 
                        'User-Agent': 'Apifox/1.0.0 (https://apifox.com)'
                    }
                };

                // 使用箭头函数以保持 this 的上下文
                this.$axios(config)
                    .then(response => {

                    const res = response.data;
                    if (res.code == '200') {
                        console.log('获取当日数据成功', res.data);
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
                console.log('更新图表',data);
                
                const chartData = data.map(item => ({
                    value: item.eventCount, // 设定 y 轴的值
                    name: item.eventType // 图例和 x 轴的名称
                }));

                // console.log('chartData',chartData);              

                this.mychart.setOption({
                        series: [
                            {
                                data: chartData, // 将转换后的数据更新到图表中
                            },
                        ],
                        xAxis: {
                            data:data.map(item => item.eventType)
                        },

                });
            }
        },
    };
</script>

<style scoped lang="less">
    #demoDiv {
        width: 32rem;
        height: 19rem;
    }
</style>
