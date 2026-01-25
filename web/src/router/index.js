import Vue from 'vue'
import VueRouter from 'vue-router'
import visual from '@/pages/visual.vue'
import login from '@/pages/login.vue'
import home from '@/pages/home.vue'

Vue.use(VueRouter)

const routes = [
  
  {
    path:'/',
    redirect:'/login'
  },
  {
    path:'/visual',
    name:'visual',
    component:visual
  },
  {
    path:'/login',
    name:'login',
    component:login
  },
  {
    path:'/home',
    name:'home',
    component:home
  }
]

const router = new VueRouter({
  routes
})

export default router
