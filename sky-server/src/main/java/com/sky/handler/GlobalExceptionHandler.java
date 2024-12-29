package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕捉数据库唯一约束异常
     * @param e
     * @return
     */
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)这个可以不加括号，因为下面已经有接受变量了
    @ExceptionHandler
    public Result handleSQLIntegrityConstraintViolationExceptionJ(SQLIntegrityConstraintViolationException e){

        // Duplicate entry 'lisi'for key 'employee.idx_username

        //1.使用异常对象获取异常的信息
        String message = e.getMessage();

        if(message.contains("Duplicate entry")){
            //2.截取重复的内容'list'
            String val = message.split(" ")[2];
            //3.返回错误信息
            return Result.error(val + MessageConstant.ALREADY_EXIST);
        }
        return  Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
