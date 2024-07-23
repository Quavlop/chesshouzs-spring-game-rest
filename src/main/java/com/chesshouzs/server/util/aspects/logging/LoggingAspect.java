package com.chesshouzs.server.util.aspects.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.chesshouzs.server.config.app.AppConfig;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Before(AppConfig.LOG_SERVICE_PKG_PATH)
    public void logBefore(JoinPoint joinPoint) {
        // Log method entry for controllers
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String parameters = Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "));
        logger.info("Entering method: {}.{} with parameters: {}", className, methodName, parameters);
    }

    @After(AppConfig.LOG_SERVICE_PKG_PATH)
    public void logAfter(JoinPoint joinPoint) {
        // Log method exit for controllers
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        logger.info("Exiting method: {}.{}", className, methodName);
    }
}