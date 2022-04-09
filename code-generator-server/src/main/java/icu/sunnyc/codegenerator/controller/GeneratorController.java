package icu.sunnyc.codegenerator.controller;

import icu.sunnyc.codegenerator.entity.ClassInfo;
import icu.sunnyc.codegenerator.entity.CommonResult;
import icu.sunnyc.codegenerator.entity.ParamInfo;
import icu.sunnyc.codegenerator.utils.MapUtil;
import icu.sunnyc.codegenerator.utils.TableParseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 代码生成器 控制器
 * @author ：hc
 * @date ：Created in 2022/4/9 12:46
 * @modified ：
 */
@Slf4j
@Api("代码生成器")
@RestController
@RequestMapping("/generate")
public class GeneratorController {

    @ApiOperation("生成代码")
    @PostMapping("/code")
    public CommonResult generate(@RequestBody ParamInfo paramInfo) {
        if (StringUtils.isEmpty(paramInfo.getTableSql())) {
            return CommonResult.error().message("表结构信息为空");
        }
        // 解析建表 sql
        ClassInfo classInfo = TableParseUtil.processTableIntoClassInfo(paramInfo);

        // 设置表格参数
        paramInfo.getOptions().put("classInfo", classInfo);
        paramInfo.getOptions().put("tableName", classInfo.getTableName());
        log.info("解析完毕的表对象：{}", classInfo);
        log.info("参数信息：{}", paramInfo);
        return CommonResult.ok();
    }
}
