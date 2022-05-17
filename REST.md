# REST API

Этот документ содержит спецификацию API для панели управления ботом.

## Общее

Тело запроса должно быть в формате JSON. Ответ так же всегда приходит в формате JSON.

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

Коды специфичных ошибок указаны в описании каждой конечной точке. 

**Общие ошибки:**

* `access_denied` - Недействительный токен

## Endpoints

### [POST] /login

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

### [GET] /logout

Завершить текущую сессию.

**Ответ**
```json
{
  "success": true
}
```

### [GET] /stats

Получить статистику сервиса.

**Ответ**
```json
{
  "users": 100,
  "subs_teacher": 50,
  "subs_consult": 12,
  "subs_course": 20,
  "subs_group": 65,
  "subs_points": 40
}
```

### [GET] /checkRate/get

Получить настройки проверки обновлений в расписании.

**Ответ**
```json
{
  "rate": 30
}
```

### [POST] /checkRate/edit

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

### [GET] /computerRooms/get

Получить настройки компьютерных аудиторий

**Ответ**
```json
{
  "success": true
}
```

### [POST] /computerRooms/edit

Изменить настройки компьютерных аудиторий.

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

### [GET] /dayIndexes/get

Получить настройки сопоставления имени дня с его индексом.

**Ответ**
```json
{
  "days": {
    "<day1>": 0,
    "<day2>": 1
  }
}
```

### [POST] /dayIndexes/edit

Изменить настройки сопоставления имени дня с его индексом.

**Запрос**
```json
{
  "days": {
    "<day1>": 0,
    "<day2>": 1
  }
}
```

**Ответ**
```json
{
  "success": true
}
```

### [GET] /teachers/get

Получить настройки расписания преподавателей.

**Ответ**
```json
{
  "url": "<document url>",
  "associations": {
    "<keyword1>": "<sheet link>",
    "<keywordN>": "<sheet link>"
  }
}
```

### [POST] /teachers/edit

Изменить настройки расписания преподавателей.

**Запрос**
```json
{
  "url": "<document url>",
  "associations": {
    "<keyword1>": "<sheet link>",
    "<keywordN>": "<sheet link>"
  }
}
```

**Ответ**
```json
{
  "success": true
}
```

### [GET] /consult/get

Получить настройки расписания консультаций.

**Ответ**
```json
{
  "url": "<document url>",
  "dayPoint": {
    "col": 1,
    "row": 2
  },
  "teacherPoint": {
    "col": 0,
    "row": 3
  }
}
```

### [POST] /consult/edit

Изменить настройки расписания консультаций.

**Запрос**
```json
{
  "url": "<document url>",
  "dayPoint": {
    "col": 1,
    "row": 2
  },
  "teacherPoint": {
    "col": 0,
    "row": 3
  }
}
```

**Ответ**
```json
{
  "success": true
}
```

### [GET] /courses/get

Получить настройки раписания курсов.

**Ответ**
```json
{
  "courses": [
    {
      "url": "<document url>",
      "name": "<course name>",
      "dayPoint": {
        "col": 1,
        "row": 2
      },
      "groupPoint": {
        "col": 0,
        "row": 3
      }
    }
  ]
}
```

### [POST] /courses/edit

Изменить настройки раписания курсов.

**Запрос**
```json
{
  "courses": [
    {
      "url": "<document url>",
      "name": "<course name>",
      "dayPoint": {
        "col": 1,
        "row": 2
      },
      "groupPoint": {
        "col": 0,
        "row": 3
      }
    }
  ]
}
```

**Ответ**
```json
{
  "success": true
}
```

### [GET] /rendering/get

Получить настройки ренедеринга.

**Ответ**
```json
{
  "format": "<JPEG | PNG | GIF>",
  "dpi": 175
}
```

### [POST] /rendering/edit

Изменить настройки ренедеринга.

**Запрос**
```json
{
  "format": "<JPEG | PNG | GIF>",
  "dpi": 175
}
```

**Ответ**
```json
{
  "success": true
}
```

## Admin Endpoints

Данное API доступно только для пользователей, имеющих право администратора.

### [GET] /user/list

Получить список пользователей панели управления.

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

### [GET] /user/sessions

Получить список сессий пользоватлей панели управления.

**Ответ**
```json
{
  "sessions": [
    {
      "login": "<user login>",
      "token": "<session token>",
      "expiryAfter": 10000,
      "agent": "<user agent name>"
    }
  ]
}
```

### [POST] /user/endSession

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

### [POST] /user/create

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

### [POST] /user/edit

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

### [POST] /user/delete

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