package icu.sunnyc.codegenerator;

import icu.sunnyc.codegenerator.entity.ClassInfo;
import icu.sunnyc.codegenerator.entity.ParamInfo;
import icu.sunnyc.codegenerator.entity.TemplateGroup;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.service.TemplateService;
import icu.sunnyc.codegenerator.utils.TableParseUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
// 按照 order 注解标志的顺序执行单元测试
@TestMethodOrder(OrderAnnotation.class)
class CodeGeneratorApplicationTests {

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private TemplateService templateService;

    String testGroupName = "hello";

    String testTemplateName = "hello";

    String testTemplateContent = "hello,world!";

    /**
     * 测试获取所有模板文件业务接口
     */
    @Test
    @Order(1)
    void testGetAllTemplates() {
        List<TemplateGroup> groupAndTemplatesMap = templateService.getAllTemplate();
        System.out.println(groupAndTemplatesMap);
        Assertions.assertTrue(groupAndTemplatesMap.size() > 0);
    }

    /**
     * 测试模板渲染业务接口
     */
    @Test
    @Order(2)
    void testRenderTemplate() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hello", "hello world!");
        Map<String, String> map = generatorService.renderTemplate(params);
        System.out.println(map);
        Assertions.assertTrue(map.size() > 0);
    }

    /**
     * 解析 DDL SQL 根据模板生成结果数据 全流程测试
     */
    @Test
    @Order(3)
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
        paramInfo.getOptions().put("ignorePrefix", "t_");
        // 解析 SQL
        ClassInfo classInfo = TableParseUtil.processTableIntoClassInfo(paramInfo);
        // 设置参数 模板中方便取值
        paramInfo.getOptions().put("classInfo", classInfo);
        // 设置用哪个模板组
        paramInfo.getOptions().put("groupName", "default");
        Map<String, String> map = generatorService.renderTemplate(paramInfo.getOptions());
        System.out.println(classInfo);
        System.out.println(map);
        Assertions.assertTrue(map.size() > 0);
    }


    /**
     * 测试创建模板组文件夹接口
     */
    @Test
    @Order(4)
    void testAddTemplateGroupDir() {
        Assertions.assertTrue(templateService.addTemplateGroup(testGroupName));
    }

    /**
     * 测试创建模板文件
     */
    @Test
    @Order(5)
    void testAddTemplate() {
        Assertions.assertTrue(templateService.addTemplate(testGroupName, testTemplateName));
    }

    /**
     * 测试 写入/更新 模板文件
     */
    @Test
    @Order(6)
    void testUpdateTemplate() {
        Assertions.assertTrue(templateService.updateTemplateContent(testGroupName, testTemplateName, testTemplateContent));
    }

    /**
     * 测试删除模板文件夹接口
     */
    @Test
    @Order(7)
    void testDeleteTemplateGroup() {
        Assertions.assertTrue(templateService.deleteTemplateGroup(testGroupName));
    }

}
