package icu.sunnyc.codegenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 代码生成器配置属性信息获取
 */
@Data
@Component
public class CodeGeneratorProperties {

    /**
     * freemarker 模板文件夹路径
     */
    @Value("${freemarker.templates-path}")
    private String templatesPath;

    /**
     * 已删除的模板组文件夹存放目录
     */
    @Value("${freemarker.deleted-path}")
    private String deletedPath;
}
