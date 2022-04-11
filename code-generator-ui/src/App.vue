<template>
  <div id="app" class="app-container scrollbar-main">

    <el-card>
      <h2>在下方粘贴建表 SQL 语句即可生成代码</h2>
    </el-card>

    <prism-editor
      v-model="paramInfo.tableSql"
      class="my-editor height-300 scrollbar-main"
      :highlight="highlighter"
      :line-numbers="lineNumbers"
    />
    <el-divider>参数配置区域，不填也不影响生成代码</el-divider>
    <el-card header="参数配置">
      <el-form :model="formParam">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="作者" label-width="60px">
              <span slot="label">
                <el-tooltip effect="light" content="类注释中的 @author 对应信息" placement="right-start">
                  <span><i class="el-icon-question" /> 作者</span>
                </el-tooltip>
              </span>
              <el-input v-model="formParam.author" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="邮箱" label-width="60px">
              <span slot="label">
                <el-tooltip effect="light" content="类注释中的 @email 对应信息" placement="right-start">
                  <span><i class="el-icon-question" /> 邮箱</span>
                </el-tooltip>
              </span>
              <el-input v-model="formParam.email" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="忽略表名前缀" label-width="100px">
              <el-input v-model="formParam.ignorePrefix" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="权限标识" label-width="90px">
              <span slot="label">
                <el-tooltip effect="light" content="@PreAuthorize 值的统一标识" placement="right-start">
                  <span><i class="el-icon-question" /> 权限标识</span>
                </el-tooltip>
              </span>
              <el-input v-model="formParam.permission" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="映射路径" label-width="90px">
              <span slot="label">
                <el-tooltip effect="light" content="controller 中 @RequestMapping 对应信息" placement="right-start">
                  <span><i class="el-icon-question" /> 映射路径</span>
                </el-tooltip>
              </span>
              <el-input v-model="formParam.requestMapping" />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <el-button v-loading.fullscreen.lock="loading" type="success" @click="generateCode">生成代码</el-button>
      <el-button @click="clear">清空</el-button>

    </el-card>

    <!-- 生成的代码显示区域 -->
    <el-card header="生成结果" style="margin-top: 10px">
      <el-radio-group v-model="selectedLabel" fill="#2F4F4F" @change="switchCode">
        <el-radio-button label="entity">entity</el-radio-button>
        <el-radio-button label="mapper">mapper</el-radio-button>
        <el-radio-button label="service">service</el-radio-button>
        <el-radio-button label="service_impl">serviceImpl</el-radio-button>
        <el-radio-button label="controller">controller</el-radio-button>
        <el-radio-button label="select">select.vue</el-radio-button>
        <el-radio-button label="index">index.vue</el-radio-button>
        <el-radio-button label="api">api.js</el-radio-button>
      </el-radio-group>

      <prism-editor
        v-model="curCode"
        class="my-editor scrollbar-main"
        :highlight="highlighter"
        :line-numbers="lineNumbers"
        @click="clickForCopy"
      />
    </el-card>

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
      // true为编辑模式， false只展示不可编辑
      lineNumbers: true,
      // 默认选中
      selectedLabel: 'entity',
      // 参数表单对象
      formParam: {
        author: 'sunnyc',
        email: 'hczshd@gmail.com',
        ignorePrefix: 't_',
        permission: 'system:user',
        requestMapping: '/system/user'
      },
      // 生成代码参数对象
      paramInfo: {
        // 表 DDL SQL
        tableSql: '',
        // 附带额外参数 不断递增，可以随意添加参数
        options: {}
      },
      // 返回结果代码对象 key 是代码类型(entity mapper...) value 是代码内容
      resultCode: {},
      // 结果代码 切换显示改变此变量
      curCode: '',
      // 加载动画开关
      loading: false
    }
  },

  created() {
    // 默认样例SQL
    this.paramInfo.tableSql = `CREATE TABLE 't_staff' (
  'id' char(19) NOT NULL,
  'username' varchar(10) NOT NULL COMMENT '用户名',
  'password' varchar(300) NOT NULL COMMENT '密码',
  'mail' varchar(50) NOT NULL COMMENT '邮箱',
  'gender' tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别 1男，2女',
  'avatar' varchar(300) DEFAULT NULL COMMENT '用户头像',
  'introduce' varchar(100) DEFAULT NULL COMMENT '个人介绍',
  'permission' int(11) NOT NULL DEFAULT '0' COMMENT '权限，0是普通用户，1是管理员',
  'del_flag' tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  'create_time' datetime NOT NULL COMMENT '创建时间',
  'update_time' datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY ('id'),
  UNIQUE KEY 'username' ('username') USING BTREE,
  UNIQUE KEY 'mail' ('mail')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;`
  },

  methods: {
    // 代码高亮显示
    highlighter(code) {
      return highlight(code, languages.js)
    },

    // 清空表单
    clear() {
      this.formParam = {}
    },

    // 生成代码
    generateCode() {
      this.loading = true
      this.paramInfo.options = this.formParam
      generate(this.paramInfo).then(res => {
        this.resultCode = res.data.code
        // 默认选中entity
        this.curCode = this.resultCode[this.selectedLabel]
        this.loading = false
        this.$message({
          message: '生成成功！',
          type: 'success'
        })
      }).catch(res => {
        this.$message({
          message: '出现异常：' + res.data.msg,
          type: 'error'
        })
        this.loading = false
      })
    },

    // 切换代码块
    switchCode(type) {
      this.curCode = this.resultCode[type]
    },

    // 点击复制输出区域的内容
    clickForCopy() {
      // TODO
      console.log('别点我')
    }
  }
}

</script>
<style lang="scss">
/* required class */
.my-editor {
  background: #2d2d2d;
  color: #ccc;
  margin-top: 10px;
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
  height: 350px;
}
/* 滚动条 */
main{
  width: 100%;
  padding: 39px 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.scrollbar-main {
  width:100%;
  overflow: auto;
}
.scrollbar-main .row {
  display: inline-block;
  padding: 12px;
  background: rgba(180,160,120,.1);
  text-align: center;
  white-space: nowrap;
}
.scrollbar-main::-webkit-scrollbar {
  width: 8px;
  background: white;
}
.scrollbar-main::-webkit-scrollbar-corner, /* 滚动条角落 */
.scrollbar-main::-webkit-scrollbar-thumb,
.scrollbar-main::-webkit-scrollbar-track {
  border-radius: 5px;
}
.scrollbar-main::-webkit-scrollbar-corner,
.scrollbar-main::-webkit-scrollbar-track { /* 滚动条轨道 */
  background-color: rgba(180,160,120,.1);
  box-shadow: inset 0 0 1px rgba(180,160,120,.5);
}
.scrollbar-main::-webkit-scrollbar-thumb { /* 滚动条手柄 */
  background-color: rgb(180,160,120);
}
</style>

