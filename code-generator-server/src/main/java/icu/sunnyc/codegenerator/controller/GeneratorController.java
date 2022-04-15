package icu.sunnyc.codegenerator.controller;

import icu.sunnyc.codegenerator.entity.ClassInfo;
import icu.sunnyc.codegenerator.entity.CommonResult;
import icu.sunnyc.codegenerator.entity.ParamInfo;
import icu.sunnyc.codegenerator.exception.CodeGenerateException;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.utils.MapUtil;
import icu.sunnyc.codegenerator.utils.TableParseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private GeneratorService generatorService;

    @PostMapping("/code")
    @ApiOperation("生成代码")
    public CommonResult generate(@RequestBody ParamInfo paramInfo) {
        if (StringUtils.isEmpty(paramInfo.getTableSql())) {
            return CommonResult.error().message("表结构信息为空");
        }
        log.info("传入参数额外options信息：{}", paramInfo.getOptions());
        try {
            // 解析建表 sql
            ClassInfo classInfo = TableParseUtil.processTableIntoClassInfo(paramInfo);
            // 设置相关参数
            paramInfo.getOptions().put("classInfo", classInfo);
            log.info("解析完毕的表对象：{}", classInfo);
            // hello world 必须有
            paramInfo.getOptions().put("hello", "hello, world!");
            // 渲染
            return CommonResult.ok().data(generatorService.renderTemplate(paramInfo.getOptions()));
        } catch (CodeGenerateException ex) {
            log.error("生成代码异常，具体信息：{}", ex.getMessage(), ex);
            return CommonResult.error().message(ex.getMessage());
        }
    }

}
