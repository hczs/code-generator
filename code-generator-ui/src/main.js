import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import 'vue-prism-editor/dist/prismeditor.min.css' // import the styles

import App from './App'
import store from './store'

import '@/icons' // icon

Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  store,
  render: h => h(App)
})
