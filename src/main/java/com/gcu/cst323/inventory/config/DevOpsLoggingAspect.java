package com.gcu.cst323.inventory.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Milestone 4 DevOps logging aspect.
 * Logs entry, exit, and errors for controller, service, and repository methods.
 */
@Aspect
@Component
public class DevOpsLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(DevOpsLoggingAspect.class);

    @Around(
        "execution(* com.gcu.cst323.inventory.controller..*(..)) || " +
        "execution(* com.gcu.cst323.inventory.service..*(..)) || " +
        "execution(* com.gcu.cst323.inventory.repository..*(..))"
    )
    public Object logEntryAndExit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        // Do not log arguments because some methods include passwords.
        logger.info("ENTER {}.{}()", className, methodName);

        try {
            Object result = joinPoint.proceed();
            logger.info("EXIT {}.{}()", className, methodName);
            return result;
        } catch (Throwable exception) {
            logger.error("ERROR in {}.{}(): {}", className, methodName, exception.getMessage(), exception);
            throw exception;
        }
    }
}