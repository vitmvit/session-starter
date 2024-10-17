# user-service - тестовый микросервис для работы с пользователями

## Описание

- База данных PostgreSql;

## Запуск

Перед запуском должна быть создана бд user-service.

## Swagger (profile - dev)

http://localhost:8080/api/doc/swagger-ui/index.html

## Контроллеры

### UserController (/api/v1/users)

#### GET ResponseEntity<UserDto> findById(@RequestParam(value = "login", required = false) SessionCreateDto createDto, @PathVariable Long id, SessionDto dto:

Request:

```http request
http://localhost:8080/api/v1/users/1?login=user1
```

Response:

```json
{
  "id": 1,
  "login": "user_1",
  "password": "user_1",
  "sessionOpen": "2024-10-17T20:35:37.893995"
}
```

User not found:

```json
{
  "errorMessage": "User not found!",
  "errorCode": 404
}
```

#### POST ResponseEntity<UserDto> create(@RequestBody UserCreateDto userCreateDto, SessionDto dto):

Request:

```http request
http://localhost:8080/api/v1/users
```

Body:

```json
{
  "login": "user2",
  "password": "user2"
}
```

Response:

```json
{
  "id": 2,
  "login": "user2",
  "password": "user2",
  "sessionOpen": "2024-10-18T00:34:17.391171"
}
```

User create error:

```json
{
  "errorMessage": "User create exception: exception",
  "errorCode": 404
}
```

User in blacklist:

```json
{
  "errorMessage": "This login is blacklisted",
  "errorCode": 400
}
```