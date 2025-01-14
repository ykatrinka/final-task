package ru.clevertec.loggingstarter.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

/**
 * @author Katerina
 * @version 1.0.0
 * Аспект для логирования.
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class LoggingAspect {
    public static final String LOG_MESSAGE_BEFORE = "Received call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_RETURNING = "Returning call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_THROWING = "Throw exception :: method {} message {}";
    public static final String EMPTY_STRING = "";

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void anyServices() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void anyControllers() {
    }

    @Before("anyControllers()")
    public void beforeControllerMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LOG_MESSAGE_BEFORE,
                method.getName(),
                args);
    }

    @AfterReturning(pointcut = "anyControllers()", returning = "result")
    public void afterControllerMethods(JoinPoint joinPoint, Object result) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LOG_MESSAGE_AFTER_RETURNING,
                method.getName(),
                ofNullable(result)
                        .orElse(EMPTY_STRING));
    }

    @AfterThrowing(pointcut = "anyServices()", throwing = "exception")
    private void afterThrowingAnyServicesMethodsLoggingAdvice(JoinPoint joinPoint, Throwable exception) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        log.debug(LOG_MESSAGE_AFTER_THROWING,
                method.getName(),
                exception.getMessage());
    }
}