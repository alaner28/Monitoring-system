import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// 定义报警项接口
export interface AlarmItem {
  id: number;
  name: string;              // 报警名称
  eventName: string;         // 事件名称
  level: number;             // 预警等级
  date: string;              // 日期时间
  department: string;        // 部门/区域
  deal: string;              // 处理状态
  content: string;           // 处理内容
  video: string;             // 视频链接
  phone: string;             // 电话号码
  [key: string]: any; // 其他可能的属性
}

// 定义图表数据接口
export interface ChartItem {
  eventType: string;
  eventCount: number;
}

export const useAlarmStore = defineStore('alarm', () => {
  // 报警列表
  const alarmList = ref<AlarmItem[]>([]);
  
  // 图表数据 - 当日统计
  const dayStatistics = ref<ChartItem[]>([]);
  
  // 图表数据 - 周统计
  const weekStatistics = ref<ChartItem[]>([]);
  
  // 图表数据 - 月统计
  const monthStatistics = ref<Record<string, { eventCount: number }>>({});
  
  // 图表数据 - 年统计
  const yearStatistics = ref<Record<string, { eventCount: number }>>({});
  
  // 天气数据
  const futureWeather = ref<Array<{
    date: string;
    dayweather: string;
    daytemp: string;
    nighttemp: string;
  }>>([]);
  
  const realTimeWeather = ref({
    temperature: 0,
    weather: ''
  });
  
  // 加载状态
  const isLoading = ref(false);

  // 计算属性
  // 获取报警列表
  const getAlarmList = computed(() => alarmList.value);
  
  // 获取当日统计数据
  const getDayStatistics = computed(() => dayStatistics.value);
  
  // 获取周统计数据
  const getWeekStatistics = computed(() => weekStatistics.value);
  
  // 获取月统计数据
  const getMonthStatistics = computed(() => monthStatistics.value);
  
  // 获取年统计数据
  const getYearStatistics = computed(() => yearStatistics.value);
  
  // 获取未来天气数据
  const getFutureWeather = computed(() => futureWeather.value);
  
  // 获取实时天气数据
  const getRealTimeWeather = computed(() => realTimeWeather.value);
  
  // 获取加载状态
  const getLoadingState = computed(() => isLoading.value);

  // 操作方法
  // 设置报警列表
  function setAlarmList(alarmListParam: AlarmItem[]) {
    alarmList.value = alarmListParam;
  }
  
  // 添加单个报警
  function addAlarm(alarm: AlarmItem) {
    alarmList.value.unshift(alarm);
  }
  
  // 更新报警状态
  function updateAlarmStatus(id: number, checked: boolean) {
    const index = alarmList.value.findIndex(alarm => alarm.id === id);
    if (index !== -1) {
      alarmList.value[index].checked = checked;
    }
  }
  
  // 设置当日统计数据
  function setDayStatistics(data: ChartItem[]) {
    dayStatistics.value = data;
  }
  
  // 设置周统计数据
  function setWeekStatistics(data: ChartItem[]) {
    weekStatistics.value = data;
  }
  
  // 设置月统计数据
  function setMonthStatistics(data: Record<string, { eventCount: number }>) {
    monthStatistics.value = data;
  }
  
  // 设置年统计数据
  function setYearStatistics(data: Record<string, { eventCount: number }>) {
    yearStatistics.value = data;
  }
  
  // 设置未来天气数据
  function setFutureWeather(data: Array<{
    date: string;
    dayweather: string;
    daytemp: string;
    nighttemp: string;
  }>) {
    futureWeather.value = data;
  }
  
  // 设置实时天气数据
  function setRealTimeWeather(data: { temperature: number; weather: string }) {
    realTimeWeather.value = data;
  }
  
  // 设置加载状态
  function setLoading(isLoadingParam: boolean) {
    isLoading.value = isLoadingParam;
  }
  
  // 增加特定事件类型的计数
  function incrementEventCount(eventType: string) {
    // 查找是否已存在该事件类型
    const existingIndex = dayStatistics.value.findIndex(item => item.eventType === eventType);
    
    if (existingIndex !== -1) {
      // 如果存在，则增加计数
      dayStatistics.value[existingIndex].eventCount += 1;
    } else {
      // 如果不存在，则添加新的事件类型
      dayStatistics.value.push({
        eventType,
        eventCount: 1
      });
    }
    
    // 同样更新周统计数据
    const weekExistingIndex = weekStatistics.value.findIndex(item => item.eventType === eventType);
    if (weekExistingIndex !== -1) {
      weekStatistics.value[weekExistingIndex].eventCount += 1;
    } else {
      weekStatistics.value.push({
        eventType,
        eventCount: 1
      });
    }
  }
  
  // 从报警列表更新统计数据
  function updateStatisticsFromAlarms() {
    // 重置统计数据
    dayStatistics.value = [];
    weekStatistics.value = [];
    
    // 根据报警列表生成统计数据
    const statsMap: Record<string, number> = {};
    
    alarmList.value.forEach(alarm => {
      const eventType = alarm.eventName; 
      statsMap[eventType] = (statsMap[eventType] || 0) + 1;
    });
    
    // 转换为ChartItem数组格式
    dayStatistics.value = Object.entries(statsMap).map(([eventType, eventCount]) => ({
      eventType,
      eventCount
    }));
    
    // 周统计数据使用相同的值（可以根据需要调整）
    weekStatistics.value = [...dayStatistics.value];
  }
  
  // 重置统计数据
  function resetStatistics() {
    dayStatistics.value = [];
    weekStatistics.value = [];
    monthStatistics.value = {};
    yearStatistics.value = {};
  }
  
  // 清空所有缓存数据
  function clearCache() {
    alarmList.value = [];
    dayStatistics.value = [];
    weekStatistics.value = [];
    monthStatistics.value = {};
    yearStatistics.value = {};
    futureWeather.value = [];
    realTimeWeather.value = {
      temperature: 0,
      weather: ''
    };
  }

  return {
    // 状态
    alarmList,
    dayStatistics,
    weekStatistics,
    monthStatistics,
    yearStatistics,
    futureWeather,
    realTimeWeather,
    isLoading,

    // 计算属性
    getAlarmList,
    getDayStatistics,
    getWeekStatistics,
    getMonthStatistics,
    getYearStatistics,
    getFutureWeather,
    getRealTimeWeather,
    getLoadingState,

    // 操作方法
    setAlarmList,
    addAlarm,
    updateAlarmStatus,
    setDayStatistics,
    setWeekStatistics,
    setMonthStatistics,
    setYearStatistics,
    setFutureWeather,
    setRealTimeWeather,
    setLoading,
    incrementEventCount,
    updateStatisticsFromAlarms,
    resetStatistics,
    clearCache,
  };
});