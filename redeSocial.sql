create table users
(
    id       serial
        constraint users_pkey
            primary key,
    name     varchar(100),
    email    varchar(100),
    password varchar(100)
);

alter table users
    owner to admin;

create table messages
(
    id           serial
        constraint messages_pkey
            primary key,
    sender_id    integer
        constraint fk_user_sender_id
            references users,
    receiver_id  integer
        constraint fk_user_receiver_id
            references users,
    message_text varchar(500)
);

alter table messages
    owner to admin;

create unique index users_email_uindex
    on users (email);

create table friendship
(
    id        serial
        constraint friendship_pkey
            primary key,
    user_id   integer
        constraint friendship_user_id_fkey
            references users,
    friend_id integer
        constraint friendship_friend_id_fkey
            references users
);

alter table friendship
    owner to admin;

