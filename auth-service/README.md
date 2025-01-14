# Система управления новостями - News Management System

Тестовое задание для Clevertec

# Описание проекта

Микросервис auth-service

Данный микросервис используется для управления безопасностью. Архитектурный стиль: REST. Вывод данных в формате JSON

Сервис используется для авторизации пользователей и формирования токена.

### Сущности:

Пользователь (User):

- id
- username
- email
- password
- role

Роли (enum Role):

- ADMIN
- JOURNALIST
- SUBSCRIBER

# Функциональные возможности

Программа реализует API для регистрации нового пользователи и авторизации.

# Зависимости

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
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

### Реализовано:

- регистрация нового пользователя
- авторизация пользователя
- валидация токена
- подключен liquibase: при запуске накатываются скрипты по генерации необходимых таблиц.
- Добавлены данные для демонстрации: admin, все пользователи задействованные в демо по сущностям новостей и комментариев
- Для демо все пароли: 123
- Подключен Open API (swagger)
- Прописан dockerFile для образа. Используется при поднятии в приложения.

# Реализована поддержка @Profile

Добавлены профили prod, dev, demo и test.

Профили prod и demo применяется для запуска и взаимодействия сервисов в docker.

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

* Пользователи
* Ответ об ошибке

### Пользователи

Описание: Этот метод добавляет нового пользователя.
Endpoint: /auth/registration
HTTP Method: POST

Parameters:

| Name     | Type   | Description      |
|----------|--------|------------------|
| username | String | Имя пользователя |
| password | String | Пароль           |
| role     | String | Роль (из enum)   |

Пример запроса:
http://localhost:8003/auth/registration

{
"username": "Patrik",
"password": "123",
"role": "SUBSCRIBER"
}

---
Описание: Этот метод возвращает сгенерированный токен после авторизации.
Endpoint: /auth/authentication
HTTP Method: GET

Parameters:

| Name     | Type   | Description      |
|----------|--------|------------------|
| username | String | Имя пользователя |
| password | String | Пароль           |

Пример запроса:
http://localhost:8003/auth/authentication

{
"username": "Patrik",
"password": "123"
}

---
Описание: Этот метод проверяет валидность токена (возвращает true или false).
Endpoint: /auth/validate
HTTP Method: GET

### Ответ об ошибке

Пример ответа об ошибке

{
"statusCode": 404,
"status": BAD REQUEST,
"message": "No such news with id 234"
}

</details>