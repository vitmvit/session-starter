# session-service - микросервис для работы с сессиями

## Описание

- База данных mongoDB;
- Содержит методы создания и получения текущей сессии по логину;
- В сессии содержит айдишник, логин, которому принадлежит сессия и время открытия сессии;
- Сессия активна 24 часа, далее шедулер ее очистит.

## Запуск

Перед запуском должна быть создана бд Sessions с коллекцией Sessions.

## Swagger (profile - dev)

http://localhost:8081/api/doc/swagger-ui/index.html

## Контроллеры

### SessionController (/api/v1/sessions)

#### GET ResponseEntity<SessionDto> getSession(@RequestParam String login):

Request:

```http request
http://localhost:8081/api/v1/sessions?login=user@mail.com
```

Response:

```json
{
  "id": "671013fe46944e0b736afb77",
  "login": "user@mail.com",
  "dateCreate": "2024-10-16T22:29:02.142414"
}
```

Session create exception:

```json
{
  "errorMessage": "Create session exception: exception",
  "errorCode": 500
}
```