# REST API

Этот документ содержит спецификацию API для панели управления ботом.

## Общее

Коммуникация с API проивзодится отправкой POST запросов в следующем формате:

```
https://<base_url>:<port>/<endpoint>
```

* `<base_url>` - Доменое имя или IP адрес сервера
* `<port>` - Порт сервера
* `<endpoint>` - Конечная точка для сообщения

Тело запроса должно быть в формате JSON. Ответ так же всегда приходит в формате JSON

Все запросы кроме `/login` должны иметь токен доступа в заголовке. Пример:

```
Authentication: <access token>
```

## Ошибки

На любой запрос может прийти ошибка вместо ответа. Сообщение ошибки имеет следующий формат:

```json
{
  "error": "<error type>",
  "message": "<error description>"
}
```

Где `<error>` - код ошибки в строков виде, а `<message>` - более подробное описание ошибки.

Коды ошибок указаны в описании каждой конечной точке.

## Endpoints

### /login

Войти в систему и получить токен доступа.

**Запрос**
```json
{
  "login": "<login>",
  "password": "<password>"
}
```

**Ответ**
```json
{
  "success": true,
  "accessToken": "<random access token>"
}
```

**Возможные ошибки**

* `invalid_login`
* `invalid_password`

### /logout

Завершить текущую сессию

**Запрос**

Отсутствует

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `user_not_found`

### /checkRate/configure

Изменить настройки проверки обновлений в расписании.

**Запрос**
```json
{
  "rate": 20
}
```

**Ответ**
```json
{
  "success": true
}
```

### /computerRooms/configure

Изменить настройки компьютерных аудиторий

**Запрос**
```json
{
  "rooms": [
    "007",
    "023"
  ]
}
```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /dayIndexes/configure

Изменить настройки сопоставления имени дня с его индексом

**Запрос**
```json

```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /teachers/configure

Изменить настройки расписания преподавателей

**Запрос**
```json

```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /consult/configure

Изменить настройки расписания консультаций

**Запрос**
```json

```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /courses/configure

Изменить настройки раписания курсов

**Запрос**
```json

```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /rendering/configure

Изменить настройки ренедеринга

**Запрос**
```json

```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `fff` -
* `fff` -

### /stats

Получить статистику сервиса

**Запрос**

Отсутствует

**Ответ**
```json
{
  "users": 100,
  "subs_teacher": 50,
  "subs_consult": 12,
  "subs_course": 20,
  "subs_group": 65
}
```

## Admin Endpoints

Данное API доступно только для пользователей, имеющих право администратора.

### /user/list

Получить список пользователей панели управления.

**Запрос**

Отсутствует

**Ответ**
```json
{
  "users": [
    {
      "login": "<user login>",
      "isAdmin": true
    }
  ]
}
```

### /user/sessions

Получить список сессий пользоватлей панели управления.

**Запрос**

Отсутствует

**Ответ**
```json
{
  "sessions": [
    {
      "token": "<session token>",
      "agent": "<user agent name>"
    }
  ]
}
```

### /user/endSession

Завершить сессию пользователя панели управления.

**Запрос**
```json
{
  "token": "<other session token>"
}
```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `invalid_token`

### /user/create

Создать нового пользователя панели управления.

**Запрос**
```json
{
  "login": "<login>",
  "password": "<password>",
  "isAdmin": true
}
```

**Ответ**

```json
{
  "success": true
}
```

**Возможные ошибки**

* `login_taken`
* `password_short`

### /user/edit

Изменить существующего пользователя панели управления.

**Запрос**
```json
{
  "login": "<login>",
  "password": "<password>",
  "isAdmin": true
}
```

**Ответ**
```json
{
  "success": true
}
```

**Возможные ошибки**

* `user_not_found`
* `password_short`

### /user/delete

Удалить пользователя панели управления.

**Запрос**
```json
{
  "login": "<user login>"
}
```

**Ответ**
```json
{
  "success": true
}
```