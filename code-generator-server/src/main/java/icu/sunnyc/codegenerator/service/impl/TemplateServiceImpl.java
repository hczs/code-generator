package icu.sunnyc.codegenerator.service.impl;

import icu.sunnyc.codegenerator.entity.FreemarkerTemplate;
import icu.sunnyc.codegenerator.entity.TemplateGroup;
import icu.sunnyc.codegenerator.exception.CodeGenerateException;
import icu.sunnyc.codegenerator.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 模板操作 业务接口实现类
 */
@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    @Value("${freemarker.templates-path}")
    private String templatesPath;

    @Value("${freemarker.deleted-path}")
    private String deletedPath;
    
    @Value("${generator.protected-group}")
    private List<String> protectedGroupList;

    private static final String FILE_SUFFIX = ".ftl";

    @Override
    public List<TemplateGroup> getAllTemplate() {
        try {
            return getAllTemplateGroup(false);
        } catch (IOException e) {
            log.error("读取模板文件内容异常：{}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<TemplateGroup> getAllTemplateContent() {
        try {
            return getAllTemplateGroup(true);
        } catch (IOException e) {
            log.error("读取模板文件内容异常：{}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean addTemplateGroup(String groupName) {
        String dirPath = templatesPath + File.separator + groupName;
        // 判断是否存在
        if (FileUtils.isDirectory(new File(dirPath))) {
            log.warn("模板组文件夹已存在，文件夹路径：{}", dirPath);
            throw new CodeGenerateException("模板组文件夹已存在");
        }
        try {
            FileUtils.forceMkdir(new File(dirPath));
            return true;
        } catch (IOException e) {
            log.error("创建模板组文件夹失败，文件夹路径： {}", dirPath, e);
        }
        return false;
    }

    @Override
    public boolean renameTemplateGroup(String originalGroupName, String newGroupName) {
        String srcDir = templatesPath + File.separator + originalGroupName;
        String destDir = templatesPath + File.separator + newGroupName;
        try {
            FileUtils.moveDirectory(new File(srcDir), new File(destDir));
            return true;
        } catch (IOException e) {
            log.error("重命名模板组文件夹失败，文件夹路径： {}", srcDir, e);
        }
        return false;
    }

    @Override
    public boolean deleteTemplateGroup(String groupName) {
        if (protectedGroupList.contains(groupName)) {
            log.warn("默认模板组无法删除，模板组名称：{}", groupName);
            throw new CodeGenerateException("默认模板组无法删除");
        }
        String srcDir = templatesPath + File.separator + groupName;
        String destDir = deletedPath + File.separator + RandomUtils.nextInt() + "-" + groupName;
        try {
            FileUtils.moveDirectory(new File(srcDir), new File(destDir));
            return true;
        } catch (IOException e) {
            log.error("删除模板组文件夹失败，文件夹路径： {}", srcDir, e);
        }
        return false;
    }

    @Override
    public boolean addTemplate(String groupName, String templateName) {
        String filePath = templatesPath + File.separator + groupName + File.separator + templateName + FILE_SUFFIX;
        // 判断是否存在
        if (new File(filePath).exists()) {
            log.warn("模板文件已存在，文件夹路径：{}", filePath);
            throw new CodeGenerateException("模板文件已存在");
        }
        try {
            FileUtils.touch(new File(filePath));
            return true;
        } catch (IOException e) {
            log.error("模板文件创建失败，模板文件路径： {}", filePath, e);
        }
        return false;
    }

    @Override
    public boolean updateTemplateContent(String groupName, String templateName, String templateContent) {
        if (protectedGroupList.contains(groupName)) {
            log.warn("默认模板组下的默认模板无法修改，模板组名称：{}", groupName);
            throw new CodeGenerateException("默认模板组下的默认模板无法修改");
        }
        String filePath = templatesPath + File.separator + groupName + File.separator + templateName + FILE_SUFFIX;
        try {
            FileUtils.writeStringToFile(new File(filePath), templateContent, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.error("模板文件内容写入失败，模板文件路径： {}", filePath, e);
        }
        return false;
    }

    @Override
    public boolean deleteTemplate(String groupName, String templateName) {
        if (protectedGroupList.contains(groupName)) {
            log.warn("默认模板组下的默认模板无法删除，模板组名称：{}", groupName);
            throw new CodeGenerateException("默认模板组下的默认模板无法删除");
        }
        String srcFile = templatesPath + File.separator + groupName + File.separator + templateName;
        String destFile = deletedPath + File.separator + RandomUtils.nextInt() + "-" + templateName;
        try {
            FileUtils.moveFile(new File(srcFile), new File(destFile));
            return true;
        } catch (IOException e) {
            log.error("删除模板文件失败，文件路径： {}", srcFile, e);
        }
        return false;
    }

    private List<TemplateGroup> getAllTemplateGroup(boolean allowContent) throws IOException {
        // 模板文件夹存放逻辑 templates/group(default group1 group2 etc...)/example.ftl
        ArrayList<TemplateGroup> templateGroups = new ArrayList<>();
        File templatesDirectory = new File(templatesPath);
        File[] templatesGroup = templatesDirectory.listFiles();
        assert templatesGroup != null;
        // 遍历模板组文件夹
        for (File group : templatesGroup) {
            File[] files = group.listFiles();
            ArrayList<FreemarkerTemplate> templates = new ArrayList<>();
            TemplateGroup curGroup = new TemplateGroup();
            assert files != null;
            // 每个模板组下的所有模板文件 封装到 freemarkerTemplate 对象中
            for (File file : files) {
                FreemarkerTemplate freemarkerTemplate = new FreemarkerTemplate();
                freemarkerTemplate.setTemplateName(file.getName());
                if (allowContent) {
                    String fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    freemarkerTemplate.setTemplateContent(fileContent);
                }
                templates.add(freemarkerTemplate);
            }
            curGroup.setGroupName(group.getName());
            curGroup.setTemplates(templates);
            templateGroups.add(curGroup);
        }
        return templateGroups;
    }

}
