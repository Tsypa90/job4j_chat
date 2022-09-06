create table role (
    id serial primary key,
    name varchar(250)
);

create table person (
    id  serial primary key,
    name varchar(250),
    role_id integer references role(id)
);

create table room (
    id serial primary key,
    name varchar(250)
);

create table message (
    id serial primary key,
    body text,
    person_id integer references person(id)
);

create table room_messages (
    room_id integer references room(id),
    messages_id integer references message(id)
);

create table room_persons (
    room_id integer references room(id),
    persons_id integer references person(id)
);