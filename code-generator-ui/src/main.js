import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import 'vue-prism-editor/dist/prismeditor.min.css' // import the styles

// json editor
import Editor from 'bin-ace-editor'
require('brace/mode/json')
require('brace/snippets/json')
require('brace/theme/solarized_light')
require('brace/theme/monokai')
require('brace/theme/terminal')
require('brace/theme/solarized_dark')
require('brace/theme/dracula')


import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon

Vue.use(ElementUI)
Vue.component(Editor.name, Editor)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
