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