package com.uboxol.cloud.pandorasBox.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uboxol.cloud.cfg.Marks;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author liyunde
 * @since 2018/12/6 14:33
 */
@Slf4j
@Aspect
@Component
public class LogHandlerInterceptor {

    private final ObjectMapper mapper;

    @Autowired
    public LogHandlerInterceptor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Pointcut("execution(* com.uboxol.cloud.pandorasBox.api.*Controller.*(..))")
    void pointApi() {
        //Pointcut 必须是空方法
    }

    @Around("pointApi()")
    public Object doServiceWeb(ProceedingJoinPoint point) {

        try {
            return doService(point);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            return null;
        }
    }

    private Object doService(ProceedingJoinPoint point) throws Throwable {

        long t = System.currentTimeMillis();

        String rs = null;

        MDC.put("requestId", UUID.randomUUID().toString());

        try {

            Object ret = point.proceed();

            try {
                if (ret instanceof String) {
                    rs = (String) ret;
//                } else if (ret instanceof GeneratedMessageV3) {
//                    rs = new String(((GeneratedMessageV3) ret).toByteArray());
                } else {
                    rs = mapper.writeValueAsString(ret);
                }
            } catch (Exception e) {
                rs = e.getMessage();
                logger.error(Marks.ERR, "结果不能正确转化:{}", e.getMessage(), e);
            }

            return ret;
        } catch (Throwable e) {

            logger.error("{} {} {} {} {} {}",
                         point.getTarget().getClass().getSimpleName(),
                         point.getSignature().getName(),
                         System.currentTimeMillis() - t,
                         point.getArgs(),
                         e.getMessage(), e.toString(), e
            );

            throw e;

        } finally {

            if (null == rs) {
                logger.warn("{} {} {} {} null",
                            point.getTarget().getClass().getSimpleName(),
                            point.getSignature().getName(),
                            System.currentTimeMillis() - t,
                            point.getArgs()
                );
            } else {
                logger.info("{} {} {} {} {}",
                            point.getTarget().getClass().getSimpleName(),
                            point.getSignature().getName(),
                            System.currentTimeMillis() - t,
                            point.getArgs(),
                            rs);
            }

            MDC.clear();
        }
    }
}
