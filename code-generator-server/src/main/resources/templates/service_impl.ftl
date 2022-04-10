import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boco.common.exception.ServiceException;
import com.boco.common.utils.DateUtils;
import com.boco.common.utils.IdUtils;
import com.boco.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: ${author}
 * @email: ${email}
 * @date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @version: V1.0
 * @description: ${classInfo.classComment}服务接口实现类
 * @modify
 */
@Service
public class ${classInfo.className}ServiceImpl implements I${classInfo.className}Service {

    @Autowired
    private ${classInfo.className}Mapper ${classInfo.className?uncap_first}Mapper;

    @Override
    public ${classInfo.className} get${classInfo.className}ById(String ${classInfo.className?uncap_first}Id) {
        LambdaQueryWrapper<${classInfo.className}> queryWrapper = new LambdaQueryWrapper<>();
        // 排除字段 create_by create_time update_by update_time
        queryWrapper.select(${classInfo.className}.class,
                message -> !message.getColumn().contains("create_") && !message.getColumn().contains("update_"))
                .eq(${classInfo.className}::get${classInfo.className}Id, ${classInfo.className?uncap_first}Id);
        return ${classInfo.className?uncap_first}Mapper.selectOne(queryWrapper);
    }

    @Override
    public List<${classInfo.className}> get${classInfo.className}List(${classInfo.className} ${classInfo.className?uncap_first}) {
        LambdaQueryWrapper<${classInfo.className}> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 查询条件
        return ${classInfo.className?uncap_first}Mapper.selectList(queryWrapper);
    }

    @Override
    public int insert${classInfo.className}(${classInfo.className} ${classInfo.className?uncap_first}) {
        // 一般都会有一个name不可重复
        if (check${classInfo.className}Name(${classInfo.className?uncap_first}.get${classInfo.className}Name())) {
            throw new ServiceException("${classInfo.classComment}名称重复");
        }
        // 版本号默认 3.0
        ${classInfo.className?uncap_first}.setVersion("3.0");
        // 设置短id
        ${classInfo.className?uncap_first}.set${classInfo.className}Id(IdUtils.genShortId());
        return ${classInfo.className?uncap_first}Mapper.insert(${classInfo.className?uncap_first});
    }

    @Override
    public int update${classInfo.className}(${classInfo.className} ${classInfo.className?uncap_first}) {
        ${classInfo.className?uncap_first}.set${classInfo.className}Id(null);
        return insert${classInfo.className}(${classInfo.className?uncap_first});
    }

    @Override
    public int delete${classInfo.className}(String[] ${classInfo.className?uncap_first}Ids) {
        int affectRow = 0;
        for (String ${classInfo.className?uncap_first}Id : ${classInfo.className?uncap_first}Ids) {
            ${classInfo.className} ${classInfo.className?uncap_first} = new ${classInfo.className}();
            ${classInfo.className?uncap_first}.set${classInfo.className}Id(${classInfo.className?uncap_first}Id);
            affectRow += ${classInfo.className?uncap_first}Mapper.deleteByIdWithFill(${classInfo.className?uncap_first});
        }
        return affectRow;
    }

    @Override
    public String import${classInfo.className}(List<${classInfo.className}> ${classInfo.className?uncap_first}List) {
        if (Objects.isNull(${classInfo.className?uncap_first}List) || ${classInfo.className?uncap_first}List.isEmpty()) {
            throw new ServiceException("${classInfo.classComment}不能为空");
        }
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        List<${classInfo.className}> prepareInsertList = new ArrayList<>();
        // 数据校验并收集错误信息
        for (int i = 0; i < ${classInfo.className?uncap_first}List.size(); i++) {
            ${classInfo.className} ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}List.get(i);
            // 导入规则
            try {
                // TODO 导入规则自己写
                // 校验通过加入预备插入列表
                prepareInsertList.add(${classInfo.className?uncap_first});
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>第" + (i+1) + "行，导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }
        // 有一行数据不规范的就不执行插入操作，提示不规范的数据信息
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 处数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            prepareInsertList.forEach(this::insert${classInfo.className});
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + prepareInsertList.size() + " 条");
        }
        return successMsg.toString();
    }

    /**
     * 检查名称是否重复
     * @param ${classInfo.className?uncap_first}Name 名称
     * @return 重复 true 不重复 false
     */
    private boolean check${classInfo.className}Name(String ${classInfo.className?uncap_first}Name) {
        LambdaQueryWrapper<${classInfo.className}> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(${classInfo.className}::get${classInfo.className}Name, ${classInfo.className?uncap_first}Name);
        ${classInfo.className} ${classInfo.className?uncap_first} = ${classInfo.className?uncap_first}Mapper.selectOne(queryWrapper);
        return Objects.nonNull(${classInfo.className?uncap_first});
    }

}
