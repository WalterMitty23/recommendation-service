# Recommendation Service

Spring Boot микросервис для генерации персональных рекомендаций банковских продуктов по `userId`.

## Стек технологий
- Java 17
- Spring Boot
- H2 Database (read-only)
- PostgreSQL (dynamic-datasource)
- JdbcTemplate
- Swagger (springdoc-openapi)
- Liquibase
- Telegram Bot API

## Сборка проекта

Если используешь Maven Wrapper:

    ./mvnw clean package

Если Maven установлен глобально:

    mvn clean package

После сборки будет создан JAR-файл:

    target/recommendation-service.jar

## Запуск проекта

Обычный запуск:

    java -jar target/recommendation-service.jar

С указанием профиля:

    SPRING_PROFILES_ACTIVE=default java -jar target/recommendation-service.jar

## Переменные окружения

- `SPRING_DYNAMIC_DATASOURCE_URL` — URL подключения к PostgreSQL
- `SPRING_DYNAMIC_DATASOURCE_USERNAME` — логин PostgreSQL
- `SPRING_DYNAMIC_DATASOURCE_PASSWORD` — пароль PostgreSQL
- `SPRING_PROFILES_ACTIVE` — активный профиль (`default`, `prod`)
- `TELEGRAM_BOT_USERNAME` — имя Telegram-бота
- `TELEGRAM_BOT_TOKEN` — токен Telegram-бота

## Документация

- Swagger UI: https://localhost:8080/swagger-ui.html
- OpenAPI JSON: https://localhost:8080/v3/api-docs
- Архитектура: https://github.com/WalterMitty23/recommendation-service/wiki/Architecture
- Развёртывание: https://github.com/WalterMitty23/recommendation-service/wiki/Deployment
- User Stories: https://github.com/WalterMitty23/recommendation-service/wiki/User-Stories

## Доступные API endpoints

- `GET /recommendation/{userId}` — возвращает список персональных рекомендаций.

## Тесты

Запуск тестов:

    ./mvnw test

Покрытие:
- `RecommendationServiceTest`
- `RecommendationControllerTest`

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./transaction`
- Username: `sa`
- Password: *(пусто)*
