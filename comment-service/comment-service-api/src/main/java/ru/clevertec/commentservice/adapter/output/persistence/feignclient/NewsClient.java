package ru.clevertec.commentservice.adapter.output.persistence.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "${feign.client.news-service.name}",
        url = "${feign.client.news-service.url}"
)
public interface NewsClient {

    @GetMapping(path = "news/{newsId}/exists")
    ResponseEntity<Boolean> existsNews(@PathVariable("newsId") UUID newsId);

}
