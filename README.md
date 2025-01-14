# Система управления новостями - News Management System

# Описание проекта

Программа реализует API для выполнения CRUD(создание, прочтение, изменение, удаление).

Добавлена возможность полнотекстового поиска.

Используется LFU/LRU кеширование для профиля dev. Кеширование Redis для профиля demo.

- Архитектурный стиль: REST.
- Вывод данных в формте JSON
- PostgreSQL
- Docker Compose для контейнеризации проекта.
- Liquibase для миграции базы данных.
- JWT token Spring Security

Подключен Swagger (OpenAPI 3.0)

Реализовано два стартера:

- spring-boot-aop-logging-starter
- spring-boot-exception-handling-starter

Реализовано три микросервиса:

- news-service
- comments-service
- auth-service

Микросервисы news-service и comments-service взаимодействуют между собой во время работы (Spring Cloud OpenFeign).

Микросервис auth-service реализует авторизацию пользователей с учетом ролей.

Добавлены профили prod, dev, demo и test.

- Профиль prod и demo применяется для запуска и взаимодействия сервисов в docker.
- Профиль dev используется для локального запуска

News-service реализует RESTFul Api для работы с новостями

[Подробнее о news-service](news-service/README.md)

Comments-service реализует RESTFul Api для работы с комментариями

[Подробнее о comments-service](comments-service/README.md)

Comments-service реализует RESTFul Api для работы с пользователями

[Подробнее о auth-servcice](auth-service/README.md)

## Инструкция по запуску приложения

1. Клонировать репозиторий из Git на свой локальный компьютер:  
   `git clone https://gitlab.com/ykv.work.j/test-task.git`

2. Установите корректные данные для подключения к базе данных в файле application.yaml (src/main/resources)
   Структура таблиц базы данных и демо данные загружаются автоматически


## Инструкция по запуску приложения с использованием Docker

Перед запуском приложения необходимо убедиться, что на вашем ПК установлено и запущенно приложение Docker.

1. Клонировать репозиторий из Git на свой локальный компьютер:  
   `git clone https://gitlab.com/ykv.work.j/test-task.git`

2. Перейти в корневую директорию проекта и запустить build:

   `cd spring-boot-aop-logging-starter`
   `gradle clean -x test build`
   `cd ..`

   `cd spring-boot-exception-handling-starter`
   `gradle clean -x test build`
   `cd ..`

   `cd auth-service`
   `gradle clean -x test build`
   `cd ..`

   `cd news-service`
   `gradle clean -x test build`
   `cd ..`

   `cd comment-service`
   `gradle clean -x test build`
   `cd ..`

3. Перейти в корневую директорию проекта и запустить docker-compose файл:

   `docker compose up -d`

После выполнения данных шагов приложение будет успешно запущенно.

### Использование Swagger (Open API)

Перейдя по адресу

http://localhost:8081/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса News.

Перейдя по адресу

http://localhost:8082/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса Comments.

Перейдя по адресу

http://localhost:8003/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса auth-service.

