package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
public class AutoFillAspect {

    /**
     * 对所有打上这个@AutoFill的注解作前置增强，目的是为了增强这些方法的参数而已！
     */
    @Before("@annotation(com.sky.annotation.AutoFill)")
    public void autoFill(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {

        //0. 获取方法的参数:: 添加或者更新的方法参数只有一个。所以使用[0]来获取它
        Object arg = joinPoint.getArgs()[0];

        //1. 获取方法的签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        //2. 根据方法签名获取方法Method对象
        Method method = signature.getMethod();

        //3. 根据方法Method对象，获取方法身上的注解@AutoFill
        AutoFill annotation = method.getAnnotation(AutoFill.class);

        //4. 根据注解的对象，获取它里面的Value属性值
        OperationType value = annotation.value();

        //5. 判定是添加还是更新，以便区分到底是填充4个值还是2个值?
        if(value == OperationType.INSERT){
            //走的是添加操作  4个属性
            System.out.println("来自动填充添加的属性了... "+ method);

            //5.1 获取到了参数的字节码对象
            Class clazz = arg.getClass();

            //5.2 可以获取属性，获取那4个set方法!
            Field f1 = clazz.getDeclaredField("createTime");
            Field f2 = clazz.getDeclaredField("updateTime");
            Field f3 = clazz.getDeclaredField("createUser");
            Field f4 = clazz.getDeclaredField("updateUser");

            //5.3 暴力反射
            f1.setAccessible(true);
            f2.setAccessible(true);
            f3.setAccessible(true);
            f4.setAccessible(true);

            //5.4 给属性赋值
            f1.set(arg, LocalDateTime.now());
            f2.set(arg, LocalDateTime.now());
            f3.set(arg, BaseContext.getCurrentId());
            f4.set(arg, BaseContext.getCurrentId());
        }else{

            //走的是添加操作  2个属性
            System.out.println("来自动填充更新的属性了... "+ method);

            //5.1 获取到了参数的字节码对象
            Class clazz = arg.getClass();

            //5.2 可以获取属性，获取那4个set方法!
            Field f1 = clazz.getDeclaredField("updateTime");
            Field f2 = clazz.getDeclaredField("updateUser");

            //5.3 暴力反射
            f1.setAccessible(true);
            f2.setAccessible(true);

            //5.4 给属性赋值
            f1.set(arg, LocalDateTime.now());
            f2.set(arg, BaseContext.getCurrentId());
        }

        //6. 通过反射来取得对应的4个属性 或者 2个属性 对象

        //7. 通过反射直接给他们赋值即可
    }
}
