package ru.clevertec.newsservice.adapter.input.web.news;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.exceptionstarter.handler.GlobalExceptionHandling;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsCreateDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsPageableDto;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.newsservice.adapter.input.web.news.dto.NewsUpdateDto;
import ru.clevertec.newsservice.port.input.news.CreateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.DeleteNewsUseCase;
import ru.clevertec.newsservice.port.input.news.ReadNewsUseCase;
import ru.clevertec.newsservice.port.input.news.UpdateNewsUseCase;
import ru.clevertec.newsservice.port.input.news.command.NewsCreateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsPageUseCaseResult;
import ru.clevertec.newsservice.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.newsservice.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.newsservice.util.Constant;
import ru.clevertec.newsservice.util.JsonUtil;
import ru.clevertec.newsservice.util.TestDataNews;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @MockitoBean
    private CreateNewsUseCase createNewsUseCase;
    @MockitoBean
    private ReadNewsUseCase readNewsUseCase;
    @MockitoBean
    private UpdateNewsUseCase updateNewsUseCase;
    @MockitoBean
    private DeleteNewsUseCase deleteNewsUseCase;

    @MockitoBean
    private NewsWebMapper newsMapper;

    @MockitoBean
    private GlobalExceptionHandling globalExceptionHandling;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateNewsAndReturnStatus201() throws Exception {
        //given
        NewsCreateDto newsCreateDto = TestDataNews.getNewsCreateDto();
        NewsCreateCommand newsCommand = TestDataNews.getNewsCreateCommand();
        NewsUseCaseResult newsUseCaseResult = TestDataNews.getNewsUseCaseResultForCreate();
        NewsResponse expectedNews = TestDataNews.getNewsResponseForCreate();

        byte[] bodyRequest = JsonUtil.getRequestBodyFromFile("create_news.json");

        when(newsMapper.dtoToCommand(newsCreateDto)).thenReturn(newsCommand);
        when(createNewsUseCase.createNews(newsCommand)).thenReturn(newsUseCaseResult);
        when(newsMapper.useCaseToDto(newsUseCaseResult)).thenReturn(expectedNews);


        //when
        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(expectedNews.title()))
                .andExpect(jsonPath("$.text").value(expectedNews.text()))
                .andExpect(jsonPath("$.author").value(expectedNews.author()));


        //then
        verify(newsMapper, times(1)).dtoToCommand(newsCreateDto);
        verify(createNewsUseCase, times(1)).createNews(newsCommand);
        verify(newsMapper, times(1)).useCaseToDto(newsUseCaseResult);
    }

    @Nested
    class ReadNews {

        @Test
        void shouldReadNewsAndReturnStatus200() throws Exception {
            //given
            UUID newsId = Constant.NEWS_UUID;
            NewsUseCaseResult newsUseCaseResult = TestDataNews.getNewsUseCaseResultForRead();
            NewsResponse expectedNews = TestDataNews.getNewsResponseForRead();

            when(readNewsUseCase.readNews(newsId)).thenReturn(newsUseCaseResult);
            when(newsMapper.useCaseToDto(newsUseCaseResult)).thenReturn(expectedNews);

            //when
            mockMvc.perform(get("/news/{newsId}", newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.title").value(expectedNews.title()))
                    .andExpect(jsonPath("$.text").value(expectedNews.text()))
                    .andExpect(jsonPath("$.author").value(expectedNews.author()));

            //then
            verify(readNewsUseCase, times(1)).readNews(newsId);
            verify(newsMapper, times(1)).useCaseToDto(newsUseCaseResult);
        }

        @Test
        void shouldNotReadNews_whenNewsNotFound() throws Exception {
            //given
            UUID newsId = Constant.NEWS_UUID;
            when(readNewsUseCase.readNews(newsId)).thenThrow(EntityNotFoundException.class);

            //when
            mockMvc.perform(get("/news/{newsId}", newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(result ->
                            assertInstanceOf(EntityNotFoundException.class,
                                    result.getResolvedException())
                    );

            //then
            verify(readNewsUseCase, times(1)).readNews(newsId);
        }

    }

    @Nested
    class UpdateNews {

        @Test
        void shouldUpdateNewsAndReturnStatus200() throws Exception {
            //given
            UUID newsId = Constant.NEWS_UUID;
            NewsUpdateDto newsUpdateDto = TestDataNews.getNewsUpdateDto();
            NewsUpdateCommand newsCommand = TestDataNews.getNewsUpdateCommand();
            NewsUseCaseResult newsUseCaseResult = TestDataNews.getNewsUseCaseResultForUpdate();
            NewsResponse expectedNews = TestDataNews.getNewsResponseForUpdate();

            byte[] bodyRequest = JsonUtil.getRequestBodyFromFile("update_news.json");

            when(newsMapper.dtoToCommand(newsUpdateDto)).thenReturn(newsCommand);
            when(updateNewsUseCase.updateNews(newsId, newsCommand)).thenReturn(newsUseCaseResult);
            when(newsMapper.useCaseToDto(newsUseCaseResult)).thenReturn(expectedNews);

            //when
            mockMvc.perform(put("/news/{newsId}", newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bodyRequest)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.title").value(expectedNews.title()))
                    .andExpect(jsonPath("$.text").value(expectedNews.text()))
                    .andExpect(jsonPath("$.author").value(expectedNews.author()));

            //then
            verify(newsMapper, times(1)).dtoToCommand(newsUpdateDto);
            verify(updateNewsUseCase, times(1)).updateNews(newsId, newsCommand);
            verify(newsMapper, times(1)).useCaseToDto(newsUseCaseResult);
        }


        @Test
        void shouldNotUpdateNews_whenNewsNotFound() throws Exception {
            //given
            UUID newsId = Constant.NEWS_UUID;
            NewsUpdateDto newsUpdateDto = TestDataNews.getNewsUpdateDto();
            NewsUpdateCommand newsCommand = TestDataNews.getNewsUpdateCommand();

            byte[] bodyRequest = JsonUtil.getRequestBodyFromFile("update_news.json");

            when(newsMapper.dtoToCommand(newsUpdateDto)).thenReturn(newsCommand);
            when(updateNewsUseCase.updateNews(newsId, newsCommand)).thenThrow(EntityNotFoundException.class);

            //when
            mockMvc.perform(put("/news/{newsId}", newsId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bodyRequest)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(result ->
                            assertInstanceOf(EntityNotFoundException.class,
                                    result.getResolvedException())
                    );

            //then
            verify(newsMapper, times(1)).dtoToCommand(newsUpdateDto);
            verify(updateNewsUseCase, times(1)).updateNews(newsId, newsCommand);
        }
    }

    @Test
    void shouldDeleteNews() {
        //given
        UUID newsId = Constant.NEWS_UUID;

        //when
        deleteNewsUseCase.deleteNews(newsId);

        //then
        verify(deleteNewsUseCase, times(1)).deleteNews(newsId);
    }

    @Test
    void shouldReadNewsPageAndReturnStatus200() throws Exception {
        //given
        NewsPageUseCaseResult newsUseCaseResult = TestDataNews.getNewsPageUseCaseResult();
        NewsPageableDto expectedNewsPage = TestDataNews.getNewsPageableDto();

        when(readNewsUseCase.readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE))
                .thenReturn(newsUseCaseResult);
        when(newsMapper.useCaseToDto(newsUseCaseResult)).thenReturn(expectedNewsPage);


        //when
        mockMvc.perform(get("/news")
                        .param("page", String.valueOf(Constant.PAGE_NUMBER))
                        .param("size", String.valueOf(Constant.PAGE_SIZE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(expectedNewsPage.content().size()))
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].title").value(expectedNewsPage.content().getFirst().title()))
                .andExpect(jsonPath("$.content[0].text").value(expectedNewsPage.content().getFirst().text()))
                .andExpect(jsonPath("$.content[0].author").value(expectedNewsPage.content().getFirst().author()));

        //then
        verify(readNewsUseCase, times(1))
                .readNewsPage(Constant.PAGE_NUMBER, Constant.PAGE_SIZE);
        verify(newsMapper, times(1)).useCaseToDto(newsUseCaseResult);
    }


    @Nested
    class ExistsNews {

        @Test
        void shouldReturnTrue_ifNewsIsExists() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID;
            when(readNewsUseCase.isNewsExists(newsId)).thenReturn(true);

            // when
            mockMvc.perform(get("/news/{newsId}/exists", newsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(Boolean.TRUE));

            // then
            verify(readNewsUseCase, times(1)).isNewsExists(newsId);
        }

        @Test
        void shouldReturnFalse_ifNewsIsNotExists() throws Exception {
            // given
            UUID newsId = Constant.NEWS_UUID_FAIL;
            when(readNewsUseCase.isNewsExists(newsId)).thenReturn(false);

            // when
            mockMvc.perform(get("/news/{newsId}/exists", newsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(Boolean.FALSE));

            // then
            verify(readNewsUseCase, times(1)).isNewsExists(newsId);
        }
    }

}