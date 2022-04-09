package icu.sunnyc.codegenerator.service;


import java.util.List;
import java.util.Map;

/**
 * 生成代码 业务处理类
 * @author ：hc
 * @date ：Created in 2022/4/9 13:00
 * @modified ：
 */
public interface GeneratorService {


    /**
     * 将传入参数渲染模板上，并返回模板和渲染结果 map
     * @param params 渲染参数
     * @return 结果
     */
    Map<String, String> renderTemplate(Map<String, Object> params);

    /**
     * 获取所有可用模板
     * @return 所有可用模板
     */
    List<String> getAllTemplate();
}
