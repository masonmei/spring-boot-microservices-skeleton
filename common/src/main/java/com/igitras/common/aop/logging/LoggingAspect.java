package com.igitras.common.aop.logging;

import com.igitras.common.utils.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * @author mason
 */
@Aspect
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired
    private Environment env;

    @Pointcut("@annotation(com.igitras.common.aop.logging.Logging)")
    public void annotated() {
    }

    @Pointcut(
            "pattern(.*.mvc.rest..*) || within(.*.mvc.controller..*)|| within(.*.mvc.resource..*)")
    public void loggingMvcPointcut() {
    }

    @Pointcut("within(.*.service..*)")
    public void loggingServicePointCut() {
    }

    @Pointcut("within(.*.domain.repository..*) || within(.*.domain.search..*)")
    public void loggingDomainPointcut() {
    }

    @AfterThrowing(
            pointcut = "annotated() || loggingMvcPointcut() || loggingDomainPointcut() || loggingServicePointCut()",
            throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Constants.Profile.DEV)) {
            LOG.error("Exception in {}.{}() with cause = {} and exception {}", joinPoint.getSignature()
                    .getDeclaringTypeName(), joinPoint.getSignature()
                    .getName(), e.getCause(), e);
        } else {
            LOG.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature()
                    .getDeclaringTypeName(), joinPoint.getSignature()
                    .getName(), e.getCause());
        }
    }

    @Around("annotated() || loggingMvcPointcut() || loggingDomainPointcut() || loggingDomainPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature()
                    .getDeclaringTypeName(), joinPoint.getSignature()
                    .getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature()
                        .getDeclaringTypeName(), joinPoint.getSignature()
                        .getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            LOG.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature()
                    .getDeclaringTypeName(), joinPoint.getSignature()
                    .getName());

            throw e;
        }
    }

}
