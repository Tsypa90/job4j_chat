# Job4j

## Chat

В данном проекте будет разработан чат с комнатами на Rest Api
Четыре модели Person. Role. Room, Message.
В каждой комнате есть свой список Person and Messages.
USER может только добавлять сообщения и редактировать их,
а ADMIN может еще удалять сообщения и Person.

Стек технологий:
- Rest Api
- Spring Boot
- Spring Data
- Spring Security
- Lombok
- Maven

Для запуска приложения Вам понадобиться установить Java 17, PostgreSQL 14.5.
1. Необходимо создать БД с именем chat
2. В консоли сменить текущую директорию на директорию проекта
3. Прописать в консоль команду и запустить приложение!
````
   mvn spring-boot:run
````

Person:
- localhost:8080/person/sign-up (Post) - создание нового пользователя.
- localhost:8080/login (Post) - вход для пользователя
- localhost:8080/person (Get) - получение всех пользователей
- localhost:8080/person/{id} (Get) - получение определенного пользователя по id
- localhost:8080/person (Put, Patch) - обновление данных пользователя
- localhost:8080/person/{id} (Delete) - удаление пользователя по id

Room:
- localhost:8080/room (Get) - получение всех комнат чата
- localhost:8080/room/{id} (Get) - получение определенной комнаты чата
- localhost:8080/room/{roomId}/enter?personId={?} (Put) - для входа в комнату по id комнаты и id пользователя
- localhost:8080/room/{roomId}/exit?personId={?} (Put) - для выхода из комнаты по id комнаты и id пользователя

Message:
- localhost:8080/message?id={?} (Get) - получение всех сообщений пользователя по id
- localhost:8080/message?roomId={?}&personId={?} (Post) - отправка сообщения
- localhost:8080/message (Patch) - обновление сообщения
- localhost:8080/message (Put) - обновление сообщения
- localhost:8080/message?roomId={?}&personId={?} (Delete) - удаление сообщения