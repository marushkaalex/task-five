CREATE TABLE PUBLIC.role
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(63) NOT NULL
);

INSERT INTO PUBLIC.ROLE (NAME) VALUES ('ADMIN');
INSERT INTO PUBLIC.ROLE (NAME) VALUES ('USER');
INSERT INTO PUBLIC.ROLE (NAME) VALUES ('MODERATOR');

CREATE TABLE PUBLIC.user
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(320) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    avatar VARCHAR(512),
    rating INT,
    role_id INT,
    gender CHAR(1) DEFAULT 'u',
    password VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
    CONSTRAINT user_ROLE_ID_fk FOREIGN KEY (role_id) REFERENCES ROLE (ID)
);
CREATE UNIQUE INDEX "user_email_uindex" ON PUBLIC.user (email);
CREATE UNIQUE INDEX "user_nickname_uindex" ON PUBLIC.user (nickname);

INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('admin@test.com', 'admin', null, 0, 1, 'u', 'Hi3EkYIbZpVoFjNVBvc3dA== w4z2JiR5/LjYF+db3BXaqw==', '2016-10-16 00:00:00.000000000');
INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('moderator@test.com', 'moderator', null, 0, 3, 'f', 'BgDED+WBohy6cbw/B4zdWg== U4FxIcZfFBxyBVk1YPGXdA==', '2016-10-16 00:00:00.000000000');
INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('user@test.com', 'user', null, 0, 2, 'm', 'bGF8CzOAftV6052vUK3TgQ== X8IzmgpF0LJJwZqAhgkqfA==', '2016-10-16 00:00:00.000000000');

CREATE TABLE PUBLIC.post
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type TINYINT,
    content VARCHAR(10000) NOT NULL,
    column_5 INT,
    date DATETIME,
    rating INT,
    author_id INT,
    status INT NOT NULL,
    CONSTRAINT post_USER_ID_fk FOREIGN KEY (author_id) REFERENCES USER (ID)
);

CREATE TABLE PUBLIC.post_rating
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    user_id INT,
    delta SMALLINT,
    date_ DATETIME,
    CONSTRAINT table_name_POST_ID_fk FOREIGN KEY (post_id) REFERENCES POST (ID),
    CONSTRAINT table_name_USER_ID_fk FOREIGN KEY (user_id) REFERENCES USER (ID)
);
CREATE UNIQUE INDEX "table_name_post_id_user_id_uindex" ON PUBLIC.post_rating (post_id, user_id);

CREATE TABLE PUBLIC.comment
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    author_id INT,
    parent_id INT,
    _date DATETIME,
    text VARCHAR(1000) NOT NULL,
    post_id INT,
    CONSTRAINT comment_USER_ID_fk FOREIGN KEY (author_id) REFERENCES USER (ID),
    CONSTRAINT comment_POST_ID_fk FOREIGN KEY (post_id) REFERENCES POST (ID)
);

INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Lorem Ipsum', 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut facilisis consectetur orci. In lacinia odio sit amet nisi interdum, eu gravida neque aliquet. Suspendisse potenti. Curabitur tellus magna, viverra quis diam in, tincidunt tempor nulla. Fusce id volutpat leo. Morbi placerat tellus et sem mattis interdum. Aenean et arcu urna. Ut mattis laoreet varius. Morbi eros elit, pharetra a nisi ac, hendrerit viverra ipsum.

Suspendisse id bibendum est. Vivamus at dui commodo turpis molestie elementum in eget risus. Morbi consequat tempus sapien, nec pharetra diam mollis sed. In hac habitasse platea dictumst. Ut a finibus nunc, nec pulvinar lacus. Praesent ac mauris tincidunt, bibendum risus in, ornare erat. Aenean mattis, purus et molestie dapibus, neque felis porta dui, in consequat urna metus non lacus. Aliquam tincidunt malesuada quam, dapibus mattis ligula suscipit eget. Donec fermentum augue vitae dignissim imperdiet. Aliquam erat volutpat. Pellentesque ut mattis neque. Nulla facilisi. Aliquam sed laoreet erat. Mauris et eros mauris. Sed ut velit in libero consectetur laoreet a eget ex. In a mauris at dolor rutrum pretium eu a lectus.', null, '2016-10-16', 1, 74, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('dolor sit amet', 1, 'Donec vel nulla cursus neque condimentum lacinia hendrerit sit amet sapien. Duis tellus eros, vulputate et pellentesque vitae, tempor non eros. Aenean lobortis tempor diam quis dapibus. Quisque lacinia, mauris sed porttitor efficitur, libero felis auctor magna, ut hendrerit arcu leo nec quam. Nulla nec est malesuada, finibus neque in, elementum quam. Nam ut faucibus tortor, sit amet imperdiet tortor. Vivamus vitae turpis nec enim blandit dignissim non quis lorem. Morbi eget eleifend eros. Sed a urna in velit malesuada varius. Sed a diam fermentum nunc placerat bibendum. In nec ex a mi convallis porta. Etiam bibendum consequat faucibus.', null, '2016-10-16', 0, 74, 3);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Ну он хотя бы попытался', 1, 'Кольцо героев, обычных парней с горящими сердцами, сжимается вокруг нас всё сильнее.
Один из последних примеров — Майкл, заподозривший, что что–то неладное происходит в доме соседей (это был начинающийся пожар), долго пытавшийся достучаться, кричавший и звавший соседей на помощь. Наконец, когда он понял, что помощи ждать ему неоткуда, он сел в свой чёрный BMW, сломал соседских забор, забежал к дому с заднего двора, выломал дверь и вытащил из пылающего ада соседского белого пса!

С этим псом на руках он и встретил дежурный полицейский наряд, вызванный перепуганными соседями и как раз прибывший по вызову на место. Служивые объяснили Майклу, что в этом деле не всё так просто — пожара, как оказалось, не было, а Майкл просто в неверной пропорции намешал лекарство от кашля с ЛСД.', null, '2016-10-16', 1, 73, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Про находки', 1, 'Вот тут пишут много про "нашел-отдал, а мне за это мульенн дали" или наоборот: "нашел-отдал-а мне из мульена только 5 копеек дали, жмоты"
Работала я как-то в туристическом магазине, смена в субботу, раннее утро,клиентов мало было. И вдруг мы с напарником(вдвоем работали), обнаружили кошелек. Внутрь заглянули, никаких данных, только деньги. Много денег. Нам на столько нужно месяца 3 вдвоем без выходных работать. Первым делом у нас проснулась алчность, типа "ОООООООО!!!!!". Но люди мы все-таки честные. Подумали, кто бы мог оставить. Было два варианта. Мужики, которые одежду мерили, потому что кошелек на веревочке, нужно было снимать.
Посовещавшись решили, что убираем все это богатство подальше, и, если за 2 месяца за ним не вернутся, поделить по-братски.
Но мужик вернулся через час. Спросил, не находили ли чего. Отдали,ничего не получив в замен. Обидно, конечно было. Но потом мужик стал постоянным клиентом и приходил только в наши с напарником смены. От других консультантов отмахивался. Брал всегда много и дорого, что для нас, сидящих на проценте, было огромным плюсом. Так что свое вознаграждение мы все-таки получили. Ну и плюсом, после оформления покупки мы с ним всегда курить выходили. Про всякие места нашей страны оч интересно рассказывал.', null, '2016-10-16', -1, 74, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Да это соседский пацан!', 1, 'Утром иду на работу. Из подъезда вываливается пацаненок лет 7 и дребезжа в пакете бутылками идет к мусорному бачку. С балкона слышу крик бати.
- Да не шуми ты так сильно.
Курит на балконе и смотрит как его чадо выкидывает бутылки.
Пацан пытается дотянуться до высокого бачка, но рост парнишки и вес бутылок в пакете не дают ему сделать этого, отчего дребезжит еще сильнее. Я подхожу, хватаю пакет и кидаю в мусорку. И оглядываюсь на балкон.
- Да не мой это пацан, это вообще соседский. Бросает бычок и закрывает окно.', null, '2016-10-16', 0, 75, 1);

INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (103, 73, 1, '2016-10-16');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (105, 74, 1, '2016-10-16');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (106, 75, -1, '2016-10-16');

INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (73, null, '2016-10-16 00:00:00.000000000', 'Lol!', 103);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (75, null, '2016-10-16 00:00:00.000000000', 'Bayan', 104);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (73, 7, '2016-10-16 00:00:00.000000000', 'Norm je!', 104);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (74, null, '2016-10-16 00:00:00.000000000', 'Хах, зачет :D', 105);