package icu.sunnyc.codegenerator.controller;

import icu.sunnyc.codegenerator.entity.CommonResult;
import icu.sunnyc.codegenerator.entity.FreemarkerTemplate;
import icu.sunnyc.codegenerator.service.TemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 模板组及模板相关操作
 */
@Slf4j
@Api("模板操作")
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("/all")
    @ApiOperation("获取所有模板信息")
    public CommonResult getAllTemplate() {
        return CommonResult.ok().data(templateService.getAllTemplate());
    }

    @GetMapping("/all/content")
    @ApiOperation("获取所有模板信息，带模板内容")
    public CommonResult getAllTemplateContent() {
        return CommonResult.ok().data(templateService.getAllTemplateContent());
    }

    @GetMapping("/group/add/{groupName}")
    @ApiOperation("添加模板组")
    public CommonResult addTemplateGroup(@PathVariable("groupName") String groupName) {
        boolean success = templateService.addTemplateGroup(groupName);
        return success ? CommonResult.ok() : CommonResult.error();
    }

    @GetMapping("/group/rename/{originalGroupName}/{newGroupName}")
    @ApiOperation("重命名模板组")
    public CommonResult renameTemplateGroup(@PathVariable("originalGroupName") String originalGroupName,
                                            @PathVariable("newGroupName") String newGroupName) {
        boolean success = templateService.renameTemplateGroup(originalGroupName, newGroupName);
        return success ? CommonResult.ok() : CommonResult.error();
    }

    @DeleteMapping("/group/delete/{groupName}")
    @ApiOperation("删除模板组")
    public CommonResult deleteTemplateGroup(@PathVariable("groupName") String groupName) {
        boolean success = templateService.deleteTemplateGroup(groupName);
        return success ? CommonResult.ok() : CommonResult.error();
    }

    @PostMapping("/content/update/{groupName}")
    @ApiOperation("更新模板内容")
    public CommonResult addTemplateGroup(@PathVariable("groupName") String groupName,
                                         @RequestBody FreemarkerTemplate template) {
        boolean success = templateService.updateTemplateContent(
                groupName, template.getTemplateName(), template.getTemplateContent());
        return success ? CommonResult.ok() : CommonResult.error();
    }

    @GetMapping("/add/{groupName}/{templateName}")
    @ApiOperation("添加模板")
    public CommonResult addTemplateGroup(@PathVariable("groupName") String groupName,
                                         @PathVariable("templateName") String templateName) {
        boolean success = templateService.addTemplate(groupName, templateName);
        return success ? CommonResult.ok() : CommonResult.error();
    }

    @DeleteMapping("/delete/{groupName}/{templateName}")
    @ApiOperation("删除模板")
    public CommonResult deleteTemplateGroup(@PathVariable("groupName") String groupName,
                                            @PathVariable("templateName") String templateName) {
        boolean success = templateService.deleteTemplate(groupName, templateName);
        return success ? CommonResult.ok() : CommonResult.error();
    }
}
