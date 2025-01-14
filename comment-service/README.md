# Система управления новостями - News Management System

Тестовое задание для Clevertec

# Описание проекта

Микросервис comments-service

Данный микросервис работает с комментариями. Архитектурный стиль: REST. Вывод данных в формате JSON

Сервис связывается с микросервисом news-service с использованием Open Feign

### Сущности:

Комментарии (Comment):

- id
- createdAt
- text
- author
- news_id.

# Функциональные возможности

Программа реализует API для выполнения CRUD(создание, чтение, изменение, удаление) операций для комментариев.

При получении списка комментариев используется пагинация

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
* OpenApi
* Docker

При написании интеграционных тестов использовались зависимости Testcontainers и WireMock

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

Профили demo и prod применяется для запуска и взаимодействия сервисов в docker.

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
        prod
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

* Комментарии
* Ответ об ошибке

### Комментарии

Описание: Этот метод добавляет новый комментарий.
Endpoint: /comments
HTTP Method: POST

Parameters:

| Name   | Type   | Description           |
|--------|--------|-----------------------|
| newsId | Long   | Идентификатор новости |
| author | String | Имя пользователя      |
| text   | String | Текст комментария     |

Пример запроса:
http://localhost:8082/comments

{
"newsId": 3fa85f64-5717-4562-b3fc-2c963f66afa6,
"author": "Patrik",
"text": "This is new comment."
}

---
Описание: Этот метод получает комментарий по id.
Endpoint: /comments/{commentId}
HTTP Method: GET

Пример запроса:
http://localhost:8082/comments/3fa85f64-5717-4562-b3fc-2c963f66afa6

---
Описание: Этот метод обновляет комментарий.
Endpoint: /comments/{commentId}
HTTP Method: PUT

Parameters:

| Name   | Type   | Description           |
|--------|--------|-----------------------|
| newsId | Long   | Идентификатор новости |
| author | String | Имя пользователя      |
| text   | String | Текст комментария     |

Пример запроса:
http://localhost:8082/comments/3fa85f64-5717-4562-b3fc-2c963f66afa6

{
"newsId": 3fa85f64-5717-4562-b3fc-2c963f66afa6,
"username": "Patrik",
"text": "This is updated comment."
}

---
Описание: Этот метод удаляет комментарий по id.
Endpoint: /comments/{commentsId}
HTTP Method: DELETE

Пример запроса:
http://localhost:8082/comments/3fa85f64-5717-4562-b3fc-2c963f66afa6

---
Описание: Этот метод выводит все комментарии (с пагинацией).
Endpoint: /comments
HTTP Method: GET

Примеры запроса:
http://localhost:8082/comments
http://localhost:8082/comments?pageNumber=1

---
Описание: Этот метод выполняет полнотекстовый поиск.
Endpoint: /comments/search
HTTP Method: GET

Примеры запроса:
http://localhost:8082/comments/search?text=content&fields=author&fields=text&limit=15

### Ответ об ошибке

Пример ответа об ошибке

{
"statusCode": 404,
"status": BAD REQUEST,
"message": "No such news with id 234"
}

</details>