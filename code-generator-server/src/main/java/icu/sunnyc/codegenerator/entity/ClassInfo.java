package icu.sunnyc.codegenerator.entity;


import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 12:38
 * @modified ：
 */
@Data
@ToString
public class ClassInfo {

    /**
     * 表名 去除过前缀后的表名
     */
    private String tableName;

    /**
     * 原始表名
     */
    private String originTableName;

    /**
     * 实体类名
     */
    private String className;

    /**
     * 实体类描述信息
     */
    private String classComment;

    /**
     * 字段信息列表
     */
    private List<FieldInfo> fieldList;
}
