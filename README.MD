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

Для запуска приложения Вам понадобиться установить Java 17, pgAdmin 4.
1. Необходимо создать БД с именем chat
2. Создать таблицы, схемы для инициализации таблиц взять из файла 001_ddl_init_schema.sql
3. Занести в таблицы Role и Room данные из файла 002_dml_insert_role_room.sql
4. В консоли сменить текущую директорию на директорию проекта
5. Прописать в консоль команду и запустить приложение!
   java -jar job4j_chat-0.0.1-SNAPSHOT.jar

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