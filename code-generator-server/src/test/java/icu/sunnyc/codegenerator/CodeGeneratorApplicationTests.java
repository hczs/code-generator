package icu.sunnyc.codegenerator;

import icu.sunnyc.codegenerator.entity.ClassInfo;
import icu.sunnyc.codegenerator.entity.ParamInfo;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.utils.FreemarkerUtil;
import icu.sunnyc.codegenerator.utils.TableParseUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CodeGeneratorApplicationTests {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 测试解析 DDL SQL
     */
    @Test
    void testGetClassInfo() {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setTableSql("CREATE TABLE 't_staff' (\n" +
                "  'id' char(19) NOT NULL,\n" +
                "  'username' varchar(10) NOT NULL COMMENT '用户名',\n" +
                "  'password' varchar(300) NOT NULL COMMENT '密码',\n" +
                "  'mail' varchar(50) NOT NULL COMMENT '邮箱',\n" +
                "  'gender' tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别 1男，2女',\n" +
                "  'avatar' varchar(300) DEFAULT NULL COMMENT '用户头像',\n" +
                "  'introduce' varchar(100) DEFAULT NULL COMMENT '个人介绍',\n" +
                "  'permission' int(11) NOT NULL DEFAULT '0' COMMENT '权限，0是普通用户，1是管理员',\n" +
                "  'del_flag' tinyint(4) NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',\n" +
                "  'create_time' datetime NOT NULL COMMENT '创建时间',\n" +
                "  'update_time' datetime NOT NULL COMMENT '更新时间',\n" +
                "  PRIMARY KEY ('id'),\n" +
                "  UNIQUE KEY 'username' ('username') USING BTREE,\n" +
                "  UNIQUE KEY 'mail' ('mail')\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        // 设置表格参数
        HashMap<String, Object> options = new HashMap<>();
        paramInfo.setOptions(options);
        // 忽略表前缀
        paramInfo.getOptions().put("ignorePrefix", "T_NS_");
        ClassInfo classInfo = TableParseUtil.processTableIntoClassInfo(paramInfo);

        // 设置俩参数模板方便取值
        paramInfo.getOptions().put("classInfo", classInfo);
        paramInfo.getOptions().put("tableName", classInfo.getTableName());
        Map<String, String> map = generatorService.renderTemplate(paramInfo.getOptions());
        System.out.println(classInfo);
        System.out.println(map);
        Assertions.assertTrue(map.size() > 0);
    }


    /**
     * 测试模板渲染
     */
    @Test
    void testFreemarker() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hello", "hello world!");
        String result = FreemarkerUtil.renderAndGetString("entity.ftl", params);
        Assertions.assertNotNull(result);
    }

    @Test
    void testGetAllTemplates() {
        List<String> allTemplate = generatorService.getAllTemplate();
        System.out.println(allTemplate);
        Assertions.assertTrue(allTemplate.size() > 0);
    }

    /**
     * 测试模板渲染业务接口
     */
    @Test
    void testRenderTemplate() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hello", "hello world!");
        Map<String, String> map = generatorService.renderTemplate(params);
        System.out.println(map);
        Assertions.assertTrue(map.size() > 0);
    }

}
