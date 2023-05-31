package com.besscroft.diyfile.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.besscroft.diyfile.common.result.CommonResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.concurrent.CompletionException;

/**
 * @Description 全局异常处理
 * @Author Bess Croft
 * @Date 2022/12/15 14:43
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 SpringMVC 请求参数缺失
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数，结果并未传递 xx 参数
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("SpringMVC 请求参数缺失.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(),
                String.format("请求参数缺失:%s", ex.getParameterName()), null);
    }

    /**
     * 处理 SpringMVC 请求参数类型错误
     *
     * 例如说，接口上设置了 @RequestParam("xx") 参数为 Integer，结果传递 xx 参数类型为 String
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.warn("SpringMVC 请求参数类型错误.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(),
                String.format("请求参数类型错误:%s", ex.getMessage()), null);
    }

    /**
     * SpringMVC 参数校验不正确
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> methodArgumentNotValidExceptionExceptionHandler(MethodArgumentNotValidException ex) {
        log.warn("SpringMVC 参数校验异常.[异常原因={}]", ex.getMessage(), ex);
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null; // 断言，避免告警
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(),
                String.format("请求参数不正确:%s", fieldError.getDefaultMessage()), null);
    }

    /**
     * SpringMVC 参数绑定不正确，本质上也是通过 Validator 校验
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindExceptionHandler(BindException ex) {
        log.warn("SpringMVC 参数绑定异常.[异常原因={}]", ex.getMessage(), ex);
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(),
                String.format("请求参数不正确:%s", fieldError.getDefaultMessage()), null);
    }

    /**
     * Validator 请求参数校验异常
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.warn("Validator 请求参数校验异常.[异常原因={}]", ex.getMessage(), ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(),
                String.format("请求参数不正确:%s", constraintViolation.getMessage()), null);
    }

    /**
     * 参数校验 ValidationException 异常
     */
    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public CommonResult<?> validationException(ValidationException ex) {
        log.warn("参数校验 ValidationException 异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
    }

    /**
     * SpringMVC 请求方法异常
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("SpringMVC 请求方法异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.FORBIDDEN.value(), ex.getMessage(), null);
    }

    /**
     * Spring Security 权限异常
     */
    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<?> accessDeniedExceptionHandler(AccessDeniedException ex) {
        log.warn("权限异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.FORBIDDEN.value(), ex.getMessage(), null);
    }

    /**
     * 自定义异常 DiyFileException
     */
    @ResponseBody
    @ExceptionHandler(value = DiyFileException.class)
    public CommonResult<?> diyFileExceptionHandler(DiyFileException ex) {
        log.info("自定义异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(ex.getCode(), ex.getMessage(), null);
    }

    /**
     * Json 转换异常 handleJsonProcessingException
     */
    @ResponseBody
    @ExceptionHandler(JsonProcessingException.class)
    public CommonResult<?> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("Json 转换异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    }

    /**
     * 微服务调用异常 processException
     */
    @ResponseBody
    @ExceptionHandler(CompletionException.class)
    public CommonResult<?> processException(CompletionException ex) {
        if (ex.getMessage().startsWith("feign.FeignException")) {
            log.error("微服务调用异常.[异常原因={}]", ex.getMessage(), ex);
            return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), "微服务调用异常！", null);
        }
        return handleException(ex);
    }

    /**
     * 参数异常 IllegalArgumentException
     */
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResult<?> handleException(IllegalArgumentException ex) {
        log.error("参数异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
    }

    /**
     * 登录失效/token失效 NotLoginException
     */
    @ResponseBody
    @ExceptionHandler(NotLoginException.class)
    public CommonResult<?> handleException(NotLoginException ex) {
        log.error("参数异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.UNAUTHORIZED.value(), "很抱歉，您未登录或登录已过期，请重新登录！", null);
    }

    /**
     * 角色不匹配异常 NotRoleException
     */
    @ResponseBody
    @ExceptionHandler(NotRoleException.class)
    public CommonResult<?> handleException(NotRoleException ex) {
        log.error("参数异常.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.FORBIDDEN.value(), "您当前用户的角色暂无权限！", null);
    }

    /**
     * 全局异常拦截 handleException
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception ex) {
        log.error("全局异常信息.[异常原因={}]", ex.getMessage(), ex);
        return CommonResult.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常，请联系管理员！", null);
    }

}
