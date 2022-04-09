package icu.sunnyc.codegenerator.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 12:40
 * @modified ：
 */
@Data
@ToString
public class ParamInfo {

    /**
     * 表 DDL SQL
     */
    private String tableSql;

    /**
     * 额外附带参数 不断新增
     */
    private Map<String,Object> options;
}
