<template>
    <div class="app-container">

        <!-- 查询表单 -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <h1>查询表单自己写</h1>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 增删改查导入导出按钮 -->
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="el-icon-plus"
                        @click="handleAdd"
                        v-hasPermi="['${permission}:add']"
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="el-icon-edit"
                        :disabled="single"
                        @click="handleUpdate"
                        v-hasPermi="['${permission}:edit']"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['${permission}:remove']"
                >删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="info"
                        plain
                        icon="el-icon-upload2"
                        @click="handleImport"
                        v-hasPermi="['${permission}:import']"
                >导入</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="warning"
                        plain
                        icon="el-icon-download"
                        :loading="exportLoading"
                        @click="handleExport"
                        v-hasPermi="['${permission}:export']"
                >导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 数据表格 -->
        <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
            <el-table-column fixed="left" type="selection" width="55" align="center"/>
            <#-- 遍历字段 -->
<#if classInfo.fieldList?? && classInfo.fieldList?size gt 0>
    <#list classInfo.fieldList as fieldItem >
        <#if fieldItem.fieldClass == "Date">
            <el-table-column label="${fieldItem.fieldComment}" align="center" prop="${fieldItem.fieldName}" width="180" show-overflow-tooltip>
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.${fieldItem.fieldName}, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
            </el-table-column>
        <#else>
            <el-table-column label="${fieldItem.fieldComment}" align="center" prop="${fieldItem.fieldName}"/>
        </#if>
    </#list>
</#if>
            <el-table-column fixed="right" label="操作" align="center" width="120" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['${permission}:edit']"
                    >修改</el-button>
                    <el-button
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['${permission}:remove']"
                    >删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
        />

        <!-- 恶意文件监测指令添加或修改对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
            <el-form :model="formData" :rules="rules" ref="form" label-width="120px" class="demo-form">
        <#-- 遍历字段 -->
        <#if classInfo.fieldList?? && classInfo.fieldList?size gt 0>
            <#list classInfo.fieldList as fieldItem >
                <el-form-item label="${fieldItem.fieldComment}" prop="${fieldItem.fieldName}">
                    <el-input v-model="formData.${fieldItem.fieldName}" placeholder="请输入${fieldItem.fieldComment}" clearable></el-input>
                </el-form-item>
            </#list>
        </#if>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="open = false">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 导入对话框 -->
        <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
            <el-upload
                    ref="upload"
                    :limit="1"
                    accept=".xlsx, .xls"
                    :headers="upload.headers"
                    :action="upload.url"
                    :disabled="upload.isUploading"
                    :on-progress="handleFileUploadProgress"
                    :on-success="handleFileSuccess"
                    :auto-upload="false"
                    drag
            >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip text-center" slot="tip">
                    <span>仅允许导入xls、xlsx格式文件。</span>
                    <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
                </div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import { list${classInfo.className}, get${classInfo.className}, add${classInfo.className}, update${classInfo.className}, del${classInfo.className} } from "@/api/idc/ TODO";
    import { getToken } from "@/utils/auth";

    export default {
        name: '${classInfo.className?uncap_first}',
        // 字典
        dicts: [],
        data() {
            return {
                // 查询表单 model
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                },
                // 搜索条件 日期区间
                dateRange: [],
                // 显示搜索条件
                showSearch: true,
                // 表格加载动画
                loading: false,
                // 表格数据列表
                tableDataList: [],
                total: 0,
                // 单选框选中的行
                ids: [],
                // 非单个禁用
                single: true,
                // 非多个禁用
                multiple: true,
                // 导出 loading
                exportLoading: false,
                // 控制对话框打开
                open: false,
                // 对话框标题
                title: '',
                // 上传相关
                upload: {
                    // 是否显示弹出层（用户导入）
                    open: false,
                    // 弹出层标题（用户导入）
                    title: "",
                    // 是否禁用上传
                    isUploading: false,
                    // 设置上传的请求头部
                    headers: { Authorization: "Bearer " + getToken() },
                    // 上传的地址
                    url: process.env.VUE_APP_BASE_API + "${requestMapping}/importData"
                },
                // 新增表单绑定数据对象
                formData: {},
                // 表单校验规则
                rules: {
                },
                ${classInfo.className?uncap_first}Id: null,
            };
        },

        created() {
            this.getList()
        },

        methods: {

            // 执行查询操作
            getList() {
                this.loading = true;
                list${classInfo.className}(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                    this.tableDataList = response.rows;
                    this.total = response.total;
                    this.loading = false;
                });
            },

            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
                this.title = "${classInfo.classComment}新增";
            },
            /** 新增对话框提交按钮 */
            submitForm() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.${classInfo.className?uncap_first}Id) {
                            const subForm = this.formData;
                            this.$modal.confirm('本次修改的策略将作为新策略保存且不会删除修改前策略，是否确认？').then(function() {
                                return update${classInfo.className}(subForm);
                            }).then(() => {
                                this.$modal.msgSuccess("新增成功");
                                this.open = false;
                                this.getList()
                            }).catch(() => {});
                        } else {
                            add${classInfo.className}(this.formData).then(response => {
                                this.$modal.msgSuccess("新增成功");
                                this.open = false;
                                this.getList()
                            });
                        }
                    }
                });
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                const ${classInfo.className?uncap_first}Id = row.${classInfo.className?uncap_first}Id || this.ids[0]
                if (${classInfo.className?uncap_first}Id) {
                    get${classInfo.className}(${classInfo.className?uncap_first}Id).then(response => {
                        this.formData = response.data;
                        this.open = true;
                        this.title = "${classInfo.classComment}修改";
                        this.${classInfo.className?uncap_first}Id = ${classInfo.className?uncap_first}Id;
                    });
                }
            },
            /** 删除按钮操作 */
            handleDelete(row) {
                const ${classInfo.className?uncap_first}Ids = row.${classInfo.className?uncap_first}Id || this.ids;
                this.$modal.confirm('是否确认删除策略号为"' + ${classInfo.className?uncap_first}Ids + '"的数据项？').then(function() {
                    return del${classInfo.className}(${classInfo.className?uncap_first}Ids);
                }).then(() => {
                    this.$modal.msgSuccess("删除成功");
                    // 刷新页面
                    this.getList()
                }).catch(() => {});
            },
            /** 表单重置 */
            reset() {
                this.formData = {
                    // 下拉默认选中第一个
                    // TODO
                };
                this.${classInfo.className?uncap_first}Id = null;
                this.resetForm("form");
            },
            /** 导入按钮操作 */
            handleImport() {
                this.upload.title = "${classInfo.classComment}导入";
                this.upload.open = true;
            },
            /** 下载模板操作 */
            importTemplate() {
                this.download('${requestMapping}/importTemplate', {}, `TODO.xlsx`)
            },
            /** 文件上传中处理 */
            handleFileUploadProgress(event, file, fileList) {
                this.upload.isUploading = true;
            },
            /** 文件上传成功处理 */
            handleFileSuccess(response, file, fileList) {
                this.upload.open = false;
                this.upload.isUploading = false;
                this.$refs.upload.clearFiles();
                this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
                this.getList();
            },
            /** 提交上传文件 */
            submitFileForm() {
                this.$refs.upload.submit();
            },
            /** 导出按钮操作 */
            handleExport() {
                this.$modal.confirm('是否确认导出所有数据项？').then(() => {
                    this.exportLoading = true;
                    return this.download('${requestMapping}/export', {
                        ...this.queryParams
                    }, `TODO.xlsx`)
                }).then(response => {
                    this.exportLoading = false;
                }).catch(() => {});
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.resetForm("queryForm");
                this.dateRange = [];
                this.handleQuery();
            },
            /** 多选框选中数据 */
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.${classInfo.className?uncap_first}Id)
                this.single = selection.length!==1
                this.multiple = !selection.length
            },
        },

    }
</script>