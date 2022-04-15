package icu.sunnyc.codegenerator.service.impl;

import icu.sunnyc.codegenerator.entity.FreemarkerTemplate;
import icu.sunnyc.codegenerator.entity.TemplateGroup;
import icu.sunnyc.codegenerator.exception.CodeGenerateException;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.service.TemplateService;
import icu.sunnyc.codegenerator.utils.FreemarkerUtil;
import icu.sunnyc.codegenerator.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 13:23
 * @modified ：
 */
@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Autowired
    private TemplateService templateService;

    @Override
    public Map<String, String> renderTemplate(Map<String, Object> params) {
        HashMap<String, String> resultMap = new HashMap<>();
        List<TemplateGroup> templateGroups = templateService.getAllTemplate();
        String groupName = params.getOrDefault("groupName", "default").toString();
        for (TemplateGroup templateGroup : templateGroups) {
            if (templateGroup.getGroupName().equals(groupName)) {
                for (FreemarkerTemplate template : templateGroup.getTemplates()) {
                    String templateName = template.getTemplateName();
                    String result = FreemarkerUtil.renderAndGetString(groupName + File.separator + templateName, params);
                    resultMap.put(groupName + "-" + templateName.split("\\.")[0], result);
                }
            }
        }
        return resultMap;
    }

}
