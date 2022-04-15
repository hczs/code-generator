package icu.sunnyc.codegenerator.utils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import icu.sunnyc.codegenerator.config.CodeGeneratorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * Freemarker 渲染工具类
 * @author ：hc
 * @date ：Created in 2022/4/9 14:28
 * @modified ：
 */
@Slf4j
public class FreemarkerUtil {

    private static final Configuration freemarkerConfig = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

    static {
        try {
            CodeGeneratorProperties generatorProperties = SpringContextUtil.getBean(CodeGeneratorProperties.class);
            freemarkerConfig.setDirectoryForTemplateLoading(new File(generatorProperties.getTemplatesPath()));
            freemarkerConfig.setNumberFormat("#");
            freemarkerConfig.setClassicCompatible(true);
            freemarkerConfig.setDefaultEncoding("UTF-8");
            freemarkerConfig.setLocale(Locale.CHINA);
            freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private FreemarkerUtil() {
    }

    /**
     * 根据模板名称和参数渲染指定模板返回渲染完毕后的字符串
     * @param templateName 模板名称
     * @param parameters 参数
     * @return 字符串渲染结果
     */
    public static String renderAndGetString(String templateName, Map<String, Object> parameters) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            StringWriter result = new StringWriter();
            template.process(parameters, result);
            return escapeString(result.toString());
        } catch (IOException | TemplateException e) {
            log.error("模板渲染异常");
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 传入需要转义的字符串进行转义
     * @param originStr 原始字符串
     * @return 转义后的字符串
     */
    private static String escapeString(String originStr) {
        return originStr.replaceAll("井", "\\#").replaceAll("￥", "\\$");
    }
}
