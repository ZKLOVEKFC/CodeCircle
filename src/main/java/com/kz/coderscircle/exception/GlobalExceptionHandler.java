package com.kz.coderscircle.exception;

import com.kz.coderscircle.pojo.Result;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolation;
import java.util.stream.Collectors;
/**
 * 全局异常处理器
 * 使用 @RestControllerAdvice 注解，可以捕获所有 @RestController 抛出的异常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 @RequestBody参数校验失败时抛出的 MethodArgumentNotValidException 异常。
     e 异常对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationException(MethodArgumentNotValidException e) {
        // 从异常对象中获取所有字段的校验失败信息，并拼接成一个字符串
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()) // 只获取我们在注解上定义的message
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("参数校验失败");

        log.warn("请求体参数校验失败: {}", errorMessage);
        return Result.error(errorMessage);
    }

    /**
     * 处理 @RequestParam 和 @PathVariable 参数校验失败时抛出的 ConstraintViolationException 异常。
     e 异常对象
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage) // 只获取注解上定义的message
                .collect(Collectors.joining("; "));   // 如果有多个错误，用分号隔开

        log.warn("请求参数或路径变量校验失败: {}", errorMessage);
        return Result.error(errorMessage);
    }

    /**
     * 捕获所有其他的未知异常，作为最后的安全网。
     e 异常对象
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 将完整的错误堆栈打印到日志中，便于后端排查问题
        log.error("发生了未知的系统异常", e);

        // 只向前台返回一个通用的、模糊的错误信息
        return Result.error("系统繁忙，请稍后再试");
    }
}