package icu.sunnyc.codegenerator;

import icu.sunnyc.codegenerator.entity.ClassInfo;
import icu.sunnyc.codegenerator.entity.ParamInfo;
import icu.sunnyc.codegenerator.service.GeneratorService;
import icu.sunnyc.codegenerator.utils.FreemarkerUtil;
import icu.sunnyc.codegenerator.utils.TableParseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CodeGeneratorApplicationTests {

    @Autowired
    private GeneratorService generatorService;

    @Test
    void contextLoads() {
    }

    /**
     * 测试解析 DDL SQL
     */
    @Test
    public void testGetClassInfo() {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setTableSql("-- IDCDB.T_NS_MESSAGE_POLICY definition\n" +
                "\n" +
                "CREATE TABLE \"IDCDB\".\"T_NS_MESSAGE_POLICY\" \n" +
                "   (\t\"POLICY_ID\" VARCHAR2(32) NOT NULL ENABLE, \n" +
                "\t\"POLICY_NAME\" VARCHAR2(128) NOT NULL ENABLE, \n" +
                "\t\"FLOW_TYPE\" NUMBER NOT NULL ENABLE, \n" +
                "\t\"START_TIME\" DATE NOT NULL ENABLE, \n" +
                "\t\"END_TIME\" DATE NOT NULL ENABLE, \n" +
                "\t\"DEL_FLAG\" CHAR(1) DEFAULT 0 NOT NULL ENABLE, \n" +
                "\t\"CREATE_BY\" VARCHAR2(64) NOT NULL ENABLE, \n" +
                "\t\"CREATE_TIME\" DATE NOT NULL ENABLE, \n" +
                "\t\"UPDATE_BY\" VARCHAR2(64), \n" +
                "\t\"UPDATE_TIME\" DATE, \n" +
                "\t\"RULE_TYPE\" NUMBER NOT NULL ENABLE, \n" +
                "\t\"EVENT_TYPE\" VARCHAR2(5) NOT NULL ENABLE, \n" +
                "\t\"RULE_DESCRIPTION\" VARCHAR2(128) NOT NULL ENABLE, \n" +
                "\t\"SNORT_RULE\" VARCHAR2(128), \n" +
                "\t\"MTX_RULE\" VARCHAR2(128), \n" +
                "\t\"IOC_RULE\" VARCHAR2(128), \n" +
                "\t\"IS_PCAP\" NUMBER NOT NULL ENABLE, \n" +
                "\t\"VERSION\" VARCHAR2(8) NOT NULL ENABLE\n" +
                "   ) SEGMENT CREATION IMMEDIATE \n" +
                "  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 \n" +
                " NOCOMPRESS LOGGING\n" +
                "  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645\n" +
                "  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1\n" +
                "  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)\n" +
                "  TABLESPACE \"IDCDBS\" ;\n" +
                "\n" +
                "COMMENT ON TABLE IDCDB.T_NS_MESSAGE_POLICY IS '恶意报文检测指令策略表';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.POLICY_ID IS '策略号';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.POLICY_NAME IS '策略名称';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.FLOW_TYPE IS '流量类型（1 入向 2 出向 3 双向）';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.START_TIME IS '策略生效时间';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.END_TIME IS '策略过期时间';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.DEL_FLAG IS '删除标志（0 未删除 2 已删除）';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.CREATE_BY IS '创建者';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.CREATE_TIME IS '创建时间';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.UPDATE_BY IS '更新者';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.UPDATE_TIME IS '更新时间';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.RULE_TYPE IS '规则类型（1 snort规则 2 mtx规则 3 威胁情报-IP规则 4 威胁情报-URL规则 5 威胁情报-域名规则）';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.EVENT_TYPE IS '事件分类（存储五位事件编号）';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.RULE_DESCRIPTION IS '规则描述';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.SNORT_RULE IS 'Snort 规则';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.MTX_RULE IS 'MTX规则';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.IOC_RULE IS '威胁情报规则';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.IS_PCAP IS '是否带pcap上报（0 不带pcap 1 带pcap）';\n" +
                "COMMENT ON COLUMN IDCDB.T_NS_MESSAGE_POLICY.VERSION IS '版本号';");
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
    }


    /**
     * 测试模板渲染
     */
    @Test
    public void testFreemarker() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hello", "hello world!");
        System.out.println(FreemarkerUtil.renderAndGetString("entity.ftl", params));
    }

    /**
     * 测试模板渲染业务接口
     */
    @Test
    public void testRenderTemplate() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("hello", "hello world!");
        Map<String, String> map = generatorService.renderTemplate(params);
        System.out.println(map);
    }

}
