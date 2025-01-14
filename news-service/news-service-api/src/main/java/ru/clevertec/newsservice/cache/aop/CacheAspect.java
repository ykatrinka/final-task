package ru.clevertec.newsservice.cache.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.cache.CustomCache;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Profile("dev")
@Component
@Aspect
@RequiredArgsConstructor
public class CacheAspect {

    public static final String NEWS_ADD_TO_CACHE = "News with id {} was added to cache";
    public static final String NEWS_REMOVE_FROM_CACHE = "News with id {} was removed from cache";
    public static final String NEWS_GET_FROM_CACHE = "News with id {} was got from cache";

    private final CustomCache<UUID, NewsEntity> cache;

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
        NewsEntity entity = (NewsEntity) joinPoint.getArgs()[0];
        NewsEntity savedEntity = (NewsEntity) joinPoint.proceed();

        if (savedEntity != null && savedEntity.getId() != null) {
            cache.put(entity.getId(), entity);
            log.info(NEWS_ADD_TO_CACHE, savedEntity.getId());
        }
        return savedEntity;
    }

    @Around("deleteMethods()")
    public Object aroundDeleteMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID id = ((NewsEntity) joinPoint.getArgs()[0]).getId();
        joinPoint.proceed();
        cache.delete(id);
        log.info(NEWS_REMOVE_FROM_CACHE, id);
        return id;
    }


    @Around("findByIdMethods()")
    public Object aroundFindByIdMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Optional<NewsEntity> foundEntity = cache.get(uuid);
        foundEntity.ifPresent(entity -> log.info(NEWS_GET_FROM_CACHE, uuid));

        if (foundEntity.isEmpty()) {
            foundEntity = (Optional<NewsEntity>) joinPoint.proceed();
            foundEntity.ifPresent(entity -> cache.put(uuid, entity));
            log.info(NEWS_ADD_TO_CACHE, uuid);
        }

        return foundEntity;
    }
}
