import java.util.List;

/**
 * @author: houcheng
 * @email: houcheng@boco.com.cn
 * @date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @version: V1.0
 * @description: ${classInfo.classComment}服务接口
 * @modify
 */
public interface I${classInfo.className}Service {

    /**
     * 查询${classInfo.classComment}详情
     * @param ${classInfo.className?uncap_first}Id ${classInfo.classComment}id
     * @return ${classInfo.className}
     */
    ${classInfo.className} get${classInfo.className}ById(String ${classInfo.className?uncap_first}Id);

    /**
     * 查询${classInfo.classComment}列表
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}
     * @return ${classInfo.classComment}列表
     */
    List<${classInfo.className}> get${classInfo.className}List(${classInfo.className} ${classInfo.className?uncap_first});

    /**
     * 添加${classInfo.classComment}
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}
     * @return 影响行数
     */
    int insert${classInfo.className}(${classInfo.className} ${classInfo.className?uncap_first});

    /**
     * 更新${classInfo.classComment}信息
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}
     * @return 影响行数
     */
    int update${classInfo.className}(${classInfo.className} ${classInfo.className?uncap_first});

    /**
     * 批量删除${classInfo.classComment}数据
     * @param ${classInfo.className?uncap_first}Ids ${classInfo.classComment}id数组
     * @return 影响行数
     */
    int delete${classInfo.className}(String[] ${classInfo.className?uncap_first}Ids);

    /**
     * 导入${classInfo.classComment}
     * @param ${classInfo.className?uncap_first}List ${classInfo.classComment}列表
     * @return 导入结果情况
     */
    String import${classInfo.className}(List<${classInfo.className}> ${classInfo.className?uncap_first}List);

}
