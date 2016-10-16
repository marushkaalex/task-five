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

INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('admin@test.com', 'admin', null, 2, 1, 'u', 'NQIVudhI44LsVoR+huNe/g== DNoh6I0Jqrx2dfnaT88FgQ==', '2016-10-16 14:00:12.416000000');
INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('user@test.com', 'user', null, 2, 2, 'm', 'aihA8qaC9hmXijkXsajLrw== qLSurxx7bKoEKNaXHu9kag==', '2016-10-16 14:08:29.937000000');
INSERT INTO PUBLIC.USER (EMAIL, NICKNAME, AVATAR, RATING, ROLE_ID, GENDER, PASSWORD, DATE) VALUES ('moderator@test.com', 'moderator', null, 0, 3, 'f', '8hf6EqXRjW1dxRgzS9FCkg== wKunVcCZxY+y84Y8H2dEhA==', '2016-10-16 14:08:56.870000000');

INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Родственники', 1, 'У меня есть принцип - не давать инструмент, мобильный, ноутбук и подобные вещи никому. Печального опыта хватает. Гаечный ключ или пасатижи соседу дам конечно и соли отсыплю если попросят.
Месяц назад звонит мама - дальней родственнице надо ехать за пару тыщ км к сыну в Вуз, дай ей фотик с собой ( фотоаппарат дорогой, лет 10 хотел, подарен женой на ДР), я отказал. Даже объяснять не стал. Для троюродной тети я теперь сволочь.
А сегодня был у родителей и они мне поведали историю как дали электропилу ее мужу, он ее благополучно убил, на ремонт денег нет ни у родителей, ни у этого хрена. Пила дорогая, бошевская, нужна в деревне часто.
Попытался им объяснить - не вышло. Теперь ножовкой пилить будут, че. Помочь же надо.', null, '2016-10-16 14:11:19.254000000', 2, 1, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Здоровье....', 1, 'Только у меня так?
Работаю в нефтяной отрасли. Помбур. Работа суровая. Рабочая площадка ничем не прикрыта ни от ветра, ни от дождя. На обед заходим иногда выливая из сапог воду. Соответственно, все остальное хоть выжимай. Так хоть кто то бы заболел! А садимся в поезд домой... Начинаются сопли, кашель и тд и тп.', null, '2016-10-16 14:12:49.433000000', 0, 2, 1);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Досмотр, однако', 1, 'Проходил тут недавно рамку в ответственном и солидном учреждении, раздался писк, велели вынимать всё из карманов.

Привычным движением всё выложил, а там - железный паровоз, две чайные ложки, латунная ручка от сундука и крышка флакона из-под духов. Телефона и ключей там не оказалось.

От подозрительных и испытующих взглядов чуть не расплавился. Видимо, не похож я на отца ребёнка 6 лет.', null, '2016-10-16 14:13:15.676000000', 0, 2, 3);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Мертвые души', 1, 'Со мной приключилась довольно забавная, но, в то же время, печальная история. Я пришёл в библиотеку за "Мёртвыми душами" Н.В.Гоголя и решил подшутить на библиотекарем (молодая девушка была). Чтобы поднять настроение и себе и ей, попросил посмотреть и 2 том "Мёртвых душ". Она ушла искать книгу...', null, '2016-10-16 14:13:33.097000000', 0, 2, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Инструктор', 1, 'Звоню знакомому который работает инструктором в автошколе. Он на работе, принимает вождение по городу у очередной авто-леди.
И вот во время разговора он вдруг так мягко и нежно обращается к ней:
- Ну зачем на встречку? Неет, на встречку нам не надо, поехали по своей полосе, а?
Нервы железные, голова седая в 30.', null, '2016-10-16 14:14:08.103000000', 0, 2, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Дети)))))))))', 1, 'Случилось только что. Иду на работу, утро, спать хочу, лицо сонное. Впереди меня идет женщина лет 30-ти и ведет ребенка года 2-3, мальчик всячески пытается вырваться, крики, шум, она его держит за капюшон. Я не спеша обгоняю их и тут женщина говорит ребенку: "Вот не будешь слушаться, тебя дядя заберет". Я, решая подыграть маме, протягиваю руку ребенку и говорю: "Все, собирайся, я забираю тебя с собой". И тут произошло нечто. Мальчишка изо всех сил начал вырываться от матери, что она даже отпустила его капюшон, он подходит ко мне, хватает меня за руку и начинает идти вперед, как ни в чем не бывало.
Тут уже офигели и мама и я.', null, '2016-10-16 14:16:11.948000000', -1, 3, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Отзыв об электроплите', 1, 'Отзыв об электроплите на известном сайте:

Плюсы: Досталась от застройщика, оставила в живых

Минусы: Взорвалась духовка к такой-то матери, расплавилась проводка, подгорела кухня. Ну и пирог не получился - подгорел.', null, '2016-10-16 14:17:10.459000000', 1, 3, 2);
INSERT INTO PUBLIC.POST (TITLE, TYPE, CONTENT, COLUMN_5, DATE, RATING, AUTHOR_ID, STATUS) VALUES ('Пельмени, приправа, забывчивость', 1, 'xxx: Я ща в магазе стоял и размышлял, а приправа для пельменей - это в воду или после приготовки?
xxx: Смотрю, там соль, все дела
xxx: Думаю, точно в воду
xxx: Оказалось в фарш :D
yyy: Забыл, что их можно еще и лепить, а не только варить?', null, '2016-10-16 14:18:21.427000000', 2, 2, 2);

INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (3, 2, 1, '2016-10-16 14:12:12.411000000');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (3, 3, 1, '2016-10-16 14:14:25.767000000');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (8, 3, -1, '2016-10-16 14:16:20.423000000');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (9, 3, 1, '2016-10-16 14:17:18.287000000');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (10, 1, 1, '2016-10-16 14:19:10.819000000');
INSERT INTO PUBLIC.POST_RATING (POST_ID, USER_ID, DELTA, DATE_) VALUES (10, 2, 1, '2016-10-16 14:20:13.298000000');

INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (2, null, '2016-10-16 14:12:02.059000000', 'Мораль проста: не давай пользоваться тем, с чем не готов расстаться.', 3);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (3, 1, '2016-10-16 14:14:39.171000000', 'Эт точно', 3);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (3, null, '2016-10-16 14:16:24.852000000', ':D', 8);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (1, null, '2016-10-16 14:19:18.526000000', 'Лол', 10);
INSERT INTO PUBLIC.COMMENT (AUTHOR_ID, PARENT_ID, _DATE, TEXT, POST_ID) VALUES (2, 6, '2016-10-16 14:20:18.894000000', 'ага', 10);