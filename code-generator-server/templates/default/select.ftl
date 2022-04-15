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

        <!-- 导出按钮 -->
        <el-row :gutter="10" class="mb8">
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
        </el-table>

        <!-- 分页 -->
        <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
        />
    </div>
</template>
<script>
    import { list${classInfo.className}, get${classInfo.className}, add${classInfo.className}, update${classInfo.className}, del${classInfo.className} } from "@/api/idc/ TODO";

    export default {
        name: '${classInfo.className?uncap_first}',
        // 字典
        dicts: [],
        data() {
            return {
                // 查询表单 model
                queryParams: {
                    pageNum: 1,
                    pageSize: 10
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
                // 导出 loading
                exportLoading: false,
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
            /** 导出按钮操作 */
            handleExport() {
                this.$modal.confirm('是否确认导出所有数据项？').then(() => {
                    this.exportLoading = true;
                    return this.download('${permission}:export', {
                        ...this.queryParams
                    }, `${classInfo.className}.xlsx`)
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
            }
        },

    }
</script>