package ru.clevertec.commentservice.cache.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.adapter.output.persistence.jpa.entity.CommentEntity;
import ru.clevertec.commentservice.cache.CustomCache;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Profile("dev")
@Component
@Aspect
@RequiredArgsConstructor
public class CacheAspect {

    public static final String COMMENT_ADD_TO_CACHE = "Comment with id {} was added to cache";
    public static final String COMMENT_REMOVE_FROM_CACHE = "Comment with id {} was removed from cache";
    public static final String COMMENT_GET_FROM_CACHE = "Comment with id {} was got from cache";

    private final CustomCache<UUID, CommentEntity> cache;

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.save(..))")
    public void saveMethods() {
    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.delete(..))")
    public void deleteMethods() {
    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.findById(..))")
    public void findByIdMethods() {
    }

    @Around("saveMethods()")
    public Object aroundSaveMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        CommentEntity entity = (CommentEntity) joinPoint.getArgs()[0];
        CommentEntity savedEntity = (CommentEntity) joinPoint.proceed();

        if (savedEntity != null && savedEntity.getId() != null) {
            cache.put(entity.getId(), entity);
            log.info(COMMENT_ADD_TO_CACHE, savedEntity.getId());
        }
        return savedEntity;
    }

    @Around("deleteMethods()")
    public Object aroundDeleteMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID id = ((CommentEntity) joinPoint.getArgs()[0]).getId();
        joinPoint.proceed();
        cache.delete(id);
        log.info(COMMENT_REMOVE_FROM_CACHE, id);
        return id;
    }


    @Around("findByIdMethods()")
    public Object aroundFindByIdMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Optional<CommentEntity> foundEntity = cache.get(uuid);
        foundEntity.ifPresent(entity -> log.info(COMMENT_GET_FROM_CACHE, uuid));

        if (foundEntity.isEmpty()) {
            foundEntity = (Optional<CommentEntity>) joinPoint.proceed();
            foundEntity.ifPresent(entity -> cache.put(uuid, entity));
            log.info(COMMENT_ADD_TO_CACHE, uuid);
        }

        return foundEntity;
    }
}
