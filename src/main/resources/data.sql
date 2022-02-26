INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ADDRESS)
VALUES ('Макдоналдс', 'ул. Зеленая, 31'),
       ('Шаляпин', 'ул. Мира, 67'),
       ('Васаби', 'ул. Бумажная, д.20');

INSERT INTO MENU_ITEM (NAME, PRICE, ACTUAL_DATE, RESTAURANT_ID)
VALUES
--- CURRENT_DATE
('Филе-о-Фиш', 12700, CURRENT_DATE, 1),
('Чикенбургер', 5000, CURRENT_DATE, 1),
('Чикен Макнаггетс (20шт)', 27200, CURRENT_DATE, 1),

('Борщ с фасолью и чесночной пампушкой', 49000, CURRENT_DATE, 2),
('Рассольник по-шаляпински', 55000, CURRENT_DATE, 2),
('Шницель из телятины с квашеной капустой и печеным яблоком', 95000, CURRENT_DATE, 2),

('Ролл Сочная креветка', 25700, CURRENT_DATE, 3),
('Ролл Огонь', 31700, CURRENT_DATE, 3),
('Ролл Калифорния с цыпленком', 12900, CURRENT_DATE, 3),

--- 2021-06-05
('Филе-о-Фиш', 12700, '2021-06-05', 1),
('Борщ с фасолью и чесночной пампушкой', 49000, '2021-06-05', 2),
('Ролл Сочная креветка', 25700, '2021-06-05', 3),

--- 2021-06-04
('Чикенбургер', 5000, '2021-06-04', 1),
('Рассольник по-шаляпински', 55000, '2021-06-04', 2),
('Ролл Огонь', 31700, '2021-06-04', 3);


INSERT INTO VOTE (USER_ID, ACTUAL_DATE, ACTUAL_TIME, RESTAURANT_ID)
VALUES (1, CURRENT_DATE, '12:30:00', 1),
       (1, '2021-06-05', '09:15:00', 1),
       (1, '2021-06-04', '15:55:00', 3),
       (2, '2021-06-05', '08:15:00', 2),
       (2, '2021-06-04', '12:55:00', 3);