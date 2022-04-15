import com.boco.common.utils.poi.ExcelUtil;
import com.boco.framework.aspectj.lang.annotation.Log;
import com.boco.framework.aspectj.lang.enums.BusinessType;
import com.boco.framework.web.controller.BaseController;
import com.boco.framework.web.domain.AjaxResult;
import com.boco.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author: ${author}
 * @email: ${email}
 * @date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @version: V1.0
 * @description: ${classInfo.classComment}控制器
 * @modify
 */
@RestController
@RequestMapping("${requestMapping}")
public class ${classInfo.className}Controller extends BaseController {

    @Autowired
    private I${classInfo.className}Service ${classInfo.className?uncap_first}Service;

    /**
     * ${classInfo.classComment} 列表查询
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}
     * @return TableDataInfo
     */
    @PreAuthorize("@ss.hasPermi('${permission}:list')")
    @GetMapping("list")
    public TableDataInfo list(${classInfo.className} ${classInfo.className?uncap_first}) {
        startPage();
        List<${classInfo.className}> ${classInfo.className?uncap_first}List = ${classInfo.className?uncap_first}Service.get${classInfo.className}List(${classInfo.className?uncap_first});
        return getDataTable(${classInfo.className?uncap_first}List);
    }

    /**
     * ${classInfo.classComment} 单条查询
     * @param ${classInfo.className?uncap_first}Id ${classInfo.classComment}id
     * @return AjaxResult
     */
    @PreAuthorize("@ss.hasPermi('${permission}:query')")
    @GetMapping("/{${classInfo.className?uncap_first}Id}")
    public AjaxResult getInfo(@PathVariable("${classInfo.className?uncap_first}Id") String ${classInfo.className?uncap_first}Id) {
        return AjaxResult.success(${classInfo.className?uncap_first}Service.get${classInfo.className}ById(${classInfo.className?uncap_first}Id));
    }

    /**
     * ${classInfo.classComment} 新增
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}对象
     * @return AjaxResult
     */
    @PreAuthorize("@ss.hasPermi('${permission}:add')")
    @Log(title = "${classInfo.classComment}", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ${classInfo.className} ${classInfo.className?uncap_first}) {
        return toAjax(${classInfo.className?uncap_first}Service.insert${classInfo.className}(${classInfo.className?uncap_first}));
    }

    /**
     * ${classInfo.classComment} 更新
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}对象
     * @return AjaxResult
     */
    @PreAuthorize("@ss.hasPermi('${permission}:edit')")
    @Log(title = "${classInfo.classComment}", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ${classInfo.className} ${classInfo.className?uncap_first}) {
        return toAjax(${classInfo.className?uncap_first}Service.update${classInfo.className}(${classInfo.className?uncap_first}));
    }

    /**
     * ${classInfo.classComment} 删除
     * @param ${classInfo.className?uncap_first}Ids ${classInfo.classComment}id数组
     * @return AjaxResult
     */
    @PreAuthorize("@ss.hasPermi('${permission}:remove')")
    @Log(title = "${classInfo.classComment}", businessType = BusinessType.DELETE)
    @DeleteMapping("/{${classInfo.className?uncap_first}Ids}")
    public AjaxResult remove(@PathVariable String[] ${classInfo.className?uncap_first}Ids) {
        return toAjax(${classInfo.className?uncap_first}Service.delete${classInfo.className}(${classInfo.className?uncap_first}Ids));
    }

    /**
     * 导出${classInfo.classComment}数据
     * @param response HttpServletResponse
     * @param ${classInfo.className?uncap_first} ${classInfo.classComment}对象
     */
    @PreAuthorize("@ss.hasPermi('${permission}:export')")
    @Log(title = "${classInfo.classComment}", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ${classInfo.className} ${classInfo.className?uncap_first}) {
        List<${classInfo.className}> dataList = ${classInfo.className?uncap_first}Service.get${classInfo.className}List(${classInfo.className?uncap_first});
        ExcelUtil<${classInfo.className}> util = new ExcelUtil<>(${classInfo.className}.class);
        util.exportExcel(response, dataList, "${classInfo.classComment}");
    }

    /**
     * ${classInfo.classComment} 导入模板下载
     * @param response HttpServletResponse
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<${classInfo.className}> util = new ExcelUtil<>(${classInfo.className}.class);
        util.importTemplateExcel(response, "${classInfo.classComment}");
    }

    @PreAuthorize("@ss.hasPermi('${permission}:import')")
    @Log(title = "${classInfo.classComment}", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<${classInfo.className}> util = new ExcelUtil<>(${classInfo.className}.class);
        List<${classInfo.className}> baseList = util.importExcel(file.getInputStream());
        String resultMessage = ${classInfo.className?uncap_first}Service.import${classInfo.className}(baseList);
        return AjaxResult.success(resultMessage);
    }

}
