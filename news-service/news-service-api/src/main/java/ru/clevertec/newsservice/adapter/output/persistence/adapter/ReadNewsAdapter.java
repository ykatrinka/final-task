package ru.clevertec.newsservice.adapter.output.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.newsservice.adapter.input.web.news.NewsWebMapper;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.entity.NewsEntity;
import ru.clevertec.newsservice.adapter.output.persistence.jpa.repository.NewsRepository;
import ru.clevertec.newsservice.domain.News;
import ru.clevertec.newsservice.domain.NewsPageable;
import ru.clevertec.newsservice.port.output.ReadNewsPort;
import ru.clevertec.newsservice.utils.ReflectionUtil;

import java.util.List;
import java.util.UUID;

@Transactional
@Component
@RequiredArgsConstructor
public class ReadNewsAdapter implements ReadNewsPort {

    public static final String NEWS_NOT_FOUND = "News with id %s not found";

    private final NewsRepository newsRepository;
    private final NewsWebMapper newsMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Cacheable(value = "news", key = "#newsId.toString()")
    @Override
    public News readNews(UUID newsId) {
        return newsRepository.findById(newsId)
                .map(newsMapper::entityToDomain)
                .orElseThrow(() -> EntityNotFoundException.getInstance(
                        String.format(NEWS_NOT_FOUND, newsId))
                );
    }

    @Override
    public NewsPageable readNewsPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<NewsEntity> newsPage = newsRepository.findAll(pageable);
        return newsMapper.pageToDomain(newsPage);
    }

    @Override
    public boolean isNewsExists(UUID newsId) {
        return newsRepository.existsById(newsId);
    }

    @Override
    public List<News> searchNews(String text, List<String> fields, int limit) {
        List<String> searchableFields = ReflectionUtil.getFieldsByAnnotation(NewsEntity.class, FullTextField.class);
        List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !searchableFields.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        SearchSession searchSession = Search.session(entityManager);

        List<NewsEntity> news = searchSession
                .search(NewsEntity.class)
                .where(f -> f.match()
                        .fields(fields.toArray(new String[0]))
                        .matching(text)
                        .fuzzy(2))
                .fetchHits(limit);

        return news.stream()
                .map(newsMapper::entityToDomain)
                .toList();
    }

}
