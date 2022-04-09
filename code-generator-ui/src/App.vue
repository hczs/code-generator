<template>
  <div id="app" class="app-container">

    <h1>在下方粘贴建表SQL语句即可生成代码</h1>

    <prism-editor
      v-model="paramInfo.tableSql"
      class="my-editor height-300"
      :highlight="highlighter"
      :line-numbers="lineNumbers"
    />

    <el-button @click="generateCode">生成代码</el-button>
  </div>
</template>

<script>
import { generate } from '@/api/generate'
// 代码块组件
import { PrismEditor } from 'vue-prism-editor'
import 'vue-prism-editor/dist/prismeditor.min.css'
import { highlight, languages } from 'prismjs/components/prism-core'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-javascript'
import 'prismjs/themes/prism-tomorrow.css'

export default {
  name: 'App',
  components: {
    PrismEditor
  },

  data() {
    return {
      msg: 'Welcome to Your Vue.js App',
      code: 'console.log("Hello World")',
      lineNumbers: true, // true为编辑模式， false只展示不可编辑
      // 生成代码参数对象
      paramInfo: {
        // 表 DDL SQL
        tableSql: 'select * from user',
        // 附带额外参数 不断递增，可以随意添加参数
        options: {}
      }
    }
  },

  methods: {
    highlighter(code) {
      return highlight(code, languages.js) // returns html
    },
    generateCode() {
      generate(this.paramInfo).then(res => {
        console.log(res)
      })
    }
  }
}

</script>
<style lang="scss">
/* required class */
.my-editor {
  background: #2d2d2d;
  color: #ccc;

  font-family: Fira code, Fira Mono, Consolas, Menlo, Courier, monospace;
  font-size: 14px;
  line-height: 1.5;
  padding: 5px;
}

/* optional */
.prism-editor__textarea:focus {
  outline: none;
}

/* not required: */
.height-300 {
  height: 300px;
}
</style>

