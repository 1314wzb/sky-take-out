package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//表示注解在运行时可通过反射访问
@Target(ElementType.METHOD)//表示注解只能应用于方法。
public @interface AutoFill {

    /**
     * 设置自动填充的类型，默认值是 添加类型
     * @return
     */
    OperationType value() default OperationType.INSERT;
}
