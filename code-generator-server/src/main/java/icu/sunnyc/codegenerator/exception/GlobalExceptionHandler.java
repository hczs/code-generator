package icu.sunnyc.codegenerator.exception;

import icu.sunnyc.codegenerator.entity.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author ：hc
 * @date ：Created in 2022/4/16 11:32
 * @modified ：
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public CommonResult exceptionHandler(Exception e) {
        if (e instanceof CodeGenerateException) {
            return CommonResult.error().message(e.getMessage());
        } else if (e instanceof IllegalStateException) {
            return CommonResult.error().message("传参错误");
        } else {
            log.error("未知错误", e);
            return CommonResult.error().message("服务内部异常");
        }
    }
}
