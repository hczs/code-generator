package icu.sunnyc.codegenerator.service.impl;

import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.utils.FreemarkerUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 13:23
 * @modified ：
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Override
    public Map<String, String> renderTemplate(Map<String, Object> params) {
        HashMap<String, String> resultMap = new HashMap<>();
        List<String> allTemplate = getAllTemplate();
        for (String template : allTemplate) {
            params.put("hello", "hello, world!");
            String result = FreemarkerUtil.renderAndGetString(template, params);
            resultMap.put(template.split("\\.")[0], result);
        }
        return resultMap;
    }

    @Override
    public List<String> getAllTemplate() {
        // 后续数据库存储模板信息 可自定义上传模板相关操作
        ArrayList<String> templates = new ArrayList<>();
        templates.add("entity.ftl");
        templates.add("mapper.ftl");
        templates.add("service.ftl");
        templates.add("service_impl.ftl");
        templates.add("controller.ftl");
        templates.add("select.ftl");
        templates.add("index.ftl");
        templates.add("api.ftl");
        return templates;
    }

}
