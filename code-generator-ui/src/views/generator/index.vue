<template>
  <div class="app-container scrollbar-main">

    <el-card>
      <h2>在下方粘贴建表 SQL 语句即可生成代码</h2>
    </el-card>

    <prism-editor
      v-model="paramInfo.tableSql"
      class="my-editor height-300 scrollbar-main"
      :highlight="highlighter"
      :line-numbers="lineNumbers"
    />
    <el-divider>参数配置区域，必须选择模板组才能生成代码</el-divider>
    <el-card header="参数配置">
      <el-form ref="paramForm" :model="formParam" :rules="formRules">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="模板组" prop="groupName" label-width="90px">
              <span slot="label">
                <el-tooltip effect="light" content="可以根据不同的模板生成不同的代码" placement="right-start">
                  <span><i class="el-icon-question" /> 模板组</span>
                </el-tooltip>
              </span>
              <el-select v-model="formParam.groupName" placeholder="请选择模板组" @change="templateGroupChange">
                <el-option
                  v-for="item in templateGroups"
                  :key="item.groupName"
                  :label="item.groupName"
                  :value="item.groupName"
                />
              </el-select>
            </el-form-item>
          </el-col>
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
        <el-radio-button
          v-for="item in currentTemplateList"
          :key="item.templateName"
          :label="currentGroup + '-' +item.templateName.split('.')[0]"
        >
          {{ item.templateName }}
        </el-radio-button>
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
import { getAllTemplate } from '@/api/template'
// 代码块组件
import { PrismEditor } from 'vue-prism-editor'
import 'vue-prism-editor/dist/prismeditor.min.css'
import { highlight, languages } from 'prismjs/components/prism-core'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-javascript'
import 'prismjs/themes/prism-tomorrow.css'

export default {
  name: 'Generator',
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
        groupName: 'default',
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
      loading: false,
      // 模板组列表
      templateGroups: [],
      // 当前选中的模板组
      currentGroup: '',
      // 当前模板列表
      currentTemplateList: [],
      // 参数填写表单校验
      formRules: {
        // groupName 必选
        groupName: [
          { required: true, message: '请选择模板组', trigger: 'blur' }
        ]
      }
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
    // 初始化模板组
    this.initTemplateGroups()
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

    // 初始化模板组信息
    initTemplateGroups() {
      getAllTemplate().then(res => {
        this.templateGroups = res.data
        // 下拉默认选择第一个
        this.formParam.groupName = this.templateGroups[0].groupName
        // 相关内容 多选按钮啥的也都初始化
        var value = this.formParam.groupName
        this.currentGroup = value
        this.currentTemplateList = this.templateGroups.find(item => item.groupName === value).templates
        this.selectedLabel = this.currentGroup + '-' + this.currentTemplateList[0].templateName.split('.')[0]
      })
    },

    // 模板组切换
    templateGroupChange(value) {
      this.currentGroup = value
      this.currentTemplateList = this.templateGroups.find(item => item.groupName === value).templates
      this.selectedLabel = this.currentGroup + '-' + this.currentTemplateList[0].templateName.split('.')[0]
    },

    // 生成代码
    generateCode() {
      this.$refs['paramForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.paramInfo.options = this.formParam
          generate(this.paramInfo).then(res => {
            this.resultCode = res.data
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
        } else {
          this.$message.error('请选择模板组')
        }
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
