import com.baomidou.mybatisplus.annotation.TableName;
import com.boco.framework.aspectj.lang.annotation.Excel;
import com.boco.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * @author: ${author}
 * @email: ${email}
 * @date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @version: V1.0
 * @description: ${classInfo.classComment}
 * @modify
 */
@Data
@TableName("${classInfo.originTableName}")
public class ${classInfo.className} extends BaseEntity {

<#if classInfo.fieldList?? && classInfo.fieldList?size gt 0>
<#list classInfo.fieldList as fieldItem >
    /**
     * ${fieldItem.fieldComment}
     */
    @Excel(name = "${fieldItem.fieldComment}"<#if fieldItem.fieldClass == "Date">, dateFormat = "yyyy-MM-dd HH:mm:ss"</#if>)
    private ${fieldItem.fieldClass} ${fieldItem.fieldName};

</#list>
</#if>

}
