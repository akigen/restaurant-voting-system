--liquibase formatted sql

--changeset gkislin:init_schema
create table USERS
(
    ID         INTEGER auto_increment primary key,
    NAME       VARCHAR(100)            not null,
    EMAIL      VARCHAR(100)            not null
        constraint UK_EMAIL unique,
    ENABLED    BOOL      default TRUE  not null,
    PASSWORD   VARCHAR(100)            not null,
    REGISTERED TIMESTAMP default NOW() not null
);

create table USER_ROLES
(
    USER_ID INTEGER not null,
    ROLE    VARCHAR(255),
    constraint UK_USER_ROLES unique (USER_ID, ROLE),
    constraint FK_USER_ROLES foreign key (USER_ID) references USERS (ID) on delete cascade
);

create table RESTAURANT
(
    ID      INTEGER auto_increment primary key,
    NAME    VARCHAR(100)      not null,
    ADDRESS VARCHAR(1024),
    ENABLED BOOL default TRUE not null,
    constraint UK_RESTAURANT unique (NAME, ADDRESS)
);

create table VOTE
(
    ID            INTEGER auto_increment primary key,
    ACTUAL_DATE   DATE    not null,
    ACTUAL_TIME   TIME    not null,
    RESTAURANT_ID INTEGER,
    USER_ID       INTEGER not null,
    constraint UK_VOTE unique (USER_ID, ACTUAL_DATE),
    constraint FK_VOTE_RESTAURANT foreign key (RESTAURANT_ID) references RESTAURANT (ID),
    constraint FK_VOTE_USER foreign key (USER_ID) references USERS (ID) on delete cascade
);

create table DISH_REF
(
    ID            INTEGER auto_increment primary key,
    NAME          VARCHAR(100)      not null,
    ENABLED       BOOL default TRUE not null,
    PRICE         INTEGER           not null,
    RESTAURANT_ID INTEGER           not null,
    constraint UK_DISH_REF unique (RESTAURANT_ID, NAME),
    constraint FK_DISH_REF_RESTAURANT foreign key (RESTAURANT_ID) references RESTAURANT (ID) on delete cascade
);

create table MENU_ITEM
(
    ID            INTEGER auto_increment primary key,
    ACTUAL_DATE   DATE    not null,
    DISH_REF_ID   INTEGER,
    RESTAURANT_ID INTEGER not null,
    constraint UK_MENU_ITEM unique (ACTUAL_DATE, DISH_REF_ID),
    constraint FK_MENU_ITEM_RESTAURANT foreign key (RESTAURANT_ID) references RESTAURANT (ID) on delete cascade,
    constraint FK_MENU_ITEM_DISH_REF foreign key (DISH_REF_ID) references DISH_REF (ID)
);

--changeset gkislin:populate_data
INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ADDRESS, ENABLED)
VALUES ('Макдоналдс', 'ул. Зеленая, 31', true),
       ('Шаляпин', 'ул. Мира, 67', false),
       ('Васаби', 'ул. Бумажная, д.20', true);

INSERT INTO DISH_REF (NAME, PRICE, RESTAURANT_ID, ENABLED)
VALUES ('Филе-о-Фиш', 12700, 1, true),
       ('Чикенбургер', 5000, 1, true),
       ('Чикен Макнаггетс (20шт)', 27200, 1, true),

       ('Борщ с фасолью и чесночной пампушкой', 49000, 2, true),
       ('Рассольник по-шаляпински', 55000, 2, true),
       ('Шницель из телятины с квашеной капустой и печеным яблоком', 95000, 2, true),

       ('Ролл Сочная креветка', 25700, 3, true),
       ('Ролл Огонь', 31700, 3, false),
       ('Ролл Калифорния с цыпленком', 12900, 3, true);

INSERT INTO MENU_ITEM (ACTUAL_DATE, RESTAURANT_ID, DISH_REF_ID)
VALUES
--- CURRENT_DATE
(CURRENT_DATE, 1, 1),
(CURRENT_DATE, 1, 2),
(CURRENT_DATE, 1, 3),

(CURRENT_DATE, 2, 4),
(CURRENT_DATE, 2, 5),
(CURRENT_DATE, 2, 6),

(CURRENT_DATE, 3, 7),
(CURRENT_DATE, 3, 8),
(CURRENT_DATE, 3, 9),

--- 2021-06-05
('2021-06-05', 1, 1),
('2021-06-05', 2, 4),
('2021-06-05', 3, 7),

--- 2021-06-04
('2021-06-04', 1, 2),
('2021-06-04', 2, 6),
('2021-06-04', 3, 8);

INSERT INTO VOTE (USER_ID, ACTUAL_DATE, ACTUAL_TIME, RESTAURANT_ID)
VALUES (1, CURRENT_DATE, '12:30:00', 1),
       (1, '2021-06-05', '09:15:00', 1),
       (1, '2021-06-04', '15:55:00', 3),
       (2, '2021-06-05', '08:15:00', 2),
       (2, '2021-06-04', '12:55:00', 3);
