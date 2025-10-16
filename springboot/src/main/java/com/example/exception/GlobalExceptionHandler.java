package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(Exception.class)
    @ResponseBody //return json content
    public Result error(Exception e) {
        //output exception detail
        log.error("error info",e);
        return Result.error();
    }
    @ExceptionHandler(CustomException.class)
    @ResponseBody //return json content
    public Result error(CustomException e) {
//        log.error("error info",e);
        return Result.error(e.getCode(),e.getMsg());
    }
}

