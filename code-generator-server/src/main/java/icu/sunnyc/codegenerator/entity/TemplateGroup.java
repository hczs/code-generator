package icu.sunnyc.codegenerator.entity;

import lombok.Data;

import java.util.List;

/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 模板组
 */
@Data
public class TemplateGroup {

    private String groupName;

    private List<FreemarkerTemplate> templates;
}
