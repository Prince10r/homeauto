create table if not exists water_sample(
                                           id INT NOT NULL AUTO_INCREMENT,
                                           temperature FLOAT,
                                           location VARCHAR (22),
    date TIMESTAMP,
    PRIMARY KEY (id)
    );

create table if not exists light_switch (
    id INT NOT NULL AUTO_INCREMENT,
    room_name varchar(25),
    floor TINYINT,
    on_code INT,
    off_code INT,
    toggle_code INT,
    PRIMARY KEY (id)
    );

CREATE TABLE if not exists users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY (username)
    );

CREATE TABLE  if not exists authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username)
    );

select if (
               exists(
                       select distinct index_name from information_schema.statistics
                       where table_schema = 'homeauto'
                         and table_name = 'authorities' and index_name like 'ix_auth_username'
                   )
           ,'select ''index ix_auth_username exists'' _______;'
           ,'create unique index ix_auth_username on authorities(username,authority)') into @a;
PREPARE stmt1 FROM @a;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;
