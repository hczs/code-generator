package icu.sunnyc.codegenerator.entity;

import icu.sunnyc.codegenerator.enums.ResultCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hc
 * @date ：Created in 2022/4/9 12:48
 * @modified ：
 */
@Data
public class CommonResult {

    private Boolean success;

    private Integer code;

    private String message;

    private Object data;

    private CommonResult() { }

    public static CommonResult ok() {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        commonResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        commonResult.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return commonResult;
    }

    public static CommonResult error() {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        commonResult.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        commonResult.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return commonResult;
    }

    public static CommonResult setResult(ResultCodeEnum resultCodeEnum) {
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(resultCodeEnum.getSuccess());
        commonResult.setCode(resultCodeEnum.getCode());
        commonResult.setMessage(resultCodeEnum.getMessage());
        return commonResult;
    }

    public CommonResult data(Object value) {
        this.data = value;
        return this;
    }

    public CommonResult message(String message) {
        this.setMessage(message);
        return this;
    }

    public CommonResult code(Integer code) {
        this.setCode(code);
        return this;
    }

    public CommonResult success(Boolean success) {
        this.setSuccess(success);
        return this;
    }
}
