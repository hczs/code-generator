package icu.sunnyc.codegenerator.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

/**
 * @author: sunnyc
 * @version: V1.0
 * @description: 代码生成器配置属性信息获取
 */
@Data
@Slf4j
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

    @PostConstruct
    public void initProperties() {
        // bean 初始化后检查目录是否存在
        log.info("初始化 CodeGeneratorProperties");
        File templatesDirectory = new File(templatesPath);
        File deleteDirectory = new File(deletedPath);
        try {
            if (!templatesDirectory.exists()) {
                log.info("模板目录不存在，正在创建目录: {}", templatesDirectory);
                FileUtils.forceMkdir(templatesDirectory);
            }
            if (!deleteDirectory.exists()) {
                log.info("删除目录不存在，正在创建目录: {}", deleteDirectory);
                FileUtils.forceMkdir(deleteDirectory);
            }
        } catch (IOException e) {
            log.error("创建相关配置文件夹失败，模板文件夹路径：{}；存放已删除文件目录：{}", templatesDirectory, deleteDirectory, e);
        }
    }
}
