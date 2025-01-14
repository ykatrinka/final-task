package ru.clevertec.commentservice.adapter.output.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.commentservice.adapter.output.persistence.feignclient.NewsClient;
import ru.clevertec.commentservice.util.Constant;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsClientAdapterTest {

    @Mock
    private NewsClient newsClient;

    @InjectMocks
    private NewsClientAdapter newsClientAdapter;

    @Test
    void shouldReturnTrue_ifNewsIsExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        when(newsClient.existsNews(newsId)).thenReturn(new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK));

        //when
        Boolean actualValue = newsClientAdapter.existsNews(newsId);

        //then
        assertTrue(actualValue);


        verify(newsClient, times(1)).existsNews(newsId);
    }

    @Test
    void shouldReturnFalse_ifNewsIsNotExists() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        when(newsClient.existsNews(newsId)).thenReturn(new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK));

        //when
        Boolean actualValue = newsClientAdapter.existsNews(newsId);

        //then
        assertFalse(actualValue);


        verify(newsClient, times(1)).existsNews(newsId);
    }
}