package ru.clevertec.newsservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.newsservice.port.output.WriteNewsPort;
import ru.clevertec.newsservice.util.Constant;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteNewsServiceTest {

    @Mock
    private WriteNewsPort newsAdapter;

    @InjectMocks
    private DeleteNewsService deleteNewsService;

    @Test
    void shouldDeleteNews() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        //when

        //then
        deleteNewsService.deleteNews(newsId);

        verify(newsAdapter, times(1)).deleteNews(newsId);
    }


}