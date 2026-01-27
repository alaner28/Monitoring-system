import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useAppStore = defineStore('app', () => {
  const monitorId = ref(0);

  // 计算属性
  const getMonitorId = computed(() => monitorId.value);

  // 操作方法
  function setMonitorId(id: number) {
    monitorId.value = id;
    // 同时保存到 sessionStorage 以维持现有功能
    sessionStorage.setItem('monitorId', id.toString());
  }

  // 从 sessionStorage 恢复数据
  function hydrateFromSessionStorage() {
    const monitorIdStr = sessionStorage.getItem('monitorId');
    if (monitorIdStr) {
      monitorId.value = parseInt(monitorIdStr, 10) || 0;
    }
  }

  return {
    // 状态
    monitorId,

    // 计算属性
    getMonitorId,

    // 操作方法
    setMonitorId,
    hydrateFromSessionStorage,
  };
});