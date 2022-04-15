<template>
  <div>
    <el-container class="el-container">

      <el-aside width="200px">
        <el-scrollbar style="height: 100%">
          <!-- <el-menu
            id="el-menu"
            unique-opened
            @select="menuSelect"
          >
            <el-submenu
              v-for="item in templateGroupList"
              :key="item.groupName"
              :index="item.groupName"
            >
              <template slot="title"><i class="el-icon-folder" />{{ item.groupName }}</template>
              <el-menu-item
                v-for="subItem in item.templates"
                :key="subItem.templateName"
                :index="subItem.templateName"
              >
                <i class="el-icon-document" />{{ subItem.templateName }}
              </el-menu-item>
            </el-submenu>
          </el-menu> -->
          <el-tree
            id="el-tree"
            :data="treeData"
            :props="defaultProps"
            class="tree-style"
            @node-click="handleNodeClick"
            @node-contextmenu="handleNodeContextMenu"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span>
                <span @click="show(node, data)">
                  <!-- 判断 node 是否有 value 这个 key -->
                  <i v-if="node.data.value" class="el-icon-document" />
                  <i v-else class="el-icon-folder" />
                  {{ node.label }}
                </span>
              </span>
            </span>
          </el-tree>
        </el-scrollbar>
      </el-aside>

      <el-container>
        <el-header style="text-align: right" height="40px">
          <el-button style="margin-top: 7px" type="success" plain @click="openAddGroup">新增模板组</el-button>
          <el-button style="margin-top: 7px" type="success" plain @click="openAddTemplate">新增模板</el-button>
          <el-button style="margin-top: 7px" type="success" plain @click="submitTemplateContent">提交修改</el-button>
        </el-header>
        <el-main style="padding: 0 5px">
          <prism-editor
            v-model="curCode"
            class="my-editor scrollbar-main"
            :highlight="highlighter"
            :line-numbers="lineNumbers"
            style="height: 600px"
          />
        </el-main>
      </el-container>
    </el-container>

    <!-- 新增模板组文件对话框 -->
    <el-dialog title="新增模板组" :visible.sync="groupDialog">
      <el-form :model="groupForm" :inline="true">
        <el-form-item label="模板组名称" label-width="100">
          <el-input v-model="groupForm.groupName" style="width: 366px" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="groupDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitAddGroup">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 新增模板文件对话框 -->
    <el-dialog title="新增模板" :visible.sync="templateDialog">
      <el-form :model="templateForm" :inline="true">
        <el-form-item label="模板组" prop="groupName" label-width="100">
          <el-select v-model="templateForm.groupName" placeholder="请选择模板组">
            <el-option
              v-for="item in templateGroupList"
              :key="item.groupName"
              :label="item.groupName"
              :value="item.groupName"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="模板名称" label-width="100">
          <el-input v-model="templateForm.templateName" style="width: 366px" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="templateDialog = false">取 消</el-button>
        <el-button type="primary" @click="submitAddTemplate">确 定</el-button>
      </div>
    </el-dialog>

    <div v-show="contextMenuVisible" id="menu" @mouseleave="contextMenuVisible=!contextMenuVisible">

      <el-card class="box-card">
        <div class="text item">
          <el-link
            v-show="rightIsGroup"
            icon="el-icon-circle-plus-outline"
            :underline="false"
            @click="templateDialog = true"
          >
            添加
          </el-link>
        </div>
        <div class="text item">
          <el-link
            icon="el-icon-remove-outline"
            :underline="false"
            @click="deleteGroupOrTemplate"
          >
            删除
          </el-link>
        </div>
      </el-card>

    </div>
  </div>
</template>

<script>
import { getAllTemplateContent, addTemplateGroup, addTemplate, updateTemplateContent, deleteTemplateGroup, deleteTemplate } from '@/api/template'
// 代码块组件
import { PrismEditor } from 'vue-prism-editor'
import 'vue-prism-editor/dist/prismeditor.min.css'
import { highlight, languages } from 'prismjs/components/prism-core'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-javascript'
import 'prismjs/themes/prism-tomorrow.css'
export default {
  name: 'Template',
  components: {
    PrismEditor
  },
  data() {
    return {
      // 模板组列表
      templateGroupList: [],
      lineNumbers: true,
      // 当前模板代码
      curCode: '',
      // 当前选中的导航菜单 一级
      curGroupName: '',
      // 当前选中的导航菜单 二级
      curTemplateName: '',
      // 新增模板组对话框
      groupDialog: false,
      // 新增模板组表单
      groupForm: {
        groupName: ''
      },
      // 新增模板对话框
      templateDialog: false,
      // 新增模板表单
      templateForm: {
        groupName: '',
        templateName: ''
      },
      // 右键选中元素
      contextMenuTarget: null,
      // 右键菜单显示
      contextMenuVisible: false,
      defaultProps: {
        children: 'templates',
        label: 'label'
      },
      // 右侧 el-tree 数据
      treeData: [],
      // 标志当前右键是选中的模板组还是模板
      rightIsGroup: true
    }
  },

  created() {
    this.initTemplate()
  },

  mounted() {
    this.$nextTick(() => {
      // vue-context-menu 需要传入一个触发右键事件的元素，等页面 dom 渲染完毕后才可获取
      // this.contextMenuTarget = document.querySelector('#el-tree')
      // console.log('mounted')
      // const tree = document.querySelectorAll('#el-tree [role="treeitem"]')
      // tree.forEach(i => {
      //   i.addEventListener('contextmenu', event => {
      //     // 如果右键了，则模拟点击这个treeitem
      //     event.target.click()
      //   })
      // })
    })
  },

  methods: {
    // 初始化模板信息
    initTemplate() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      getAllTemplateContent().then(res => {
        this.templateGroupList = res.data
        // treeData
        this.treeData = this.templateGroupList.map(item => {
          return {
            label: item.groupName,
            templates: item.templates.map(template => {
              return {
                label: template.templateName,
                value: template.templateName
              }
            })
          }
        })
        loading.close()
      }).catch(err => {
        loading.close()
        this.$message.error(err.message)
      })
    },

    show(node, data) {
      console.log(node)
      console.log(data)
    },

    // 树目录右键点击
    handleNodeContextMenu(event, data, node, self) {
      this.contextMenuVisible = true
      if (node.data.value) {
        // 代表是子节点
        this.rightIsGroup = false
      } else {
        this.rightIsGroup = true
      }
      // node.data.value 存在就是子节点
      if (node.data.value) {
        // 子节点
        this.rightIsGroup = false
        this.curTemplateName = node.data.value
      } else {
        // 父节点
        this.rightIsGroup = true
        this.curGroupName = node.label
        this.templateForm.groupName = this.curGroupName
        this.templateForm.templateName = ''
      }
      const menu = document.querySelector('#menu')
      menu.style.cssText = 'position: fixed; left: ' + (event.clientX - 10) + 'px' + '; top: ' + (event.clientY - 25) + 'px; z-index: 999; cursor:pointer;'
    },

    // 节点点击
    handleNodeClick(data, node, self) {
      // 先看看是不是子节点
      if (node.data.value) {
        // 是子节点
        this.curTemplateName = data.value
        // 获取父节点的名称
        this.curGroupName = node.parent.data.label
        // 遍历 templateGroupList 找到模板 list
        const templateList = this.templateGroupList.find(item => item.groupName === this.curGroupName)
        // 遍历模板 list 找到模板
        const template = templateList.templates.find(item => item.templateName === this.curTemplateName)
        if (template) {
          this.curCode = template.templateContent
        }
      } else {
        // 是模板组
        this.curGroupName = data.label
      }
    },

    // 删除模板或模板组
    deleteGroupOrTemplate() {
      // 判断删的是模板还是模板组
      if (this.rightIsGroup) {
        // 删除模板组
        // 弹出确认框
        this.$confirm(`此操作将永久删除「${this.curGroupName}」模板组及模板组下所有模板, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 确定删除
          deleteTemplateGroup(this.curGroupName).then(res => {
            if (res.code === 0) {
              // 删除成功
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.initTemplate()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败!'
              })
            }
          })
        }).catch(() => {
          // 取消删除
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      } else {
        // 删除模板
        // 弹出确认框
        this.$confirm(`此操作将永久删除「${this.curTemplateName}」模板, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 确定删除
          deleteTemplate(this.curGroupName, this.curTemplateName).then(res => {
            if (res.code === 0) {
              // 删除成功
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.initTemplate()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败!'
              })
            }
          })
        }).catch(() => {
          // 取消删除
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },

    // 菜单选中
    menuSelect(index, indexPath) {
      // indexPath[0] 是一级菜单的值 indexPath[1]是二级菜单的值 index 是当前菜单的值
      this.curGroupName = indexPath[0]
      // 先从 templateGroupList 找到对应的模板list
      const templateList = this.templateGroupList.find(item => item.groupName === indexPath[0]).templates
      // 再从模板list中找到对应的模板
      const template = templateList.find(item => item.templateName === index)
      if (template) {
        this.curTemplateName = template.templateName
        this.curCode = template.templateContent
      } else {
        this.curTemplateName = ''
        this.curCode = ''
      }
    },

    // 提交模板内容修改
    submitTemplateContent() {
      var params = {
        templateName: this.curTemplateName.split('.')[0],
        templateContent: this.curCode
      }
      if (this.curTemplateName === '') {
        this.$message({
          type: 'warning',
          message: '请选择模板'
        })
        return
      }
      // 弹出确认框
      this.$confirm(`此操作将更新「${this.curTemplateName}」模板, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确定更新
        updateTemplateContent(this.curGroupName, params).then(res => {
          if (res.code === 0) {
            // 更新成功
            this.$message({
              type: 'success',
              message: '更新成功!'
            })
            this.initTemplate()
          } else {
            this.$message({
              type: 'error',
              message: '更新失败!'
            })
          }
        })
      }).catch(() => {
        // 取消更新
        this.$message({
          type: 'info',
          message: '已取消更新'
        })
      })
      // updateTemplateContent(this.curGroupName, params).then(res => {
      //   this.$message({
      //     message: '更新成功',
      //     type: 'success'
      //   })
      //   this.initTemplate()
      // })
    },

    // 打开新增模板组对话框
    openAddGroup() {
      this.groupForm = {}
      this.groupDialog = true
    },

    // 模板组新增 提交
    submitAddGroup() {
      addTemplateGroup(this.groupForm.groupName).then(res => {
        this.groupDialog = false
        this.initTemplate()
        this.$message({
          message: '添加成功',
          type: 'success'
        })
      })
    },

    // 新增模板
    openAddTemplate() {
      this.templateForm = {}
      this.templateDialog = true
    },

    // 新增模板提交
    submitAddTemplate() {
      addTemplate(this.templateForm.groupName, this.templateForm.templateName).then(res => {
        this.templateDialog = false
        this.initTemplate()
        this.$message({
          message: '添加成功',
          type: 'success'
        })
      })
    },

    // 代码高亮显示
    highlighter(code) {
      return highlight(code, languages.js)
    }
  }
}
</script>

<style lang="scss">
.el-container {
  height: 666px;
}
.tree-style {
  margin-top: 45px;
  font-size: 16px;
}
.custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 16px;
    padding-right: 8px;
  }

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
// aside 不显示横向滚动条
.el-scrollbar__wrap {
  overflow-x: hidden;
}
// 右键菜单样式
.text {
    font-size: 14px;
}

.item {
    margin-bottom: 10px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}
.clearfix:after {
    clear: both
}

.box-card {
    width: 100px;
}
</style>
