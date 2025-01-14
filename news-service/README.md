# Система управления новостями - News Management System

Тестовое задание для Clevertec

# Описание проекта

Микросервис news-service

Данный микросервис работает с новостями. Архитектурный стиль: REST. Вывод данных в формате JSON

Сервис связывается с микросервисом comments-service с использованием Open Feign

### Сущности:

Новость (News):

- id
- createdAt
- title
- text
- author

# Функциональные возможности

Программа реализует API для выполнения CRUD(создание, чтение, изменение, удаление) операций с новостями.

При получении списка новостей используется пагинация

Реализован полнотекстовый поиск

# Зависимости

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Data Redis
* Hibernate Validation
* PostgreSQL
* Spring Web
* Lombok
* MapStruct
* Gradle 8.5
* Hibernate ORM
* Lucene
* OpenFeign
* Open API
* Docker

При написании интеграционных тестов использовались зависимости Testcontainers и WireMock

Логирование и обработка исключений подключается через стартеры

### Реализовано:

- логирование запрос-ответ в аспектном стиле (сервисный слой и контроллеры)
- глобальную обработку исключений
- реализован кастомный кэш: LFU и LRU реализации с обработкой в аспекте AOP
- подключен liquibase: при запуске накатываются скрипты по генерации необходимых таблиц.
- Добавлены данные для демонстрации (20 новостей, по 10 комментариев на каждую новость).
- Реализовано с использованием changelog в формате xml
- Подключено кеширование Redis для профиля demo
- Подключен Open API (swagger)
- Прописан dockerFile для образа. Используется при поднятии в приложения.

# Реализована поддержка @Profile

Добавлены профили prod, dev, demo и test.

Профили prod, demo применяется для запуска и взаимодействия сервисов в docker.

Профиль dev используется для локального запуска

### Смена профиля

Для смены профиля необходимо:

В папке проекта в application.yml сменить активный профиль

```
profiles: 
    active: 
        dev

```

или

```
profiles: 
    active: 
        demo
```

При использовании профиля dev необходимо указать корректные данные для подключения к БД (username и password).

```
spring: 
    datasource: 
        username: имя_пользователя
        password: пароль
```

---

# API руководство

В этом справочном руководстве по API представлена подробная информация о доступных методах и параметрах API системы.

<details>

## Содержание

* Новости
* Ответ об ошибке

### Новости

Описание: Этот метод добавляет новую новость.
Endpoint: /news
HTTP Method: POST

Parameters:

| Name   | Type   | Description       |
|--------|--------|-------------------|
| title  | String | Заголовок новости |
| text   | String | Текст новости     |
| author | String | Автор новости     |

Пример запроса:
http://localhost:8081/news

{
"title": "This is title of news",
"text": "This is content of news."
author: "crazy"
}

---
Описание: Этот метод получает новость по id.
Endpoint: /news/{newsId}
HTTP Method: GET

Пример запроса:
http://localhost:8081/news/3fa85f64-5717-4562-b3fc-2c963f66afa6

---
Описание: Этот метод обновляет новость.
Endpoint: /news/{newsId}
HTTP Method: PUT

Parameters:

| Name   | Type   | Description       |
|--------|--------|-------------------|
| title  | String | Заголовок новости |
| text   | String | Текст новости     |
| author | String | Автор новости     |

Пример запроса:
http://localhost:8081/news/3fa85f64-5717-4562-b3fc-2c963f66afa6

{
"title": "This is title of news",
"text": "This is content of news."
author: "crazy"
}

---
Описание: Этот метод удаляет новость по id.
Endpoint: /news/{newsId}
HTTP Method: DELETE

Пример запроса:
http://localhost:8081/news/3fa85f64-5717-4562-b3fc-2c963f66afa6

---
Описание: Этот метод выводит все новости (с пагинацией).
Endpoint: /news
HTTP Method: GET

Примеры запроса:
http://localhost:8081/news
http://localhost:8081/news?page=1
http://localhost:8081/news?size=5
http://localhost:8081/news?page=1&size=5

---
Описание: Этот метод выполняет полнотекстовый поиск.
Endpoint: /news/search
HTTP Method: GET

Примеры запроса:
http://localhost:8081/news/search?text=content&fields=title&fields=text&limit=15

---
Описание: Этот метод получает новость со списком комментариев (с пагинацией).
Endpoint: /{newsId}/comments
HTTP Method: GET

Примеры запроса:
http://localhost:8081/3fa85f64-5717-4562-b3fc-2c963f66afa6/comments
http://localhost:8081/1/comments?page=1&size=5


---
Описание: Этот метод получает комментарий по идентификатору.
Endpoint: /{newsId}/comments/{commentId}
HTTP Method: GET

Примеры запроса:
http://localhost:8081/3fa85f64-5717-4562-b3fc-2c963f66afa6/comments/3fa85f64-5717-4562-b3fc-2c963f66afa6

### Ответ об ошибке

Пример ответа об ошибке

{
"statusCode": 404,
"status": BAD REQUEST,
"message": "No such news with id 234"
}

</details>