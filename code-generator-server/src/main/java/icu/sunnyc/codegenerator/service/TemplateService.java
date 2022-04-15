package icu.sunnyc.codegenerator.service;

import icu.sunnyc.codegenerator.entity.TemplateGroup;

import java.util.List;

/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 模板操作业务接口
 */
public interface TemplateService {

    /**
     * 获取所有可用模板
     * @return 模板组列表 包括模板组下的模板名称
     */
    List<TemplateGroup> getAllTemplate();

    /**
     * 获取所有可用模板
     * @return 模板组列表 包括模板组下的模板信息（名称及内容）
     */
    List<TemplateGroup> getAllTemplateContent();

    /**
     * 新增模板组
     * @param groupName 模板组名称
     * @return 是否创建成功
     */
    boolean addTemplateGroup(String groupName);

    /**
     * 重命名模板组名称 未测试 后续测试及开发
     * @param originalGroupName 原始名称
     * @param newGroupName 新名称
     * @return 是否命名成功
     */
    boolean renameTemplateGroup(String originalGroupName, String newGroupName);

    /**
     * 删除模板组 注：目前是假删除，而是把文件夹移动到另一个目录下
     * @param groupName 模板组名称
     * @return 是否删除成功
     */
    boolean deleteTemplateGroup(String groupName);

    /**
     * 添加模板
     * @param groupName 模板组名称
     * @param templateName 模板名称
     * @return 是否添加成功
     */
    boolean addTemplate(String groupName, String templateName);

    /**
     * 写入/更新 模板内容
     * @param groupName 模板组名称
     * @param templateName 模板名称
     * @param templateContent 模板内容
     * @return 是否写入成功
     */
    boolean updateTemplateContent(String groupName, String templateName, String templateContent);

    /**
     * 删除模板
     * @param groupName 模板组名称
     * @param templateName 模板名称
     * @return 是否删除成功
     */
    boolean deleteTemplate(String groupName, String templateName);
}
