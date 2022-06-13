# REST API

Этот документ содержит спецификацию API для панели управления ботом.

## Общее

Тело запроса должно быть в формате JSON. Ответ так же всегда приходит в формате JSON.

Все запросы кроме `/login` должны иметь токен доступа в заголовке. Пример:

```
Authentication: <access token>
```

## Ошибки

На авторизованый запрос может прийти ошибка в формате JSON:

```json
{
  "error": "<error type>",
  "message": "<error description>"
}
```

Где `<error>` - код ошибки в строков виде, а `<message>` - более подробное описание ошибки.

Коды ошибок указаны в описании каждой конечной точки. 

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
  "subsTeacher": 50,
  "subsConsult": 12,
  "subsCourse": 20,
  "subsGroup": 65,
  "subsPoints": 40
}
```

### [GET] /properties

Получить общие настройки расписания.

**Ответ**

```json
{
  "checkRate": 30,
  "compAuds": [
    "007",
    "009",
    "..."
  ],
  "dayIndexes": {
    "day1": 0,
    "day2": 1,
    "...": 2
  }
}
```

### [POST] /properties

Изменить общие настройки расписания.

**Запрос**
```json
{
  "checkRate": 30,
  "compAuds": [
    "007",
    "009",
    "..."
  ],
  "dayIndexes": {
    "day1": 0,
    "day2": 1,
    "...": 2
  }
}
```

**Ответ**
```json
{
  "success": true
}
```

### [GET] /teachers

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

### [POST] /teachers

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

### [GET] /consult

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

### [POST] /consult

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

### [GET] /courses

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

### [POST] /courses

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

### [GET] /rendering

Получить настройки ренедеринга.

**Ответ**
```json
{
  "format": "<JPEG | PNG | GIF>",
  "dpi": 175
}
```

### [POST] /rendering

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
