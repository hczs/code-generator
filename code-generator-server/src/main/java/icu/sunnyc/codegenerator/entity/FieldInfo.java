package icu.sunnyc.codegenerator.entity;

import lombok.Data;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 12:38
 * @modified ：
 */
@Data
public class FieldInfo {

    /**
     * 列名 表中的列名
     */
    private String columnName;

    /**
     * 字段名 下划线转驼峰后的 Java 实体类字段名
     */
    private String fieldName;

    /**
     * 字段类型 （Java 类型）
     */
    private String fieldClass;

    /**
     * swagger 类型
     */
    private String swaggerClass;

    /**
     * 字段注释信息
     */
    private String fieldComment;

}
