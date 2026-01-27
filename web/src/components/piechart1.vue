<template>
  <div class="panel">
    <div class="chart" ref="piechart" id="demoDiv"></div>
    <div class="panel-footer"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, watch } from 'vue';
import * as echarts from 'echarts';
import axios from 'axios';
import { useAlarmStore } from '@/stores/alarm';
import { storeToRefs } from 'pinia';

// 定义图表数据接口
interface ChartItem {
    eventType: string;
    eventCount: number;
}

const alarmStore = useAlarmStore();
const { getWeekStatistics } = storeToRefs(alarmStore);

const piechart = ref<HTMLDivElement>();
let mychart: any = null; // 保存echarts实例
const chartData = computed(() => getWeekStatistics.value);

// 初始化图表
const initChart = (): void => {
    if(piechart.value) {
        mychart = echarts.init(piechart.value);

        // 设置初始的图表选项 (可以是空数据，稍后通过 axios 填充)
        mychart.setOption({
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
    }
};

// 从Pinia获取数据
const fetchChartData = (): void => {
    // 直接从Pinia获取数据并更新图表
    updateChart(chartData.value);
};

// 更新图表
const updateChart = (data: ChartItem[]): void => {
    const processedData = data.map(item => ({
        value: item.eventCount, // 设定 y 轴的值
        name: item.eventType // 图例和 x 轴的名称
    }));

    if(mychart) {
        mychart.setOption({
            series: [
                {
                    data: processedData, // 将转换后的数据更新到图表中
                },
            ],
            legend: {
                data: data.map(item => item.eventType), // 更新图例为 eventType
            },
        });
    }
};

// 处理事件总线的回调函数
let alarmHandler: (() => void) | null = null;

onMounted(() => {
    // 初始化图表
    initChart();

    //测试使用，一会删了
    // updateChart(chartData.value);

    fetchChartData();
    
    // 监听数据变化，自动更新图表
    watch(chartData, (newData) => {
        updateChart(newData);
    }, { deep: true });

    // 获取事件总线实例
    const bus = (window as any).$bus;
    if(bus) {
        alarmHandler = () => {
            console.log('piechart1接收到报警信息，准备更新');
            
            fetchChartData();
        };
        bus.$on('alarm', alarmHandler);
    }

    // 监听窗口大小调整事件
    window.addEventListener("resize", () => {
        if(mychart) {
            mychart.resize();
        }
    });
});

onUnmounted(() => {
    // 移除事件监听
    const bus = (window as any).$bus;
    if(bus && alarmHandler) {
        bus.$off('alarm', alarmHandler);
    }
    
    // 销毁图表实例
    if(mychart) {
        mychart.dispose();
    }
    
    // 移除窗口大小调整的监听事件
    window.removeEventListener("resize", () => {
        if(mychart) {
            mychart.resize();
        }
    });
});
</script>

<style scoped>
#demoDiv {
  width: 100%;
  height: 100%;
}
</style>
