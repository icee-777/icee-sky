package com.icee.aspect;


import com.icee.annotation.AutoFill;
import com.icee.constant.AutoFillConstant;
import com.icee.context.BaseContext;
import com.icee.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 配置自动填充的切点:mapper 包下的(任意返回类型)所有方法(0~n个参数) 且含有AutoFill注解
     */
    @Pointcut("execution(* com.icee.mapper.*.*(..)) && @annotation(com.icee.annotation.AutoFill)")
    public void autoFillPT(){}

    /**
     * 逻辑：在切点方法执行之前，进行数据填充
     *
     * @param joinPoint
     */
    @Before("autoFillPT()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行数据填充...");
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法上的AutoFill注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获取数据库操作类型
        OperationType operationType = autoFill.value();

        //获取当前方法参数的实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //准备注入的属性
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //填充创建时间、更新时间、创建人、更新人
        if(operationType == OperationType.INSERT){
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

                //通过反射为属性赋值
                setCreateUser.invoke(entity,currentId);
                setCreateTime.invoke(entity,now);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateUser.invoke(entity,currentId);
            setUpdateTime.invoke(entity,now);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
