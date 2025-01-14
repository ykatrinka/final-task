package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.commentservice.adapter.output.persistence.feignclient.NewsClient;
import ru.clevertec.commentservice.port.output.NewsClientPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NewsClientAdapter implements NewsClientPort {

    private final NewsClient newsClient;

    @Override
    public Boolean existsNews(UUID newsId) {
        return newsClient.existsNews(newsId).getBody();
    }

}
