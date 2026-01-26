import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// import 'lib-flexible/flexible.js'
import './flexible'
import axios from 'axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


Vue.config.productionTip = false
// axios.defaults.baseURL = 'http://123.57.77.71:10215'
// axios.defaults.baseURL = 'http://8.152.219.117:8001'
axios.defaults.baseURL = 'http://localhost:10215';
// axios.defaults.baseURL = '';
Vue.prototype.$axios = axios
Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: h => h(App),
  beforeCreate(){
    Vue.prototype.$bus = this
  }
}).$mount('#app')
