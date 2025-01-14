package ru.clevertec.newsservice.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CacheMode;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
public class BuildLuceneIndexOnStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final String INIT_INDEXES = "Start indexing ...";
    public static final String FAILED_LOAD_INDEX = "Failed indexed";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info(INIT_INDEXES);
        MassIndexer massIndexer = Search.session(entityManager).massIndexer();

        massIndexer.idFetchSize(100)
                .cacheMode(CacheMode.IGNORE)
                .batchSizeToLoadObjects(25)
                .threadsToLoadObjects(4);

        try {
            massIndexer.startAndWait();
        } catch (InterruptedException e) {
            log.warn(FAILED_LOAD_INDEX);
            Thread.currentThread().interrupt();
        }

    }
}