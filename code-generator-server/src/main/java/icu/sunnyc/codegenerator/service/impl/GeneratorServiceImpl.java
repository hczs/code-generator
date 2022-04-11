package icu.sunnyc.codegenerator.service.impl;

import icu.sunnyc.codegenerator.exception.CodeGenerateException;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.utils.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 13:23
 * @modified ：
 */
@Service
@Slf4j
public class GeneratorServiceImpl implements GeneratorService {

    @Override
    public Map<String, String> renderTemplate(Map<String, Object> params) {
        HashMap<String, String> resultMap = new HashMap<>();
        List<String> allTemplate = getAllTemplate();
        for (String template : allTemplate) {
            String result = FreemarkerUtil.renderAndGetString(template, params);
            resultMap.put(template.split("\\.")[0], result);
        }
        return resultMap;
    }

    @Override
    public List<String> getAllTemplate() {
        // 后续数据库存储模板信息 可自定义上传模板相关操作
        ArrayList<String> templates = new ArrayList<>();
        try {
            File dir = ResourceUtils.getFile("classpath:templates");
            File[] files = dir.listFiles();
            assert files != null;
            Arrays.stream(files).forEach(file -> templates.add(file.getName()));
            return templates;
        } catch (FileNotFoundException e) {
            log.error("模板文件未找到：{}", e.getMessage(), e);
            throw new CodeGenerateException("模板文件未找到");
        }
    }
}
